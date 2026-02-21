import * as React from 'react';
import {ServerRepository} from '../../../data/serverRepository';
import {ServerFormTO, ServerTO} from "../../../api/rest";
import {CrudForm} from "../base/CrudFormHandler";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import FormGroup from "@mui/material/FormGroup";
import Box from '@mui/material/Box';


export default function ServerCreate() {
    return (
        <CrudForm<ServerFormTO, ServerTO>
            resource="server"
            mode="create"
            idParam="serverId"
            repository={ServerRepository}
            submitLabel="Create"
            initialValues={{name: '', serverUrl: ''}}
            renderFields={({values, errors, onFieldChange}) => (
                <Box
                    component="form"
                    noValidate
                    autoComplete="off"
                    sx={{width: '100%'}}
                >
                    <FormGroup>
                        <Grid container spacing={2} sx={{mb: 2, width: '100%'}}>
                            <Grid size={{xs: 12, sm: 6}} sx={{display: 'flex'}}>
                                <TextField
                                    type="text"
                                    value={values.name ?? ''}
                                    onChange={(e) => onFieldChange('name', e.target.value)}
                                    name="name"
                                    label="Name"
                                    error={!!errors.name}
                                    helperText={errors.name ?? ' '}
                                    fullWidth
                                />
                            </Grid>
                            <Grid size={{xs: 12, sm: 6}} sx={{display: 'flex'}}>
                                <TextField
                                    type="text"
                                    value={values.serverUrl ?? ''}
                                    onChange={(e) => onFieldChange('serverUrl', e.target.value)}
                                    name="serverUrl"
                                    label="Server URL"
                                    error={!!errors.serverUrl}
                                    helperText={errors.serverUrl ?? ' '}
                                    fullWidth
                                />
                            </Grid>
                        </Grid>
                    </FormGroup>
                </Box>
            )}
        />
    );
}