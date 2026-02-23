import * as React from 'react';
import {useNavigate} from 'react-router';
import useNotifications from '../../../hooks/useNotifications/useNotifications';
import {DownloadRepository as repository} from '../../../data/downloadRepository';
import DownloadForm, {type DownloadFormState, type FormFieldValue,} from './DownloadForm';
import PageContainer from '../../pagecontainer/PageContainer';
import {DownloadFormTO} from "../../../api/rest";

const INITIAL_FORM_VALUES: Partial<DownloadFormState['values']> = {
    fileRefId: '',
    targetBotId: undefined,

};

export default function DownloadCreate() {
    const navigate = useNavigate();

    const notifications = useNotifications();

    const [formState, setFormState] = React.useState<DownloadFormState>(() => ({
        values: INITIAL_FORM_VALUES,
        errors: {},
    }));
    const formValues = formState.values;
    const formErrors = formState.errors;

    const setFormValues = React.useCallback(
        (newFormValues: Partial<DownloadFormState['values']>) => {
            setFormState((previousState) => ({
                ...previousState,
                values: newFormValues,
            }));
        },
        [],
    );

    const setFormErrors = React.useCallback(
        (newFormErrors: Partial<DownloadFormState['errors']>) => {
            setFormState((previousState) => ({
                ...previousState,
                errors: newFormErrors,
            }));
        },
        [],
    );

    const handleFormFieldChange = React.useCallback(
        (name: keyof DownloadFormState['values'], value: FormFieldValue) => {
            const validateField = async (values: Partial<DownloadFormState['values']>) => {
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
            await repository.create(formValues as Omit<DownloadFormTO, 'id'>);
            notifications.show('Download created successfully.', {
                severity: 'success',
                autoHideDuration: 3000,
            });

            navigate('/download/');
        } catch (createError) {
            notifications.show(
                `Failed to create Download. Reason: ${(createError as Error).message}`,
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
            title="New Download"
            breadcrumbs={[{title: 'Downloads', path: '/downloads/all'}, {title: 'New'}]}
        >
            <DownloadForm
                formState={formState}
                onFieldChange={handleFormFieldChange}
                onSubmit={handleFormSubmit}
                onReset={handleFormReset}
                submitButtonLabel="Create"
            />
        </PageContainer>
    );
}
