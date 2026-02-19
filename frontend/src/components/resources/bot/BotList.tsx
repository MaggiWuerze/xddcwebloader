import * as React from 'react';
import type {GridColDef} from '@mui/x-data-grid';
import {useNavigate} from 'react-router';
import {BaseList} from '../base/BaseList';
import {useUrlDataGridState} from '../base/UrlDataGridState';
import {BotRepository} from '../../../data/botRepository';
import type {BotTO} from '../../../api/rest';

export default function BotList() {
    const navigate = useNavigate();
    const gridState = useUrlDataGridState(10);

    const columns = React.useMemo<GridColDef<BotTO>[]>(
        () => [
            {field: 'name', headerName: 'Name', flex: 1, minWidth: 80, headerAlign: 'center', align: 'center'},
            // bot-specific actions column lives here (edit/delete) if needed
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