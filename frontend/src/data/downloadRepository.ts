import type {GridFilterModel, GridPaginationModel, GridSortModel} from '@mui/x-data-grid';
import {DownloadControllerApi, DownloadTO} from '../api/rest';

const api = new DownloadControllerApi();

type ValidationResult = { issues: { message: string; path: (keyof DownloadTO)[] }[] };
/**
 * Bot repository:
 * - T = BotTO (read model)
 * - F = TargetBotForm (write model)
 * - ID = string (UUID)
 */
export const DownloadRepository = {

    validate(download: Partial<DownloadTO>): ValidationResult {
        let issues: ValidationResult['issues'] = [];

        if (!download.bot) {
            issues = [...issues, {message: 'Bot is required', path: ['bot']}];
        }

        if (!download.filename) {
            issues = [...issues, {message: 'Filename is required', path: ['filename']}];
        }

        if (!download.filesize) {
            issues = [...issues, {message: 'FileSize is required', path: ['filesize']}];
        }

        return {issues};
    },

    async list({
                   paginationModel,
                   filterModel,
                   sortModel,
               }: {
        paginationModel: GridPaginationModel;
        sortModel: GridSortModel;
        filterModel: GridFilterModel;
    }): Promise<{ items: DownloadTO[]; itemCount: number }> {
        const bots = await api.getActiveDownloads(true).then((rs) => rs.data);

        let filteredBots = [...bots];

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
    }
};