import * as React from 'react';
import {GridActionsCellItem, GridColDef} from '@mui/x-data-grid';
import {useNavigate} from 'react-router';
import {BaseList} from '../base/BaseList';
import {useUrlDataGridState} from '../base/UrlDataGridState';
import {BotRepository} from '../../../data/botRepository';
import type {BotTO} from '../../../api/rest';
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import {useDialogs} from '../../../hooks/useDialogs/useDialogs';
import useNotifications from '../../../hooks/useNotifications/useNotifications';

export default function BotList() {
    const navigate = useNavigate();
    const gridState = useUrlDataGridState(10);
    const dialogs = useDialogs();
    const notifications = useNotifications();
    const [isLoading, setIsLoading] = React.useState(true);

    const handleRowEdit = React.useCallback(
        (employee: BotTO) => () => {
            navigate(`/bot/${employee.id}/edit`);
        },
        [navigate],
    );

    const handleRowDelete = React.useCallback(
        (bot: BotTO) => async () => {
            const confirmed = await dialogs.confirm(
                `Do you wish to delete ${bot.name}?`,
                {
                    title: `Delete bot?`,
                    severity: 'error',
                    okText: 'Delete',
                    cancelText: 'Cancel',
                },
            );

            if (confirmed) {
                setIsLoading(true);
                try {
                    await BotRepository.delete(bot.id);

                    notifications.show('Employee deleted successfully.', {
                        severity: 'success',
                        autoHideDuration: 3000,
                    });
                    navigate('/bot');
                } catch (deleteError) {
                    notifications.show(
                        `Failed to delete employee. Reason:' ${(deleteError as Error).message}`,
                        {
                            severity: 'error',
                            autoHideDuration: 3000,
                        },
                    );
                }
                setIsLoading(false);
            }
        },
        [dialogs, notifications],
    );

    const columns = React.useMemo<GridColDef<BotTO>[]>(
        () => [
            {field: 'name', headerName: 'Name', flex: 1, minWidth: 80, headerAlign: 'center', align: 'center'},
            {
                field: 'actions',
                type: 'actions',
                flex: 1,
                align: 'right',
                getActions: ({row}) => [
                    <GridActionsCellItem
                        key="edit-item"
                        icon={<EditIcon/>}
                        label="Edit"
                        onClick={handleRowEdit(row)}
                    />,
                    <GridActionsCellItem
                        key="delete-item"
                        icon={<DeleteIcon/>}
                        label="Delete"
                        onClick={handleRowDelete(row)}
                    />,
                ],
            },
        ],
        [],
    );

    return (
        <BaseList<BotTO>
            title="Bots"
            columns={columns}
            gridState={gridState}
            load={(params) => BotRepository.list(params)}
            onRowClick={(row) => navigate(`/bot/${row.id}`)}
            onCreateClick={() => navigate('/bot/new')}
        />
    );
}