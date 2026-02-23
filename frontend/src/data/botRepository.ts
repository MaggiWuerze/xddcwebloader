import type {GridFilterModel, GridPaginationModel, GridSortModel} from '@mui/x-data-grid';
import type {BaseRepository} from './base/baseRepository';
import {BotControllerApi, type BotFormTO, type BotTO} from '../api/rest';

const api = new BotControllerApi();

type ValidationResult = { issues: { message: string; path: (keyof BotTO)[] }[] };
/**
 * Bot repository:
 * - T = BotTO (read model)
 * - F = TargetBotForm (write model)
 * - ID = string (UUID)
 */
export const BotRepository: BaseRepository<BotTO, BotFormTO> = {

    validate(bot: Partial<BotFormTO>): ValidationResult {
        let issues: ValidationResult['issues'] = [];

        if (!bot.name) {
            issues = [...issues, {message: 'Name is required', path: ['name']}];
        }

        if (!bot.pattern) {
            issues = [...issues, {message: 'Pattern is required', path: ['pattern']}];
        }

        if (!bot.channelId) {
            issues = [...issues, {message: 'Channel is required', path: ['channel']}];
        }

        if (!bot.serverId) {
            issues = [...issues, {message: 'Server is required', path: ['server']}];
        }

        if (!bot.maxParallelDownloads) {
            issues = [...issues, {message: 'MaxParallelDownloads is required', path: ['maxParallelDownloads']}];
        }

        return {issues};
    },

    async listAll(): Promise<BotTO[]> {
        return await this.list({
            paginationModel: {page: 0, pageSize: 1000},
            sortModel: [],
            filterModel: {items: []},
        }).then(server => server.items);
    },

    async list({
                   paginationModel = {page: 0, pageSize: 1000},
                   sortModel = [],
                   filterModel = {items: []},
               }: {
        paginationModel?: GridPaginationModel;
        sortModel?: GridSortModel;
        filterModel?: GridFilterModel;
    }): Promise<{ items: BotTO[]; itemCount: number }> {
        const bots = await api.listBots().then((rs) => rs.data);

        let filteredBots = [...bots];

        // Apply filters (same style as bot.ts)
        if (filterModel?.items?.length) {
            filterModel.items.forEach(({field, value, operator}) => {
                if (!field || value == null) {
                    return;
                }

                filteredBots = filteredBots.filter((bot) => {
                    const botValue = bot[field as keyof BotTO];

                    switch (operator) {
                        case 'contains':
                            return String(botValue).toLowerCase().includes(String(value).toLowerCase());
                        case 'equals':
                            return botValue === (value as unknown as BotTO[keyof BotTO]);
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
                    const botA = a[field as keyof BotTO];
                    const botB = b[field as keyof BotTO];

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

    async get(id: string): Promise<BotTO> {
        return await api.getBot(id).then((res) => res.data);
    },

    async create(data: Omit<BotFormTO, 'id'>): Promise<BotTO> {
        return await api.createBot({...data}).then((res) => res.data);
    },

    async update(_id: string, _data: Omit<BotFormTO, 'id'>): Promise<BotTO> {
        return await api.updateBot(_id, _data).then((res) => res.data);
    },

    async delete(id: string): Promise<void> {
        await api.deleteBot(id);
    }
};