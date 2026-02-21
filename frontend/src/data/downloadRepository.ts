import type {GridFilterModel, GridPaginationModel, GridSortModel} from '@mui/x-data-grid';
import {DownloadControllerApi, DownloadFormTO, DownloadTO} from '../api/rest';

const api = new DownloadControllerApi();

type ValidationResult = { issues: { message: string; path: (keyof DownloadFormTO)[] }[] };


export enum ListTypes {
    ALL,
    ACTIVE,
    FINISHED,
    CANCELLED
}

/**
 * Bot repository:
 * - T = BotTO (read model)
 * - F = TargetBotForm (write model)
 * - ID = string (UUID)
 */
export const DownloadRepository = {

    validate(download: Partial<DownloadFormTO>): ValidationResult {
        let issues: ValidationResult['issues'] = [];

        if (!download.fileRefId) {
            issues = [...issues, {message: 'Bot is required', path: ['fileRefId']}];
        }

        if (!download.targetBotId) {
            issues = [...issues, {message: 'Filename is required', path: ['targetBotId']}];
        }

        return {issues};
    },

    async cancel(id: string) {
        return await api.removeDownload(id);
    },

    async get(id: string): Promise<DownloadTO> {
        return await api.getDownload(id).then((res) => res.data);
    },

    async listAll(): Promise<DownloadTO[]> {
        return await this.list({
            paginationModel: {page: 0, pageSize: 1000},
            sortModel: [],
            filterModel: {items: []},
        }, ListTypes.ALL).then(server => server.items);
    },

    async list({
                   paginationModel,
                   filterModel,
                   sortModel,
               }: {
        paginationModel: GridPaginationModel;
        sortModel: GridSortModel;
        filterModel: GridFilterModel;
    }, listType: ListTypes): Promise<{ items: DownloadTO[]; itemCount: number }> {
        let botData;

        switch (listType) {
            case ListTypes.ALL:
                botData = await api.listDownloads().then((rs) => rs.data);
                break;
            case ListTypes.ACTIVE:
                botData = await api.getActiveDownloads(true).then((rs) => rs.data);
                break;
            case ListTypes.FINISHED:
                botData = await api.getActiveDownloads(false).then((rs) => rs.data);
                break;
            case ListTypes.CANCELLED:
                botData = await api.failedDownloads().then((rs) => rs.data);
                break;

        }

        let filteredBots = [...botData];

        // Apply filters (same style as bot.ts)
        if (filterModel?.items?.length) {
            filterModel.items.forEach(({field, value, operator}) => {
                if (!field || value == null) {
                    return;
                }

                filteredBots = filteredBots.filter((bot) => {
                    const botValue = bot[field as keyof DownloadTO];

                    switch (operator) {
                        case 'contains':
                            return String(botValue).toLowerCase().includes(String(value).toLowerCase());
                        case 'equals':
                            return botValue === (value as unknown as DownloadTO[keyof DownloadTO]);
                        case 'startsWith':
                            return String(botValue).toLowerCase().startsWith(String(value).toLowerCase());
                        case 'endsWith':
                            return String(botValue).toLowerCase().endsWith(String(value).toLowerCase());
                        case '>':
                            return botValue === undefined || (botValue as any) > value;
                        case '<':
                            return botValue === undefined || (botValue as any) < value;
                        default:
                            return true;
                    }
                });
            });
        }

        // Apply sorting (same style as bot.ts)
        if (sortModel?.length) {
            filteredBots.sort((a, b) => {
                for (const {field, sort} of sortModel) {
                    const botA = a[field as keyof DownloadTO];
                    const botB = b[field as keyof DownloadTO];

                    if (botA === undefined || botB === undefined) {
                        return 0;
                    }

                    if (botA < botB) {
                        return sort === 'asc' ? -1 : 1;
                    }
                    if (botA > botB) {
                        return sort === 'asc' ? 1 : -1;
                    }
                }
                return 0;
            });
        }

        // Apply pagination (same style as bot.ts)
        const start = paginationModel.page * paginationModel.pageSize;
        const end = start + paginationModel.pageSize;
        const paginatedBots = filteredBots.slice(start, end);

        return {
            items: paginatedBots,
            itemCount: filteredBots.length,
        };
    },

    async create(data: Omit<DownloadFormTO, 'id'>): Promise<DownloadTO[]> {
        return await api.addDownload({...data}).then((res) => res.data);
    },
};