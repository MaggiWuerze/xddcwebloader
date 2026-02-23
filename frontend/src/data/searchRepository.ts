import type {GridFilterModel, GridPaginationModel, GridSortModel} from '@mui/x-data-grid';
import {DownloadTO, SearchControllerApi, SearchEngineTO, SearchResultItem} from '../api/rest';

const api = new SearchControllerApi();


/**
 * Bot repository:
 * - T = BotTO (read model)
 * - F = TargetBotForm (write model)
 * - ID = string (UUID)
 */
export const SearchRepository = {

    async createFromSearchResult(searchResultItem: SearchResultItem): Promise<DownloadTO> {
        return await api.startDownloadFromSearchResult(searchResultItem).then((rs) => rs.data);
    },

    async listAll(): Promise<SearchEngineTO[]> {
        return await this.list({
            paginationModel: {page: 0, pageSize: 1000},
            sortModel: [],
            filterModel: {items: []},
        }).then(server => server.items);
    },

    async list({
                   paginationModel,
                   filterModel,
                   sortModel,
               }: {
        paginationModel: GridPaginationModel;
        sortModel: GridSortModel;
        filterModel: GridFilterModel;
    }): Promise<{ items: SearchEngineTO[]; itemCount: number }> {
        let searchProviderData = await api.listSearchProviders().then((rs) => rs.data);

        let filteredSearchProviders = [...searchProviderData];

        // Apply filters (same style as bot.ts)
        if (filterModel?.items?.length) {
            filterModel.items.forEach(({field, value, operator}) => {
                if (!field || value == null) {
                    return;
                }

                filteredSearchProviders = filteredSearchProviders.filter((bot) => {
                    const botValue = bot[field as keyof SearchEngineTO];

                    switch (operator) {
                        case 'contains':
                            return String(botValue).toLowerCase().includes(String(value).toLowerCase());
                        case 'equals':
                            return botValue === (value as unknown as SearchEngineTO[keyof SearchEngineTO]);
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
            filteredSearchProviders.sort((a, b) => {
                for (const {field, sort} of sortModel) {
                    const botA = a[field as keyof SearchEngineTO];
                    const botB = b[field as keyof SearchEngineTO];

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
        const paginatedBots = filteredSearchProviders.slice(start, end);

        return {
            items: paginatedBots,
            itemCount: filteredSearchProviders.length,
        };
    },

    async search({
                     paginationModel,
                     filterModel,
                     sortModel,
                 }: {
        paginationModel: GridPaginationModel;
        sortModel: GridSortModel;
        filterModel: GridFilterModel;
    }, query: string, provider: string): Promise<{ items: SearchResultItem[]; itemCount: number }> {
        let searchData = await api.searchWithProvider(provider,query).then((rs) => rs.data);

        let filteredSearchResults = [...searchData];

        // Apply filters (same style as bot.ts)
        if (filterModel?.items?.length) {
            filterModel.items.forEach(({field, value, operator}) => {
                if (!field || value == null) {
                    return;
                }

                filteredSearchResults = filteredSearchResults.filter((bot) => {
                    const botValue = bot[field as keyof SearchResultItem];

                    switch (operator) {
                        case 'contains':
                            return String(botValue).toLowerCase().includes(String(value).toLowerCase());
                        case 'equals':
                            return botValue === (value as unknown as SearchEngineTO[keyof SearchEngineTO]);
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
            filteredSearchResults.sort((a, b) => {
                for (const {field, sort} of sortModel) {
                    const botA = a[field as keyof SearchResultItem];
                    const botB = b[field as keyof SearchResultItem];

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
        const paginatedSearchResults = searchData.slice(start, end);

        return {
            items: paginatedSearchResults,
            itemCount: searchData.length,
        };
    }
};