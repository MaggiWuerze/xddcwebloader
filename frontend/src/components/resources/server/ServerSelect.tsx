import * as React from 'react';
import {MenuItem, Select, type SelectChangeEvent} from '@mui/material';
import type {ServerTO} from '../../../api/rest';

type Props = {
    serverSelectValues: Promise<ServerTO[]>;
    onChange: (serverId: string) => void;
};

export function ServerSelect({serverSelectValues, onChange}: Props) {
    const [servers, setServers] = React.useState<ServerTO[]>([]);
    const [loading, setLoading] = React.useState(true);
    const [serverId, setServerId] = React.useState<string>('');

    React.useEffect(() => {
        let cancelled = false;

        (async () => {
            setLoading(true);
            try {
                const result = await serverSelectValues;
                if (!cancelled) setServers(result);
            } finally {
                if (!cancelled) setLoading(false);
            }
        })();

        return () => {
            cancelled = true;
        };
    }, [serverSelectValues]);

    const handleChange = (e: SelectChangeEvent) => {
        setServerId(e.target.value);
        onChange(e.target.value);
    };

    return (
        <Select value={serverId} onChange={handleChange} displayEmpty disabled={loading} fullWidth>
            <MenuItem value="" disabled>
                {loading ? 'Loading…' : 'Select Server'}
            </MenuItem>

            {servers.map((s) => (
                <MenuItem key={s.id} value={s.id}>
                    {s.name}
                </MenuItem>
            ))}
        </Select>
    );
}