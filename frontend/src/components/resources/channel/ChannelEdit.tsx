import * as React from 'react';
import Box from '@mui/material/Box';
import {ChannelRepository} from '../../../data/channelRepository';
import {ChannelFormTO, ChannelTO} from "../../../api/rest";
import {CrudForm} from "../base/CrudFormHandler";
import FormGroup from "@mui/material/FormGroup";
import Grid from "@mui/material/Grid";
import TextField from "@mui/material/TextField";

export default function ChannelEdit() {
    return (
        <CrudForm<ChannelFormTO, ChannelTO>
            resource="server"
            mode="edit"
            idParam="serverId"
            repository={ChannelRepository}
            submitLabel="Save"
            mapEntityToFormValues={(channel) => ({name: channel.name})}
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
                        </Grid>
                    </FormGroup>
                </Box>
            )}
        />
    );
}
