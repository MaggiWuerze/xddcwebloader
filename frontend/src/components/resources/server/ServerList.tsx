import * as React from 'react';
import type {GridColDef} from '@mui/x-data-grid';
import {useNavigate} from 'react-router';
import {BaseList} from '../base/BaseList';
import {useUrlDataGridState} from '../base/UrlDataGridState';
import type {ServerTO} from '../../../api/rest';
import {ServerRepository} from "../../../data/serverRepository";

export default function ServerList() {
    const navigate = useNavigate();
    const gridState = useUrlDataGridState(10);

    const columns = React.useMemo<GridColDef<ServerTO>[]>(
        () => [
            {field: 'name', headerName: 'Name', flex: 1, minWidth: 80, headerAlign: 'center', align: 'center'},
            // specific actions column lives here (edit/delete) if needed
        ],
        [],
    );

    return (
        <BaseList<ServerTO>
            title="Servers"
            columns={columns}
            gridState={gridState}
            load={(params) => ServerRepository.list(params)}
            onRowClick={(row) => navigate(`/server/${row.id}`)}
            onCreateClick={() => navigate('/server/new')}
        />
    );
}