import * as React from 'react';
import {useNavigate} from 'react-router';
import useNotifications from '../../../hooks/useNotifications/useNotifications';
import ChannelForm, {type ChannelFormState, type FormFieldValue,} from './ChannelForm';
import PageContainer from '../../pagecontainer/PageContainer';
import {ChannelRepository as repository} from '../../../data/channelRepository';
import {ChannelTO} from "../../../api/rest";

const INITIAL_FORM_VALUES: Partial<ChannelFormState['values']> = {
    name: '',
};

export default function ChannelCreate() {
    const navigate = useNavigate();

    const notifications = useNotifications();

    const [formState, setFormState] = React.useState<ChannelFormState>(() => ({
        values: INITIAL_FORM_VALUES,
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
        setFormValues(INITIAL_FORM_VALUES);
    }, [setFormValues]);

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
            await repository.create(formValues as Omit<ChannelTO, 'id'>);
            notifications.show('Channel created successfully.', {
                severity: 'success',
                autoHideDuration: 3000,
            });

            navigate('/bot');
        } catch (createError) {
            notifications.show(
                `Failed to create Channel. Reason: ${(createError as Error).message}`,
                {
                    severity: 'error',
                    autoHideDuration: 3000,
                },
            );
            throw createError;
        }
    }, [formValues, navigate, notifications, setFormErrors]);

    return (
        <PageContainer
            title="New Channel"
            breadcrumbs={[{title: 'Channel', path: '/channel'}, {title: 'New'}]}
        >
            <ChannelForm
                formState={formState}
                onFieldChange={handleFormFieldChange}
                onSubmit={handleFormSubmit}
                onReset={handleFormReset}
                submitButtonLabel="Create"
            />
        </PageContainer>
    );
}
