import * as React from 'react';
import {useNavigate, useParams} from 'react-router';
import {BaseForm, type FormFieldValue, type FormState} from './BaseFormComponent';
import PageContainer from "../../pagecontainer/PageContainer";

type CrudRepository<T, F> = {
    get: (id: string) => Promise<T>;
    create: (data: Omit<F, 'id'>) => Promise<T>;
    update: (id: string, data: Partial<Omit<F, 'id'>>) => Promise<T>;
    validate: (entity: Partial<F>) => { issues: { message: string; path: string[] }[] };
};

type Props<F extends Record<string, any>, T> = {
    resource: string;              // "bot" | "server" | ...
    mode: 'create' | 'edit';
    idParam: string;               // e.g. "botId" (matches your router param name)
    repository: CrudRepository<T, F>;
    submitLabel: string;

    initialValues?: Partial<F>;    // useful for create defaults
    mapEntityToFormValues?: (entity: T) => Partial<F>; // for edit mode

    renderFields: (ctx: {
        values: Partial<F>;
        errors: Partial<Record<keyof F, string>>;
        onFieldChange: (name: keyof F, value: FormFieldValue) => void;
    }) => React.ReactNode;
};

export function CrudForm<F extends Record<string, any>, T>(props: Props<F, T>) {
    const {
        resource,
        mode,
        idParam,
        repository,
        submitLabel,
        initialValues,
        mapEntityToFormValues,
        renderFields,
    } = props;

    const navigate = useNavigate();
    const params = useParams();
    const id = params[idParam]; // string | undefined

    const backPath = `/${resource}`;

    const [formState, setFormState] = React.useState<FormState<F>>({
        values: initialValues ?? {},
        errors: {},
    });

    // Edit: load entity -> fill values
    React.useEffect(() => {
        if (mode !== 'edit') return;
        if (!id) return;

        (async () => {
            const entity = await repository.get(id);
            setFormState((prev: any) => ({
                ...prev,
                values: mapEntityToFormValues ? mapEntityToFormValues(entity) : (entity as any),
                errors: {},
            }));
        })();
    }, [mode, id, repository, mapEntityToFormValues]);

    const onFieldChange = React.useCallback((name: keyof F, value: FormFieldValue) => {
        setFormState((prev: { values: Partial<F>; errors: any; }) => ({
            values: {...prev.values, [name]: value} as Partial<F>,
            errors: {...prev.errors, [name]: undefined},
        }));
    }, []);

    const onSubmit = React.useCallback(
        async (values: Partial<F>) => {
            // validate
            const validation = repository.validate(values);
            const nextErrors: Partial<Record<keyof F, string>> = {};

            for (const issue of validation.issues ?? []) {
                const key = issue.path?.[0] as keyof F | undefined;
                if (key) nextErrors[key] = issue.message;
            }

            if (Object.keys(nextErrors).length > 0) {
                setFormState((prev: any) => ({...prev, errors: nextErrors}));
                return;
            }

            // persist
            if (mode === 'create') {
                await repository.create(values as Omit<F, 'id'>);
            } else {
                if (!id) throw new Error('Missing id route param');
                await repository.update(id, values as Partial<Omit<F, 'id'>>);
            }

            navigate(backPath);
        },
        [repository, mode, id, navigate, backPath],
    );

    return (

        <PageContainer
            title="New Server"
            breadcrumbs={[{title: 'Servers', path: '/server'}, {title: 'New'}]}
        >
            <BaseForm<F>
                formState={formState}
                onFieldChange={onFieldChange}
                onSubmit={onSubmit}
                submitButtonLabel={submitLabel}
                backButtonPath={backPath}
            >
                {renderFields}
            </BaseForm>
        </PageContainer>

    );
}
