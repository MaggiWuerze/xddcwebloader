import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import FormGroup from '@mui/material/FormGroup';
import Grid from '@mui/material/Grid';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {useNavigate} from 'react-router';
import {DownloadFormTO} from "../../../api/rest";
import {BotRepository as botRepository} from "../../../data/botRepository";
import {BotSelect} from "../bot/BotSelect";

export interface DownloadFormState {
    values: Partial<Omit<DownloadFormTO, 'id'>>;
    errors: Partial<Record<keyof DownloadFormState['values'], string>>;
}

export type FormFieldValue = string | string[] | number | boolean | File | null;

export interface EmployeeFormProps {
    formState: DownloadFormState;
    onFieldChange: (
        name: keyof DownloadFormState['values'],
        value: FormFieldValue,
    ) => void;
    onSubmit: (formValues: Partial<DownloadFormState['values']>) => Promise<void>;
    onReset?: (formValues: Partial<DownloadFormState['values']>) => void;
    submitButtonLabel: string;
    backButtonPath?: string;
}

export default function DownloadForm(props: EmployeeFormProps) {
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
                event.target.name as keyof DownloadFormState['values'],
                event.target.value,
            );
        },
        [onFieldChange],
    );

    const handleNumberFieldChange = React.useCallback(
        (event: React.ChangeEvent<HTMLInputElement>) => {
            onFieldChange(
                event.target.name as keyof DownloadFormState['values'],
                Number(event.target.value),
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
                            value={formValues.fileRefId ?? ''}
                            onChange={handleTextFieldChange}
                            name="fileRefId"
                            label="FileRefId"
                            placeholder="1234"
                            error={!!formErrors.fileRefId}
                            helperText={formErrors.fileRefId ?? ' '}
                            fullWidth
                        />
                    </Grid>
                    <Grid size={{xs: 12, sm: 6}} sx={{display: 'flex'}}>
                        <BotSelect
                            botSelectValues={botRepository.listAll()}
                            onChange={(botId) => formValues.targetBotId = botId}
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
