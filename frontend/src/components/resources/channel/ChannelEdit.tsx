import * as React from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import CircularProgress from '@mui/material/CircularProgress';
import {useNavigate, useParams} from 'react-router';
import useNotifications from '../../../hooks/useNotifications/useNotifications';
import {ChannelRepository as repository} from '../../../data/channelRepository';
import ChannelForm, {type ChannelFormState, type FormFieldValue,} from './ChannelForm';
import PageContainer from '../../pagecontainer/PageContainer';
import {ChannelTO} from "../../../api/rest";

function EmployeeEditForm({
                              initialValues,
                              onSubmit,
                          }: {
    initialValues: Partial<ChannelFormState['values']>;
    onSubmit: (formValues: Partial<ChannelFormState['values']>) => Promise<void>;
}) {
    const {employeeId} = useParams();
    const navigate = useNavigate();

    const notifications = useNotifications();

    const [formState, setFormState] = React.useState<ChannelFormState>(() => ({
        values: initialValues,
        errors: {},
    }));
    const formValues = formState.values;
    const formErrors = formState.errors;

    const setFormValues = React.useCallback(
        (newFormValues: Partial<ChannelFormState['values']>) => {
            setFormState((previousState) => ({
                ...previousState,
                values: newFormValues,
            }));
        },
        [],
    );

    const setFormErrors = React.useCallback(
        (newFormErrors: Partial<ChannelFormState['errors']>) => {
            setFormState((previousState) => ({
                ...previousState,
                errors: newFormErrors,
            }));
        },
        [],
    );

    const handleFormFieldChange = React.useCallback(
        (name: keyof ChannelFormState['values'], value: FormFieldValue) => {
            const validateField = async (values: Partial<ChannelFormState['values']>) => {
                const {issues} = repository.validate(values);
                setFormErrors({
                    ...formErrors,
                    [name]: issues?.find((issue) => issue.path?.[0] === name)?.message,
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
                Object.fromEntries(issues.map((issue) => [issue.path?.[0], issue.message])),
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

            navigate('/employees');
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
        <ChannelForm
            formState={formState}
            onFieldChange={handleFormFieldChange}
            onSubmit={handleFormSubmit}
            onReset={handleFormReset}
            submitButtonLabel="Save"
            backButtonPath={`/employees/${employeeId}`}
        />
    );
}

export default function ChannelEdit() {
    const {employeeId} = useParams();

    const [employee, setEmployee] = React.useState<ChannelTO | null>(null);
    const [isLoading, setIsLoading] = React.useState(true);
    const [error, setError] = React.useState<Error | null>(null);

    const loadData = React.useCallback(async () => {
        if (!employeeId) return
        setError(null);
        setIsLoading(true);

        try {
            const showData = await repository.get(employeeId);

            setEmployee(showData);
        } catch (showDataError) {
            setError(showDataError as Error);
        }
        setIsLoading(false);
    }, [employeeId]);

    React.useEffect(() => {
        loadData();
    }, [loadData]);

    const handleSubmit = React.useCallback(
        async (formValues: Partial<ChannelFormState['values']>) => {
            if (!employeeId) return
            const updatedData = await repository.update(employeeId, formValues);
            setEmployee(updatedData);
        },
        [employeeId],
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
            title={`Edit Employee ${employeeId}`}
            breadcrumbs={[
                {title: 'Employees', path: '/employees'},
                {title: `Employee ${employeeId}`, path: `/employees/${employeeId}`},
                {title: 'Edit'},
            ]}
        >
            <Box sx={{display: 'flex', flex: 1}}>{renderEdit}</Box>
        </PageContainer>
    );
}
