import * as React from 'react';
import type {GridColDef} from '@mui/x-data-grid';
import {useNavigate} from 'react-router';
import {BaseList} from '../base/BaseList';
import {useUrlDataGridState} from '../base/UrlDataGridState';
import {ChannelRepository} from '../../../data/channelRepository';
import type {ChannelTO} from '../../../api/rest';

export default function ChannelList() {
    const navigate = useNavigate();
    const gridState = useUrlDataGridState(10);

    const columns = React.useMemo<GridColDef<ChannelTO>[]>(
        () => [
            {field: 'name', headerName: 'Name', flex: 1, minWidth: 80, headerAlign: 'center', align: 'center'},
            // bot-specific actions column lives here (edit/delete) if needed
        ],
        [],
    );

    return (
        <BaseList<ChannelTO>
            title="Channels"
            columns={columns}
            gridState={gridState}
            load={(params) => ChannelRepository.list(params)}
            onRowClick={(row) => navigate(`/channel/${row.id}`)}
            onCreateClick={() => navigate('/channel/new')}
        />
    );
}