import * as React from 'react';
import {GridColDef} from '@mui/x-data-grid';
import {BotRepository} from '../../../data/botRepository';
import type {BotTO} from '../../../api/rest';
import {CrudList} from "../base/CrudListWithActions";

export default function BotList() {

    const columns = React.useMemo<GridColDef<BotTO>[]>(
        () => [
            {field: 'name', headerName: 'Name', flex: 1, minWidth: 80, headerAlign: 'center', align: 'center'},
        ],
        [],
    );

    return <CrudList<BotTO> resource="bot" title="Bots" columns={columns} repository={BotRepository}/>;
}