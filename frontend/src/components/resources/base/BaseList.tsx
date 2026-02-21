import * as React from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import Stack from '@mui/material/Stack';
import Tooltip from '@mui/material/Tooltip';
import AddIcon from '@mui/icons-material/Add';
import RefreshIcon from '@mui/icons-material/Refresh';
import {
    DataGrid,
    gridClasses,
    type GridColDef,
    type GridFilterModel,
    type GridPaginationModel,
    type GridSortModel,
    GridValidRowModel,
} from '@mui/x-data-grid';
import PageContainer from '../../pagecontainer/PageContainer';

type ListResult<T> = { items: T[]; itemCount: number };

export type BaseListLoadParams = {
    paginationModel: GridPaginationModel;
    sortModel: GridSortModel;
    filterModel: GridFilterModel;
};

export type BaseListApi<T extends GridValidRowModel = GridValidRowModel> = {
    reload: () => void;

    /**
     * Merge a partial update into a row if it exists in the current page.
     * Returns true if row was found and patched; false otherwise.
     */
    patchRow: (id: string | number, patch: Partial<T>) => boolean;

    /**
     * Remove a row from the current page if present.
     * Returns true if row was found and removed; false otherwise.
     */
    removeRow: (id: string | number) => boolean;

    /**
     * Insert if missing, otherwise patch. (Only affects current page.)
     * Returns 'inserted' | 'patched'.
     */
    upsertRow: (row: T) => 'inserted' | 'patched';
};

type DefaultActionsContext = {
    refresh: () => void;
    isLoading: boolean;
};

type Props<T extends GridValidRowModel> = {
    title: string;
    breadcrumbs?: { title: string }[];
    columns: GridColDef<T>[];
    getRowId?: (row: T) => string | number;

    gridState: {
        paginationModel: GridPaginationModel;
        sortModel: GridSortModel;
        filterModel: GridFilterModel;
        onPaginationModelChange: (m: GridPaginationModel) => void;
        onSortModelChange: (m: GridSortModel) => void;
        onFilterModelChange: (m: GridFilterModel) => void;
    };

    load: (params: BaseListLoadParams) => Promise<ListResult<T>>;

    onRowClick?: (row: T) => void;

    onCreateClick?: () => void;
    createLabel?: string;
    pageSizeOptions?: number[];

    actions?: (ctx: DefaultActionsContext) => React.ReactNode;

    apiRef?: React.MutableRefObject<BaseListApi<T> | null>;
};

const INITIAL_PAGE_SIZE = 10;

export function BaseList<T extends GridValidRowModel>(props: Props<T>) {
    const {
        title,
        breadcrumbs,
        columns,
        getRowId,
        gridState,
        load,
        onRowClick,
        onCreateClick,
        createLabel = 'Create',
        pageSizeOptions = [5, INITIAL_PAGE_SIZE, 25],
        actions,
        apiRef,
    } = props;

    const [rowsState, setRowsState] = React.useState<{ rows: T[]; rowCount: number }>({
        rows: [],
        rowCount: 0,
    });

    const [isLoading, setIsLoading] = React.useState(true);
    const [error, setError] = React.useState<Error | null>(null);

    const getId = React.useCallback(
        (row: T) => (getRowId ? getRowId(row) : (row as any).id) as string | number,
        [getRowId],
    );

    const loadData = React.useCallback(async () => {
        setError(null);
        setIsLoading(true);
        try {
            const res = await load({
                paginationModel: gridState.paginationModel,
                sortModel: gridState.sortModel,
                filterModel: gridState.filterModel,
            });
            setRowsState({rows: res.items, rowCount: res.itemCount});
        } catch (e) {
            setError(e as Error);
        } finally {
            setIsLoading(false);
        }
    }, [load, gridState.paginationModel, gridState.sortModel, gridState.filterModel]);

    React.useEffect(() => {
        loadData();
    }, [loadData]);

    const patchRow = React.useCallback(
        (id: string | number, patch: Partial<T>) => {
            let didPatch = false;

            setRowsState(prev => {
                const idx = prev.rows.findIndex(r => getId(r) === id);
                if (idx < 0) return prev;

                const nextRows = prev.rows.slice();
                nextRows[idx] = {...nextRows[idx], ...patch};

                didPatch = true;
                return {...prev, rows: nextRows};
            });

            return didPatch;
        },
        [getId],
    );

    const removeRow = React.useCallback(
        (id: string | number) => {
            let didRemove = false;

            setRowsState(prev => {
                const idx = prev.rows.findIndex(r => getId(r) === id);
                if (idx < 0) return prev;

                const nextRows = prev.rows.slice();
                nextRows.splice(idx, 1);

                didRemove = true;
                return {rows: nextRows, rowCount: Math.max(0, prev.rowCount - 1)};
            });

            return didRemove;
        },
        [getId],
    );

    const upsertRow = React.useCallback(
        (row: T) => {
            const id = getId(row);
            let result: 'inserted' | 'patched' = 'inserted';

            setRowsState(prev => {
                const idx = prev.rows.findIndex(r => getId(r) === id);

                // patch existing
                if (idx >= 0) {
                    const nextRows = prev.rows.slice();
                    nextRows[idx] = {...nextRows[idx], ...row};
                    result = 'patched';
                    return {...prev, rows: nextRows};
                }

                // insert into current page (NOTE: may violate server sort; use with care)
                const nextRows = [row, ...prev.rows];
                return {rows: nextRows, rowCount: prev.rowCount + 1};
            });

            return result;
        },
        [getId],
    );

    React.useEffect(() => {
        if (apiRef) {
            apiRef.current = {reload: loadData, patchRow, removeRow, upsertRow};
            return () => {
                apiRef.current = null;
            };
        }
        return;
    }, [apiRef, loadData, patchRow, removeRow, upsertRow]);

    const handleRefresh = React.useCallback(() => {
        if (!isLoading) loadData();
    }, [isLoading, loadData]);

    const defaultActions = React.useMemo(
        () => (
            <Stack direction="row" alignItems="center" spacing={1}>
                <Tooltip title="Reload data" placement="right" enterDelay={1000}>
                    <div>
                        <IconButton size="small" aria-label="refresh" onClick={handleRefresh}>
                            <RefreshIcon/>
                        </IconButton>
                    </div>
                </Tooltip>

                {onCreateClick && (
                    <Button variant="contained" onClick={onCreateClick} startIcon={<AddIcon/>}>
                        {createLabel}
                    </Button>
                )}
            </Stack>
        ),
        [createLabel, handleRefresh, onCreateClick],
    );

    return (
        <PageContainer
            title={title}
            breadcrumbs={breadcrumbs ?? [{title}]}
            actions={actions ? actions({refresh: handleRefresh, isLoading}) : defaultActions}
        >
            <Box sx={{flex: 1, width: '100%'}}>
                {error ? (
                    <Box sx={{flexGrow: 1}}>
                        <Alert severity="error">{error.message}</Alert>
                    </Box>
                ) : (
                    <DataGrid
                        rows={rowsState.rows}
                        rowCount={rowsState.rowCount}
                        columns={columns}
                        getRowId={getRowId}
                        pagination
                        sortingMode="server"
                        filterMode="server"
                        paginationMode="server"
                        paginationModel={gridState.paginationModel}
                        onPaginationModelChange={gridState.onPaginationModelChange}
                        sortModel={gridState.sortModel}
                        onSortModelChange={gridState.onSortModelChange}
                        filterModel={gridState.filterModel}
                        onFilterModelChange={gridState.onFilterModelChange}
                        disableRowSelectionOnClick
                        onRowClick={onRowClick ? (p) => onRowClick(p.row as T) : undefined}
                        loading={isLoading}
                        showToolbar
                        pageSizeOptions={pageSizeOptions}
                        sx={{
                            [`& .${gridClasses.columnHeader}, & .${gridClasses.cell}`]: {outline: 'transparent'},
                            [`& .${gridClasses.columnHeader}:focus-within, & .${gridClasses.cell}:focus-within`]: {
                                outline: 'none',
                            },
                            [`& .${gridClasses.row}:hover`]: {cursor: onRowClick ? 'pointer' : 'default'},
                        }}
                        slotProps={{
                            loadingOverlay: {variant: 'circular-progress', noRowsVariant: 'circular-progress'},
                            baseIconButton: {size: 'small'},
                        }}
                    />
                )}
            </Box>
        </PageContainer>
    );
}