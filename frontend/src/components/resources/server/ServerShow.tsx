import * as React from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import CircularProgress from '@mui/material/CircularProgress';
import Divider from '@mui/material/Divider';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {useNavigate, useParams} from 'react-router';
import {useDialogs} from '../../../hooks/useDialogs/useDialogs';
import useNotifications from '../../../hooks/useNotifications/useNotifications';
import {ServerRepository as repository} from '../../../data/serverRepository';
import PageContainer from '../../pagecontainer/PageContainer';
import {ServerTO} from "../../../api/rest";

export default function ServerShow() {
    const {serverId} = useParams();

    const navigate = useNavigate();

    const dialogs = useDialogs();
    const notifications = useNotifications();

    const [server, setServer] = React.useState<ServerTO | null>(null);
    const [isLoading, setIsLoading] = React.useState(true);
    const [error, setError] = React.useState<Error | null>(null);

    const loadData = React.useCallback(async () => {
        if (!serverId) return;
        setError(null);
        setIsLoading(true);

        try {
            const showData = await repository.get(serverId);

            setServer(showData);
        } catch (showDataError) {
            setError(showDataError as Error);
        }
        setIsLoading(false);
    }, [serverId]);

    React.useEffect(() => {
        loadData();
    }, [loadData]);

    const handleServerEdit = React.useCallback(() => {
        navigate(`/server/${serverId}/edit`);
    }, [navigate, serverId]);

    const handleServerDelete = React.useCallback(async () => {
        if (!server || !serverId) {
            return;
        }

        const confirmed = await dialogs.confirm(
            `Do you wish to delete ${server.name}?`,
            {
                title: `Delete Server?`,
                severity: 'error',
                okText: 'Delete',
                cancelText: 'Cancel',
            },
        );

        if (confirmed) {
            setIsLoading(true);
            try {
                await repository.delete(serverId);

                navigate('/server');

                notifications.show('Server deleted successfully.', {
                    severity: 'success',
                    autoHideDuration: 3000,
                });
            } catch (deleteError) {
                notifications.show(
                    `Failed to delete Server. Reason:' ${(deleteError as Error).message}`,
                    {
                        severity: 'error',
                        autoHideDuration: 3000,
                    },
                );
            }
            setIsLoading(false);
        }
    }, [server, dialogs, serverId, navigate, notifications]);

    const handleBack = React.useCallback(() => {
        navigate('/server');
    }, [navigate]);

    const renderShow = React.useMemo(() => {
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

        return server ? (
            <Box sx={{flexGrow: 1, width: '100%'}}>
                <Grid container spacing={2} sx={{width: '100%'}}>
                    <Grid size={{xs: 12, sm: 6}}>
                        <Paper sx={{px: 2, py: 1}}>
                            <Typography variant="overline">Name</Typography>
                            <Typography variant="body1" sx={{mb: 1}}>
                                {server.name}
                            </Typography>
                        </Paper>
                    </Grid>
                    <Grid size={{xs: 12, sm: 6}}>
                        <Paper sx={{px: 2, py: 1}}>
                            <Typography variant="overline">Server URL</Typography>
                            <Typography variant="body1" sx={{mb: 1}}>
                                {server.serverUrl}
                            </Typography>
                        </Paper>
                    </Grid>
                </Grid>
                <Divider sx={{my: 3}}/>
                <Stack direction="row" spacing={2} justifyContent="space-between">
                    <Button
                        variant="contained"
                        startIcon={<ArrowBackIcon/>}
                        onClick={handleBack}
                    >
                        Back
                    </Button>
                    <Stack direction="row" spacing={2}>
                        <Button
                            variant="contained"
                            startIcon={<EditIcon/>}
                            onClick={handleServerEdit}
                        >
                            Edit
                        </Button>
                        <Button
                            variant="contained"
                            color="error"
                            startIcon={<DeleteIcon/>}
                            onClick={handleServerDelete}
                        >
                            Delete
                        </Button>
                    </Stack>
                </Stack>
            </Box>
        ) : null;
    }, [
        isLoading,
        error,
        server,
        handleBack,
        handleServerEdit,
        handleServerDelete,
    ]);

    const pageTitle = `Server ${serverId}`;

    return (
        <PageContainer
            title={pageTitle}
            breadcrumbs={[
                {title: 'Servers', path: '/server'},
                {title: pageTitle},
            ]}
        >
            <Box sx={{display: 'flex', flex: 1, width: '100%'}}>{renderShow}</Box>
        </PageContainer>
    );
}
