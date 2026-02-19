import * as React from 'react';
import {MenuItem, Select, type SelectChangeEvent} from '@mui/material';
import type {BotTO} from '../../../api/rest';

type Props = {
    botSelectValues: Promise<BotTO[]>; // or Promise<{ items: ServerTO[] }>
    onChange: (botId: string) => void;
};

export function BotSelect({botSelectValues, onChange}: Props) {
    const [channels, setBots] = React.useState<BotTO[]>([]);
    const [loading, setLoading] = React.useState(true);
    const [BotId, setBotId] = React.useState<string>('');

    React.useEffect(() => {
        let cancelled = false;

        (async () => {
            setLoading(true);
            try {
                const result = await botSelectValues;
                if (!cancelled) setBots(result);
            } finally {
                if (!cancelled) setLoading(false);
            }
        })();

        return () => {
            cancelled = true;
        };
    }, [botSelectValues]);

    const handleChange = (e: SelectChangeEvent) => {
        setBotId(e.target.value);
        onChange(e.target.value);
    };

    return (
        <Select value={BotId} onChange={handleChange} displayEmpty disabled={loading} fullWidth>
            <MenuItem value="" disabled>
                {loading ? 'Loading…' : 'Select Bot'}
            </MenuItem>

            {channels.map((s) => (
                <MenuItem key={s.id} value={s.id}>
                    {s.name}
                </MenuItem>
            ))}
        </Select>
    );
}