import * as React from 'react';
import {MenuItem, Select, type SelectChangeEvent} from '@mui/material';
import type {ChannelTO} from '../../../api/rest';

type Props = {
    channelSelectValues: Promise<ChannelTO[]>; // or Promise<{ items: ServerTO[] }>
    onChange: (serverId: string) => void;
};

export function ChannelSelect({channelSelectValues, onChange}: Props) {
    const [channels, setChannels] = React.useState<ChannelTO[]>([]);
    const [loading, setLoading] = React.useState(true);
    const [channelId, setChannelId] = React.useState<string>('');

    React.useEffect(() => {
        let cancelled = false;

        (async () => {
            setLoading(true);
            try {
                const result = await channelSelectValues;
                if (!cancelled) setChannels(result);
            } finally {
                if (!cancelled) setLoading(false);
            }
        })();

        return () => {
            cancelled = true;
        };
    }, [channelSelectValues]);

    const handleChange = (e: SelectChangeEvent) => {
        setChannelId(e.target.value);
        onChange(e.target.value);
    };

    return (
        <Select value={channelId} onChange={handleChange} displayEmpty disabled={loading} fullWidth>
            <MenuItem value="" disabled>
                {loading ? 'Loading…' : 'Select Channel'}
            </MenuItem>

            {channels.map((s) => (
                <MenuItem key={s.id} value={s.id}>
                    {s.name}
                </MenuItem>
            ))}
        </Select>
    );
}