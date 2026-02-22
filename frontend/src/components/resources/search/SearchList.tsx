import * as React from 'react';
import {GridActionsCellItem, GridColDef} from '@mui/x-data-grid';
import {useNavigate} from 'react-router';
import {useDialogs} from '../../../hooks/useDialogs/useDialogs';
import useNotifications from '../../../hooks/useNotifications/useNotifications';
import {BaseList, BaseListApi, BaseListLoadParams} from '../base/BaseList';
import {useUrlDataGridState} from '../base/UrlDataGridState';
import type {SearchResultItem} from '../../../api/rest';
import Stack from "@mui/material/Stack";
import {SearchRepository} from "../../../data/searchRepository";
import {SearchProviderSelect} from "./SearchProviderSelect";
import DownloadForOfflineIcon from '@mui/icons-material/DownloadForOffline';
import Paper from "@mui/material/Paper";
import {InputBase} from "@mui/material";

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
    const debouncedQuery = useDebouncedValue(query, 500);

    function loadData(params: BaseListLoadParams) {

        if (!debouncedQuery || !provider) {
            let emptyResult: SearchResultItem[] = [];
            return Promise.resolve({items: emptyResult, itemCount: 0});
        }

        return SearchRepository.search(params, debouncedQuery, provider);
    }

    const handleRowAdd = React.useCallback(
        (searchResult: SearchResultItem) => async () => {

            try {
                await SearchRepository.createFromSearchResult(searchResult);
                notifications.show('Download added successfully.', {
                    severity: 'success',
                    autoHideDuration: 3000,
                });
            } catch (deleteError) {
                notifications.show(
                    `Failed to add Download. Reason:' ${(deleteError as Error).message}`,
                    {
                        severity: 'error',
                        autoHideDuration: 3000,
                    },
                );
            }
        },
        [],
    );

    function useDebouncedValue<T>(value: T, delayMs: number): T {
        const [debounced, setDebounced] = React.useState(value);

        React.useEffect(() => {
            const id = window.setTimeout(() => setDebounced(value), delayMs);
            return () => window.clearTimeout(id);
        }, [value, delayMs]);

        return debounced;
    }

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
                        icon={<DownloadForOfflineIcon/>}
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
            getRowId={(row) => {
                return row.channel + row.fileRefId
            }}
            actions={() => (

                <Stack direction="row" alignItems="center" spacing={1} width={"100%"}>
                    <Paper
                        component="form"
                        sx={{p: '2px 4px', display: 'flex', alignItems: 'center', width: 400}}
                    >
                        <InputBase
                            sx={{ml: 1, flex: 1}}
                            placeholder="Search"
                            inputProps={{'aria-label': 'search'}}
                            onChange={(e) => setQuery(e.target.value)}
                        />
                        <SearchProviderSelect
                            searchSelectValues={SearchRepository.listAll()}
                            onChange={(serverId) => setProvider(serverId)}>

                        </SearchProviderSelect>
                    </Paper>
                </Stack>
            )}
        />
    );
}