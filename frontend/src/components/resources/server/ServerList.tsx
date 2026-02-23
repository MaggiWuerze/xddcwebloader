import * as React from 'react';
import type {GridColDef} from '@mui/x-data-grid';
import type {ServerTO} from '../../../api/rest';
import {ServerRepository} from "../../../data/serverRepository";
import {CrudList} from "../base/CrudListWithActions";

export default function ServerList() {

    const columns = React.useMemo<GridColDef<ServerTO>[]>(
        () => [{field: 'name', headerName: 'Name', flex: 1, minWidth: 140, headerAlign: 'center', align: 'center'}],
        [],
    );

    return <CrudList<ServerTO> resource="server" title="Servers" columns={columns} repository={ServerRepository}/>;
}