import * as React from 'react';
import {useNavigate} from 'react-router';
import useNotifications from '../../../hooks/useNotifications/useNotifications';
import {BotRepository as repository} from '../../../data/botRepository';
import BotForm, {type BotFormState, type FormFieldValue,} from './BotForm';
import PageContainer from '../../pagecontainer/PageContainer';
import {BotFormTO} from "../../../api/rest";

const INITIAL_FORM_VALUES: Partial<BotFormState['values']> = {
    name: '',
    pattern: '',
    maxParallelDownloads: 3,
    serverId: undefined,
    channelId: undefined,

};

export default function BotCreate() {
    const navigate = useNavigate();

    const notifications = useNotifications();

    const [formState, setFormState] = React.useState<BotFormState>(() => ({
        values: INITIAL_FORM_VALUES,
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
        console.log("creating bot")
        console.log(formValues)
        const {issues} = repository.validate(formValues);
        if (issues && issues.length > 0) {
            console.log(issues)
            setFormErrors(
                Object.fromEntries(issues.map((issue) => [issue.path?.[0], issue.message])),
            );
            return;
        }
        setFormErrors({});

        try {
            await repository.create(formValues as Omit<BotFormTO, 'id'>);
            notifications.show('Bot created successfully.', {
                severity: 'success',
                autoHideDuration: 3000,
            });

            navigate('/bot');
        } catch (createError) {
            notifications.show(
                `Failed to create Bot. Reason: ${(createError as Error).message}`,
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
            title="New Bot"
            breadcrumbs={[{title: 'Bots', path: '/bot'}, {title: 'New'}]}
        >
            <BotForm
                formState={formState}
                onFieldChange={handleFormFieldChange}
                onSubmit={handleFormSubmit}
                onReset={handleFormReset}
                submitButtonLabel="Create"
            />
        </PageContainer>
    );
}
