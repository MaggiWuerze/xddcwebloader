import * as React from 'react';
import {MenuItem, Select, type SelectChangeEvent} from '@mui/material';
import type {SearchEngineTO} from '../../../api/rest';

type Props = {
    serverSelectValues: Promise<SearchEngineTO[]>;
    onChange: (serverId: string) => void;
};

export function SearchProviderSelect({serverSelectValues, onChange}: Props) {
    const [searchEngine, setSearchEngine] = React.useState<SearchEngineTO[]>([]);
    const [loading, setLoading] = React.useState(true);
    const [serverId, setServerId] = React.useState<string>('');

    React.useEffect(() => {
        let cancelled = false;

        (async () => {
            setLoading(true);
            try {
                const result = await serverSelectValues;
                if (!cancelled) setSearchEngine(result);
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
                {loading ? 'Loading…' : 'Select Search Provider'}
            </MenuItem>

            {searchEngine.map((s) => (
                <MenuItem key={s.name} value={s.name}>
                    {s.name}
                </MenuItem>
            ))}
        </Select>
    );
}