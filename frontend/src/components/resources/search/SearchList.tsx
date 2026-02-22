import * as React from 'react';
import {GridActionsCellItem, GridColDef} from '@mui/x-data-grid';
import {useNavigate} from 'react-router';
import {useDialogs} from '../../../hooks/useDialogs/useDialogs';
import useNotifications from '../../../hooks/useNotifications/useNotifications';
import {BaseList, BaseListApi, BaseListLoadParams} from '../base/BaseList';
import {useUrlDataGridState} from '../base/UrlDataGridState';
import type {SearchResultItem} from '../../../api/rest';
import Stack from "@mui/material/Stack";
import Tooltip from "@mui/material/Tooltip";
import IconButton from "@mui/material/IconButton";
import RefreshIcon from "@mui/icons-material/Refresh";
import Button from "@mui/material/Button";
import {SearchRepository} from "../../../data/searchRepository";
import SearchIcon from '@mui/icons-material/SearchOffOutlined';
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import {SearchProviderSelect} from "./SearchProviderSelect";
import CancelledIcon from "@mui/icons-material/Cancel";

export default function SearchList() {
    const navigate = useNavigate();
    const gridState = useUrlDataGridState(10);

    const dialogs = useDialogs();
    const notifications = useNotifications();

    const [query, setQuery] = React.useState("");
    const [provider, setProvider] = React.useState("");
    const [isLoading, setIsLoading] = React.useState(true);
    const [error, setError] = React.useState<Error | null>(null);

    const apiRef = React.useRef<BaseListApi<SearchResultItem> | null>(null);


    function loadData(params: BaseListLoadParams) {

        if (!query || !provider) {
            let emptyResult: SearchResultItem[] = [];
            return Promise.resolve({items: emptyResult, itemCount: 0});
        }

        return SearchRepository.search(params, query, "");
    }

    const handleRowAdd = React.useCallback(
        (searchResult: SearchResultItem) => () => {
            SearchRepository.createFromSearchResult(searchResult);
        },
        [],
    );

    const columns = React.useMemo<GridColDef<SearchResultItem>[]>(
        () => [
            {field: 'fileName', headerName: 'File name', flex: 1, minWidth: 80, headerAlign: 'center', align: 'center'},
            {field: 'fileRefId', headerName: 'Pack Nr', flex: 1, minWidth: 80, headerAlign: 'center', align: 'center'},
            {field: 'fileSize', headerName: 'Filesize', flex: 1, minWidth: 80, headerAlign: 'center', align: 'center'},
            {
                field: 'server',
                headerName: 'Server Name',
                flex: 1,
                minWidth: 80,
                headerAlign: 'center',
                align: 'center'
            },
            {
                field: 'channel',
                headerName: 'Channel Name',
                flex: 1,
                minWidth: 80,
                headerAlign: 'center',
                align: 'center'
            },
            {
                field: 'bot',
                headerName: 'Bot Name',
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
                        onClick={handleRowAdd(row)}
                    />
                ],
            },

        ],
        [],
    );

    return (
        <BaseList<SearchResultItem>
            title="Search"
            columns={columns}
            gridState={gridState}
            load={loadData}
            apiRef={apiRef}
            actions={({refresh, isLoading}) => (

                <Stack direction="row" alignItems="center" spacing={1}>
                    <Box sx={{display: 'flex', alignItems: 'center'}}>
                        <SearchProviderSelect
                            serverSelectValues={SearchRepository.listAll()}
                            onChange={(serverId) => setProvider(serverId)}>

                        </SearchProviderSelect>
                    </Box>
                    <Box sx={{display: 'flex', alignItems: 'center', width: '400px'}}>
                        <SearchIcon sx={{color: 'action.active', mr: 1, my: 0.5}}/>
                        <TextField label="Search" variant="standard" fullWidth size="small" sx={{ml: 1}}
                                   onChange={(e) => setQuery(e.target.value)}
                        />
                    </Box>
                    <Tooltip title="Search again" placement="right" enterDelay={1000}>
                        <div>
                            <IconButton size="small" aria-label="refresh" onClick={refresh} disabled={isLoading}>
                                <RefreshIcon/>
                            </IconButton>
                        </div>
                    </Tooltip>

                    <Button variant="outlined" onClick={() => {/* e.g. cancelAll */
                    }}>
                        Search
                    </Button>
                </Stack>
            )}
        />
    );
}