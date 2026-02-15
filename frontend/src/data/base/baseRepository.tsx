import type {GridFilterModel, GridPaginationModel, GridSortModel} from '@mui/x-data-grid';

type ValidationResult<T> = { issues: { message: string; path: (keyof T)[] }[] };

export interface BaseRepository<T, F, ID = string> {
    list(params: {
        paginationModel: GridPaginationModel;
        sortModel: GridSortModel;
        filterModel: GridFilterModel;
    }): Promise<{ items: T[]; itemCount: number }>;

    get(id: ID): Promise<T>;

    create(data: Omit<F, 'id'>): Promise<T>;

    update(id: ID, data: Partial<Omit<F, 'id'>>): Promise<T>;

    delete(id: ID): Promise<void>;

    validate(entity: Partial<T>): ValidationResult<T>;
}