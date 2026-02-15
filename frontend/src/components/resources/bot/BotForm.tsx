import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import FormGroup from '@mui/material/FormGroup';
import Grid from '@mui/material/Grid';
import {SelectChangeEvent} from '@mui/material/Select';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {useNavigate} from 'react-router';
import {BotTO} from "../../../api/rest";

export interface BotFormState {
    values: Partial<Omit<BotTO, 'id'>>;
    errors: Partial<Record<keyof BotFormState['values'], string>>;
}

export type FormFieldValue = string | string[] | number | boolean | File | null;

export interface EmployeeFormProps {
    formState: BotFormState;
    onFieldChange: (
        name: keyof BotFormState['values'],
        value: FormFieldValue,
    ) => void;
    onSubmit: (formValues: Partial<BotFormState['values']>) => Promise<void>;
    onReset?: (formValues: Partial<BotFormState['values']>) => void;
    submitButtonLabel: string;
    backButtonPath?: string;
}

export default function BotForm(props: EmployeeFormProps) {
    const {
        formState,
        onFieldChange,
        onSubmit,
        onReset,
        submitButtonLabel,
        backButtonPath,
    } = props;

    const formValues = formState.values;
    const formErrors = formState.errors;

    const navigate = useNavigate();

    const [isSubmitting, setIsSubmitting] = React.useState(false);

    const handleSubmit = React.useCallback(
        async (event: React.FormEvent<HTMLFormElement>) => {
            event.preventDefault();

            setIsSubmitting(true);
            try {
                await onSubmit(formValues);
            } finally {
                setIsSubmitting(false);
            }
        },
        [formValues, onSubmit],
    );

    const handleTextFieldChange = React.useCallback(
        (event: React.ChangeEvent<HTMLInputElement>) => {
            onFieldChange(
                event.target.name as keyof BotFormState['values'],
                event.target.value,
            );
        },
        [onFieldChange],
    );

    const handleNumberFieldChange = React.useCallback(
        (event: React.ChangeEvent<HTMLInputElement>) => {
            onFieldChange(
                event.target.name as keyof BotFormState['values'],
                Number(event.target.value),
            );
        },
        [onFieldChange],
    );

    const handleCheckboxFieldChange = React.useCallback(
        (event: React.ChangeEvent<HTMLInputElement>, checked: boolean) => {
            onFieldChange(event.target.name as keyof BotFormState['values'], checked);
        },
        [onFieldChange],
    );

    const handleSelectFieldChange = React.useCallback(
        (event: SelectChangeEvent) => {
            onFieldChange(
                event.target.name as keyof BotFormState['values'],
                event.target.value,
            );
        },
        [onFieldChange],
    );

    const handleReset = React.useCallback(() => {
        if (onReset) {
            onReset(formValues);
        }
    }, [formValues, onReset]);

    const handleBack = React.useCallback(() => {
        navigate(backButtonPath ?? '/bot');
    }, [navigate, backButtonPath]);

    return (
        <Box
            component="form"
            onSubmit={handleSubmit}
            noValidate
            autoComplete="off"
            onReset={handleReset}
            sx={{width: '100%'}}
        >
            <FormGroup>
                <Grid container spacing={2} sx={{mb: 2, width: '100%'}}>
                    <Grid size={{xs: 12, sm: 6}} sx={{display: 'flex'}}>
                        <TextField
                            value={formValues.name ?? ''}
                            onChange={handleTextFieldChange}
                            name="name"
                            label="Name"
                            error={!!formErrors.name}
                            helperText={formErrors.name ?? ' '}
                            fullWidth
                        />
                    </Grid>
                    <Grid size={{xs: 12, sm: 6}} sx={{display: 'flex'}}>
                        <TextField
                            type="number"
                            value={formValues.pattern ?? ''}
                            onChange={handleNumberFieldChange}
                            name="pattern"
                            label="Pattern"
                            error={!!formErrors.pattern}
                            helperText={formErrors.pattern ?? ' '}
                            fullWidth
                        />
                    </Grid>
                </Grid>
            </FormGroup>
            <Stack direction="row" spacing={2} justifyContent="space-between">
                <Button
                    variant="contained"
                    startIcon={<ArrowBackIcon/>}
                    onClick={handleBack}
                >
                    Back
                </Button>
                <Button
                    type="submit"
                    variant="contained"
                    size="large"
                    loading={isSubmitting}
                >
                    {submitButtonLabel}
                </Button>
            </Stack>
        </Box>
    );
}
