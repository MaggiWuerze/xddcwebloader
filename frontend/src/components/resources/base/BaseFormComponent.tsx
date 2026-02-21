import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import FormGroup from '@mui/material/FormGroup';
import Stack from '@mui/material/Stack';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { useNavigate } from 'react-router';

export type FormFieldValue = string | string[] | number | boolean | File | null;

export type FormState<V extends Record<string, any>> = {
  values: Partial<V>;
  errors: Partial<Record<keyof V, string>>;
};

type Props<V extends Record<string, any>> = {
  formState: FormState<V>;
  onFieldChange: (name: keyof V, value: FormFieldValue) => void;
  onSubmit: (values: Partial<V>) => Promise<void>;
  onReset?: (values: Partial<V>) => void;

  submitButtonLabel: string;
  backButtonPath: string;

  children: (ctx: {
    values: Partial<V>;
    errors: Partial<Record<keyof V, string>>;
    onFieldChange: (name: keyof V, value: FormFieldValue) => void;
  }) => React.ReactNode;
};

export function BaseForm<V extends Record<string, any>>(props: Props<V>) {
  const { formState, onFieldChange, onSubmit, onReset, submitButtonLabel, backButtonPath, children } = props;

  const navigate = useNavigate();
  const [isSubmitting, setIsSubmitting] = React.useState(false);

  const handleSubmit = React.useCallback(
    async (event: React.FormEvent<HTMLFormElement>) => {
      event.preventDefault();
      setIsSubmitting(true);
      try {
        await onSubmit(formState.values);
      } finally {
        setIsSubmitting(false);
      }
    },
    [formState.values, onSubmit],
  );

  const handleReset = React.useCallback(() => {
    onReset?.(formState.values);
  }, [formState.values, onReset]);

  const handleBack = React.useCallback(() => {
    navigate(backButtonPath);
  }, [navigate, backButtonPath]);

  return (
    <Box
      component="form"
      onSubmit={handleSubmit}
      onReset={handleReset}
      noValidate
      autoComplete="off"
      sx={{ width: '100%' }}
    >
      <FormGroup>{children({ values: formState.values, errors: formState.errors, onFieldChange })}</FormGroup>

      <Stack direction="row" spacing={2} justifyContent="space-between">
        <Button variant="contained" startIcon={<ArrowBackIcon />} onClick={handleBack}>
          Back
        </Button>
        <Button type="submit" variant="contained" size="large" loading={isSubmitting}>
          {submitButtonLabel}
        </Button>
      </Stack>
    </Box>
  );
}
