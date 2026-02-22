import * as React from 'react';
import {MenuItem, Select, type SelectChangeEvent} from '@mui/material';
import type {SearchEngineTO} from '../../../api/rest';

type Props = {
    searchSelectValues: Promise<SearchEngineTO[]>;
    onChange: (serverId: string) => void;
};

export function SearchProviderSelect({searchSelectValues, onChange}: Props) {
    const [loading, setLoading] = React.useState(true);
    const [serverId, setServerId] = React.useState<string>('');
    const [searchEngines, setSearchEngines] = React.useState<SearchEngineTO[]>([]);

    React.useEffect(() => {
        let cancelled = false;

        (async () => {
            setLoading(true);
            try {
                const result = await searchSelectValues;
                if (!cancelled) setSearchEngines(result);
            } finally {
                if (!cancelled) setLoading(false);
            }
        })();

        return () => {
            cancelled = true;
        };
    }, [searchSelectValues]);

    React.useEffect(() => {
        if (loading) return;
        if (serverId) return;                 // don’t override a user choice
        if (searchEngines.length === 0) return;

        const first = searchEngines[0]!.name;  // matches your MenuItem value={s.name}
        setServerId(first);
        onChange(first);                      // optional, but usually desired
    }, [loading, searchEngines, serverId, onChange]);


    const handleChange = (e: SelectChangeEvent) => {
        setServerId(e.target.value);
        onChange(e.target.value);
    };

    return (
        <div>
            <Select value={serverId} onChange={handleChange}
                    disabled={loading} fullWidth>
                <MenuItem value="" disabled>
                    {loading ? 'Loading…' : 'Select Search Provider'}
                </MenuItem>

                {searchEngines.map((s) => (
                    <MenuItem key={s.name} value={s.name}>
                        {s.name}
                    </MenuItem>
                ))}
            </Select>
        </div>
    );
}