import * as React from 'react';
import type {GridColDef} from '@mui/x-data-grid';
import {useNavigate} from 'react-router';
import {BaseList, BaseListLoadParams} from '../base/BaseList';
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

export default function DownloadList(state: DownloadState) {
    const navigate = useNavigate();
    const gridState = useUrlDataGridState(10);

    const columns = React.useMemo<GridColDef<DownloadTO>[]>(
        () => [
            {field: 'name', headerName: 'Name', flex: 1, minWidth: 80, headerAlign: 'center', align: 'center'},
            // specific actions column lives here (edit/delete) if needed
        ],
        [],
    );

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


    return (
        <BaseList<DownloadTO>
            title="Downloads"
            columns={columns}
            gridState={gridState}
            load={load}
            onRowClick={(row) => navigate(`/downloads/${row.id}`)}
            actions={({refresh, isLoading}) => (
                <Stack direction="row" alignItems="center" spacing={1}>
                    <Tooltip title="Reload data" placement="right" enterDelay={1000}>
                        <div>
                            <IconButton size="small" aria-label="refresh" onClick={refresh} disabled={isLoading}>
                                <RefreshIcon/>
                            </IconButton>
                        </div>
                    </Tooltip>

                    {/* Download-specific buttons */}
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