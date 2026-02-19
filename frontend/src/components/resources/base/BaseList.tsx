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
} from '@mui/x-data-grid';
import PageContainer from '../../pagecontainer/PageContainer';

type ListResult<T> = { items: T[]; itemCount: number };

export type BaseListLoadParams = {
    paginationModel: GridPaginationModel;
    sortModel: GridSortModel;
    filterModel: GridFilterModel;
};

type Props<T> = {
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
};

const INITIAL_PAGE_SIZE = 10;

export function BaseList<T>(props: Props<T>) {
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
    } = props;

    const [rowsState, setRowsState] = React.useState<{ rows: T[]; rowCount: number }>({
        rows: [],
        rowCount: 0,
    });

    const [isLoading, setIsLoading] = React.useState(true);
    const [error, setError] = React.useState<Error | null>(null);

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

    const handleRefresh = React.useCallback(() => {
        if (!isLoading) loadData();
    }, [isLoading, loadData]);

    return (
        <PageContainer
            title={title}
            breadcrumbs={breadcrumbs ?? [{title}]}
            actions={
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
            }
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