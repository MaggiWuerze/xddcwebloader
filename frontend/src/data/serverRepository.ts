import type {GridFilterModel, GridPaginationModel, GridSortModel} from '@mui/x-data-grid';
import type {BaseRepository} from './base/baseRepository';
import {ServerControllerApi, type ServerFormTO, ServerTO} from '../api/rest';


const api = new ServerControllerApi();

type ValidationResult = { issues: { message: string; path: (keyof ServerTO)[] }[] };
/**
 * Bot repository:
 * - T = BotTO (read model)
 * - F = TargetBotForm (write model)
 * - ID = string (UUID)
 */
export const ServerRepository: BaseRepository<ServerTO, ServerFormTO> = {

    async listAll(): Promise<ServerTO[]> {
        return await this.list({
            paginationModel: {page: 0, pageSize: 1000},
            sortModel: [],
            filterModel: {items: []},
        }).then(server => server.items);
    },

    validate(bot: Partial<ServerTO>): ValidationResult {
        let issues: ValidationResult['issues'] = [];

        if (!bot.name) {
            issues = [...issues, {message: 'Name is required', path: ['name']}];
        }

        return {issues};
    },

    async list({
                   paginationModel = {page: 0, pageSize: 1000},
                   sortModel = [],
                   filterModel = {items: []},
               }: {
        paginationModel?: GridPaginationModel;
        sortModel?: GridSortModel;
        filterModel?: GridFilterModel;
    }): Promise<{ items: ServerTO[]; itemCount: number }> {
        const servers = await api.listServers().then((rs) => rs.data);

        let filteredServers = [...servers];

        // Apply filters (same style as bot.ts)
        if (filterModel?.items?.length) {
            filterModel.items.forEach(({field, value, operator}) => {
                if (!field || value == null) {
                    return;
                }

                filteredServers = filteredServers.filter((bot) => {
                    const botValue = bot[field as keyof ServerTO];

                    switch (operator) {
                        case 'contains':
                            return String(botValue).toLowerCase().includes(String(value).toLowerCase());
                        case 'equals':
                            return botValue === (value as unknown as ServerTO[keyof ServerTO]);
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
            filteredServers.sort((a, b) => {
                for (const {field, sort} of sortModel) {
                    const botA = a[field as keyof ServerTO];
                    const botB = b[field as keyof ServerTO];

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
        const paginatedServers = filteredServers.slice(start, end);

        return {
            items: paginatedServers,
            itemCount: filteredServers.length,
        };
    },

    async get(id: string): Promise<ServerTO> {
        return await api.getServer(id).then((res) => res.data);
    },

    async create(data: Omit<ServerFormTO, 'id'>): Promise<ServerTO> {
        return await api.createServer({...data}).then((res) => res.data);
    },

    async update(_id: string, _data: Partial<Omit<ServerFormTO, 'id'>>): Promise<ServerTO> {
        return await api.updateServer(_id, _data).then((res) => res.data);
    },

    async delete(id: string): Promise<void> {
        await api.deleteServer(id);
    }
};