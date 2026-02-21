import * as React from 'react';
import {GridActionsCellItem, GridColDef, GridEventListener, GridRenderCellParams} from '@mui/x-data-grid';
import {useNavigate} from 'react-router';
import {useDialogs} from '../../../hooks/useDialogs/useDialogs';
import useNotifications from '../../../hooks/useNotifications/useNotifications';
import {BaseList, BaseListApi, BaseListLoadParams} from '../base/BaseList';
import {useUrlDataGridState} from '../base/UrlDataGridState';
import type {DownloadTO} from '../../../api/rest';
import Stack from "@mui/material/Stack";
import Tooltip from "@mui/material/Tooltip";
import IconButton from "@mui/material/IconButton";
import RefreshIcon from "@mui/icons-material/Refresh";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import {DownloadState} from "../../../data/DownloadState";
import {DownloadRepository} from "../../../data/downloadRepository";
import Box from "@mui/material/Box";
import {styled} from '@mui/material/styles';
import {linearProgressClasses} from "@mui/material";
import LinearProgress, {LinearProgressProps} from '@mui/material/LinearProgress';
import Typography from "@mui/material/Typography";
import CancelledIcon from "@mui/icons-material/Cancel";
import {downloadUpdatesClient} from "../../../api/stomp/DownloadUpdatesClient";


const BorderLinearProgress = styled(LinearProgress)(({theme}) => ({
    height: 15,
    borderRadius: 5,
    [`&.${linearProgressClasses.colorPrimary}`]: {
        backgroundColor: theme.palette.grey[200],
        ...theme.applyStyles('dark', {
            backgroundColor: theme.palette.grey[800],
        }),
    },
    [`& .${linearProgressClasses.bar}`]: {
        borderRadius: 5,
        backgroundColor: '#1a90ff',
        ...theme.applyStyles('dark', {
            backgroundColor: '#308fe8',
        }),
    },
}));

function LinearProgressWithLabel(props: LinearProgressProps & { value: number }) {
    return (
        <Box sx={{position: 'relative', width: '100%'}}>
            <BorderLinearProgress variant="determinate" value={props.value ?? 0}/>

            <Box
                sx={{
                    position: 'absolute',
                    inset: 0,                 // top:0 right:0 bottom:0 left:0
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    pointerEvents: 'none',    // clicks go to the row/cell, not the label
                }}
            >
                <Typography
                    variant="body2"
                    sx={{
                        color: 'text.primary',
                        fontWeight: 400,
                        lineHeight: 0.8,
                        textShadow: '0 1px 2px rgba(0,0,0,0.35)', // optional: readability
                    }}
                >
                    {`${Math.round(props.value ?? 0)}%`}
                </Typography>
            </Box>
        </Box>
    );
}

export default function DownloadList(state: DownloadState) {
    const navigate = useNavigate();
    const gridState = useUrlDataGridState(10);

    const dialogs = useDialogs();
    const notifications = useNotifications();

    const [isLoading, setIsLoading] = React.useState(true);
    const [error, setError] = React.useState<Error | null>(null);

    const apiRef = React.useRef<BaseListApi<DownloadTO> | null>(null);

    React.useEffect(() => {
        const sub = downloadUpdatesClient.subscribeDownloadEvents('download-list', (evt) => {
            const id = evt.payload?.id;
            if (!id) return;

            if (evt.type === 'update') {
                const patched = apiRef.current?.patchRow(id, evt.payload) ?? false;
                if (!patched) {
                    // Update for a row not currently on this page; ignore or reload.
                    // apiRef.current?.reload();
                }
                return;
            }

            if (evt.type === 'delete') {
                apiRef.current?.removeRow(id);
                return;
            }

            if (evt.type === 'new') {
                // In server-mode lists, safest is reload so ordering/filtering stays correct.
                apiRef.current?.reload();

                // If you prefer immediate insert for UX, you can do:
                // apiRef.current?.upsertRow(evt.payload as DownloadTO);
            }
        });

        return () => sub.unsubscribe();
    }, []);

    type DownloadLoadFn = (params: BaseListLoadParams) => Promise<{ items: DownloadTO[]; itemCount: number }>;

    function determineLoadFunction(state: DownloadState): DownloadLoadFn {
        switch (state) {
            case DownloadState.all:
                return (params) => DownloadRepository.list(params);

            case DownloadState.active:
                // If you have a dedicated endpoint, call it here; otherwise reuse list() for now.
                return (params) => DownloadRepository.list(params);

            case DownloadState.finished:
                return (params) => DownloadRepository.list(params);

            case DownloadState.cancelled:
                return (params) => DownloadRepository.list(params);

            default:
                return (params) => DownloadRepository.list(params);
        }
    }

    const load = React.useMemo(() => determineLoadFunction(state), [state]);

    const handleRowClick = React.useCallback<GridEventListener<'rowClick'>>(
        ({row}) => {
            navigate(`/download/${row.id}`);
        },
        [navigate],
    );

    const handleRowEdit = React.useCallback(
        (employee: DownloadTO) => () => {
            navigate(`/download/${employee.id}/edit`);
        },
        [navigate],
    );

    const handleRowCancel = React.useCallback(
        (download: DownloadTO) => async () => {
            const confirmed = await dialogs.confirm(
                `Do you wish to cancel ${download.filename}?`,
                {
                    title: `Cancel Download?`,
                    severity: 'error',
                    okText: 'Confirm',
                    cancelText: 'Back',
                },
            );

            if (confirmed) {
                setIsLoading(true);
                try {

                    await DownloadRepository.cancel(download.id);
                    notifications.show('Download cancelled successfully.', {
                        severity: 'success',
                        autoHideDuration: 3000,
                    });
                    await load({
                        paginationModel: {page: 0, pageSize: 1000},
                        sortModel: [],
                        filterModel: {items: []},
                    });
                } catch (deleteError) {
                    notifications.show(
                        `Failed to cancel Download. Reason:' ${(deleteError as Error).message}`,
                        {
                            severity: 'error',
                            autoHideDuration: 3000,
                        },
                    );
                }
                setIsLoading(false);
            }
        },
        [dialogs, notifications, load],
    );

    const columns = React.useMemo<GridColDef<DownloadTO>[]>(
        () => [
            {field: 'filename', headerName: 'Filename', flex: 1, minWidth: 80, headerAlign: 'center', align: 'center'},
            {field: 'filesize', headerName: 'Filesize', flex: 1, minWidth: 80, headerAlign: 'center', align: 'center'},
            {
                field: 'averageSpeed',
                headerName: 'Average Speed',
                flex: 1,
                minWidth: 80,
                headerAlign: 'center',
                align: 'center'
            },
            {
                field: 'progress',
                headerName: 'Progress',
                flex: 1,
                maxWidth: 200,
                headerAlign: 'center',
                align: 'center',
                renderCell: (params: GridRenderCellParams<DownloadTO>) => {
                    const progress = params.row.progress ?? 50;
                    return (
                        <Box
                            sx={{
                                height: '100%',
                                width: '100%',
                                display: 'flex',
                                alignItems: 'center',     // vertical centering
                            }}
                        >
                            <Box sx={{width: '100%'}}>
                                <LinearProgressWithLabel variant="determinate" value={progress}/>
                            </Box>
                        </Box>
                    );
                },
            },
            {
                field: 'status',
                headerName: 'Status',
                description: 'The current status of the download',
                flex: 1,
                minWidth: 80,
                headerAlign: 'center',
                align: 'center',
                renderCell: (params: GridRenderCellParams<DownloadTO>) => {
                    return (
                        <Box
                            sx={{
                                height: '100%',
                                width: '100%',
                                display: 'flex',
                                alignItems: 'center',     // vertical centering
                            }}
                        >
                            <Box sx={{width: '100%'}}>
                                <Typography title={params.row.statusMessage} variant="body2"
                                            color={params.value === 'Error' ? 'error' : 'white'}>
                                    {params.value}
                                </Typography>
                            </Box>
                        </Box>

                    );
                }
            },
            {
                field: 'timeRemaining',
                headerName: 'Time Remaining',
                flex: 1,
                minWidth: 80,
                headerAlign: 'center',
                align: 'center'
            },
            {
                field: 'actions',
                type: 'actions',
                flex: 1,
                align: 'right',
                getActions: ({row}) => [
                    <GridActionsCellItem
                        key="edit-item"
                        icon={<CancelledIcon/>}
                        label="Cancel"
                        onClick={handleRowCancel(row)}
                    />
                ],
            },

        ],
        [],
    );

    return (
        <BaseList<DownloadTO>
            title="Downloads"
            columns={columns}
            gridState={gridState}
            load={load}
            onRowClick={(row) => navigate(`/downloads/${row.id}`)}
            apiRef={apiRef}
            actions={({refresh, isLoading}) => (
                <Stack direction="row" alignItems="center" spacing={1}>
                    <Tooltip title="Reload data" placement="right" enterDelay={1000}>
                        <div>
                            <IconButton size="small" aria-label="refresh" onClick={refresh} disabled={isLoading}>
                                <RefreshIcon/>
                            </IconButton>
                        </div>
                    </Tooltip>

                    <Button variant="outlined" onClick={() => {/* e.g. cancelAll */
                    }}>
                        Cancel all
                    </Button>
                    <Button variant="contained" onClick={() => navigate('/downloads/new')} startIcon={<AddIcon/>}>
                        Create
                    </Button>
                </Stack>
            )}
        />
    );
}