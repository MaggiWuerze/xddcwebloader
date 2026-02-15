import * as React from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import CircularProgress from '@mui/material/CircularProgress';
import {useNavigate, useParams} from 'react-router';
import useNotifications from '../../../hooks/useNotifications/useNotifications';
import {BotRepository as repository} from '../../../data/botRepository';
import BotForm, {type BotFormState, type FormFieldValue,} from './BotForm';
import PageContainer from '../../pagecontainer/PageContainer';
import {BotTO} from "../../../api/rest";

function EmployeeEditForm({
                              initialValues,
                              onSubmit,
                          }: {
    initialValues: Partial<BotFormState['values']>;
    onSubmit: (formValues: Partial<BotFormState['values']>) => Promise<void>;
}) {
    const {employeeId} = useParams();
    const navigate = useNavigate();

    const notifications = useNotifications();

    const [formState, setFormState] = React.useState<BotFormState>(() => ({
        values: initialValues,
        errors: {},
    }));
    const formValues = formState.values;
    const formErrors = formState.errors;

    const setFormValues = React.useCallback(
        (newFormValues: Partial<BotFormState['values']>) => {
            setFormState((previousState) => ({
                ...previousState,
                values: newFormValues,
            }));
        },
        [],
    );

    const setFormErrors = React.useCallback(
        (newFormErrors: Partial<BotFormState['errors']>) => {
            setFormState((previousState) => ({
                ...previousState,
                errors: newFormErrors,
            }));
        },
        [],
    );

    const handleFormFieldChange = React.useCallback(
        (name: keyof BotFormState['values'], value: FormFieldValue) => {
            const validateField = async (values: Partial<BotFormState['values']>) => {
                const {issues} = repository.validate(values);
                setFormErrors({
                    ...formErrors,
                    [name]: issues?.find((issue: {
                        path: (string | number | symbol)[];
                    }) => issue.path?.[0] === name)?.message,
                });
            };

            const newFormValues = {...formValues, [name]: value};

            setFormValues(newFormValues);
            validateField(newFormValues);
        },
        [formValues, formErrors, setFormErrors, setFormValues],
    );

    const handleFormReset = React.useCallback(() => {
        setFormValues(initialValues);
    }, [initialValues, setFormValues]);

    const handleFormSubmit = React.useCallback(async () => {
        const {issues} = repository.validate(formValues);
        if (issues && issues.length > 0) {
            setFormErrors(
                Object.fromEntries(issues.map((issue: {
                    path: any[];
                    message: any;
                }) => [issue.path?.[0], issue.message])),
            );
            return;
        }
        setFormErrors({});

        try {
            await onSubmit(formValues);
            notifications.show('Employee edited successfully.', {
                severity: 'success',
                autoHideDuration: 3000,
            });

            navigate('/bot');
        } catch (editError) {
            notifications.show(
                `Failed to edit employee. Reason: ${(editError as Error).message}`,
                {
                    severity: 'error',
                    autoHideDuration: 3000,
                },
            );
            throw editError;
        }
    }, [formValues, navigate, notifications, onSubmit, setFormErrors]);

    return (
        <BotForm
            formState={formState}
            onFieldChange={handleFormFieldChange}
            onSubmit={handleFormSubmit}
            onReset={handleFormReset}
            submitButtonLabel="Save"
            backButtonPath={`/bot/${employeeId}`}
        />
    );
}

export default function BotEdit() {
    const {botId} = useParams();

    const [employee, setEmployee] = React.useState<BotTO | null>(null);
    const [isLoading, setIsLoading] = React.useState(true);
    const [error, setError] = React.useState<Error | null>(null);

    const loadData = React.useCallback(async () => {
        if (!botId) return;
        setError(null);
        setIsLoading(true);

        try {
            const showData = await repository.get(botId);

            setEmployee(showData);
        } catch (showDataError) {
            setError(showDataError as Error);
        }
        setIsLoading(false);
    }, [botId]);

    React.useEffect(() => {
        loadData();
    }, [loadData]);

    const handleSubmit = React.useCallback(
        async (formValues: Partial<BotFormState['values']>) => {
            if (!botId) return;
            const updatedData = await repository.update(botId, formValues);
            setEmployee(updatedData);
        },
        [botId],
    );

    const renderEdit = React.useMemo(() => {
        if (isLoading) {
            return (
                <Box
                    sx={{
                        flex: 1,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        justifyContent: 'center',
                        width: '100%',
                        m: 1,
                    }}
                >
                    <CircularProgress/>
                </Box>
            );
        }
        if (error) {
            return (
                <Box sx={{flexGrow: 1}}>
                    <Alert severity="error">{error.message}</Alert>
                </Box>
            );
        }

        return employee ? (
            <EmployeeEditForm initialValues={employee} onSubmit={handleSubmit}/>
        ) : null;
    }, [isLoading, error, employee, handleSubmit]);

    return (
        <PageContainer
            title={`Edit Employee ${botId}`}
            breadcrumbs={[
                {title: 'Employees', path: '/bot'},
                {title: `Employee ${botId}`, path: `/bot/${botId}`},
                {title: 'Edit'},
            ]}
        >
            <Box sx={{display: 'flex', flex: 1}}>{renderEdit}</Box>
        </PageContainer>
    );
}
