import type {GridFilterModel, GridPaginationModel, GridSortModel} from '@mui/x-data-grid';
import type {BaseRepository} from './base/baseRepository';
import {ChannelControllerApi, type ChannelFormTO, type ChannelTO, Configuration} from '../api/rest';
import {EnvironmentService} from "./environmentService";

const baseUrl = EnvironmentService.getBaseUrl();
const api = new ChannelControllerApi(new Configuration({ basePath: baseUrl }));

type ValidationResult = { issues: { message: string; path: (keyof ChannelTO)[] }[] };
/**
 * Bot repository:
 * - T = BotTO (read model)
 * - F = TargetBotForm (write model)
 * - ID = string (UUID)
 */
export const ChannelRepository: BaseRepository<ChannelTO, ChannelFormTO> = {

    validate(channel: Partial<ChannelTO>): ValidationResult {
        let issues: ValidationResult['issues'] = [];

        if (!channel.name) {
            issues = [...issues, {message: 'Name is required', path: ['name']}];
        }

        return {issues};
    },

    async listAll(): Promise<ChannelTO[]> {
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
    }): Promise<{ items: ChannelTO[]; itemCount: number }> {
        const bots = await api.listChannels().then((rs) => rs.data);

        let filteredBots = [...bots];

        // Apply filters (same style as bot.ts)
        if (filterModel?.items?.length) {
            filterModel.items.forEach(({field, value, operator}) => {
                if (!field || value == null) {
                    return;
                }

                filteredBots = filteredBots.filter((bot) => {
                    const botValue = bot[field as keyof ChannelTO];

                    switch (operator) {
                        case 'contains':
                            return String(botValue).toLowerCase().includes(String(value).toLowerCase());
                        case 'equals':
                            return botValue === (value as unknown as ChannelTO[keyof ChannelTO]);
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
                    const botA = a[field as keyof ChannelTO];
                    const botB = b[field as keyof ChannelTO];

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

    async get(id: string): Promise<ChannelTO> {
        return await api.getChannel(id).then((res) => res.data);
    },

    async create(data: Omit<ChannelFormTO, 'id'>): Promise<ChannelTO> {
        return await api.createChannel({...data}).then((res) => res.data);
    },

    async update(_id: string, _data: Omit<ChannelFormTO, 'id'>): Promise<ChannelTO> {
        return await api.updateChannel(_id, _data).then((res) => res.data);
    },

    async delete(id: string): Promise<void> {
        await api.deleteChannel(id);
    }
};