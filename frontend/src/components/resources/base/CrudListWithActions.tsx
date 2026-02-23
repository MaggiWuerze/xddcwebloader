import * as React from 'react';
import type { GridColDef, GridValidRowModel } from '@mui/x-data-grid';
import { GridActionsCellItem } from '@mui/x-data-grid';
import { useNavigate } from 'react-router';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

import { BaseList, type BaseListApi } from './BaseList';
import { useUrlDataGridState } from './UrlDataGridState';
import { useDialogs } from '../../../hooks/useDialogs/useDialogs';
import useNotifications from '../../../hooks/useNotifications/useNotifications';

// UUIDs in the frontend are represented as strings
type Uuid = string;

type CrudRepository<T> = {
    list: (params: any) => Promise<{ items: T[]; itemCount: number }>;
    delete?: (id: Uuid) => Promise<void>;
};

type CrudListProps<T extends GridValidRowModel & { id: Uuid }> = {
    /** singular resource name: "bot" | "server" | "channel" | "download" */
    resource: string;

    title: string;
    columns: GridColDef<T>[];
    repository: CrudRepository<T>;

    enableEdit?: boolean;
    enableDelete?: boolean;

    /** Used in the confirm dialog: "Do you wish to delete X?" */
    getDeleteLabel?: (row: T) => string;
};

export function CrudList<T extends GridValidRowModel & { id: Uuid }>(props: CrudListProps<T>) {
    const {
        resource,
        title,
        columns,
        repository,
        enableEdit = true,
        enableDelete = true,
        getDeleteLabel = (row) => String((row as any).name ?? row.id),
    } = props;

    const basePath = `/${resource}`;

    const navigate = useNavigate();
    const gridState = useUrlDataGridState(10);
    const dialogs = useDialogs();
    const notifications = useNotifications();
    const apiRef = React.useRef<BaseListApi | null>(null);

    const actionsColumn = React.useMemo<GridColDef<T> | null>(() => {
        if (!enableEdit && !enableDelete) return null;

        return {
            field: 'actions',
            type: 'actions',
            headerName: '',
            flex: 1,
            align: 'right',
            getActions: ({ row }) => {
                const items: React.ReactElement[] = [];

                if (enableEdit) {
                    items.push(
                        <GridActionsCellItem
                            key="edit"
                            icon={<EditIcon />}
                            label="Edit"
                            onClick={() => navigate(`${basePath}/${row.id}/edit`)}
                        />,
                    );
                }

                if (enableDelete && repository.delete) {
                    items.push(
                        <GridActionsCellItem
                            key="delete"
                            icon={<DeleteIcon />}
                            label="Delete"
                            onClick={async () => {
                                const confirmed = await dialogs.confirm(
                                    `Do you wish to delete ${getDeleteLabel(row)}?`,
                                    {
                                        title: 'Delete?',
                                        severity: 'error',
                                        okText: 'Delete',
                                        cancelText: 'Cancel',
                                    },
                                );

                                if (!confirmed) return;

                                try {
                                    await repository.delete!(row.id);
                                    notifications.show('Deleted successfully.', {
                                        severity: 'success',
                                        autoHideDuration: 3000,
                                    });
                                    apiRef.current?.reload();
                                } catch (e) {
                                    notifications.show(`Failed to delete. Reason: ${(e as Error).message}`, {
                                        severity: 'error',
                                        autoHideDuration: 3000,
                                    });
                                }
                            }}
                        />,
                    );
                }

                return items;
            },
        };
    }, [basePath, dialogs, enableDelete, enableEdit, getDeleteLabel, navigate, notifications, repository.delete]);

    const mergedColumns = React.useMemo(
        () => (actionsColumn ? [...columns, actionsColumn] : columns),
        [columns, actionsColumn],
    );

    return (
        <BaseList<T>
            title={title}
            columns={mergedColumns}
            gridState={gridState}
            load={(params) => repository.list(params)}
            onRowClick={(row) => navigate(`${basePath}/${row.id}`)}
            onCreateClick={() => navigate(`${basePath}/new`)}
            apiRef={apiRef}
        />
    );
}
