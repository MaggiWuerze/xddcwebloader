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
import {ChannelRepository as repository} from "../../../data/channelRepository";
import PageContainer from '../../pagecontainer/PageContainer';
import {ChannelTO} from "../../../api/rest";

export default function ChannelShow() {
    const {employeeId} = useParams();
    const navigate = useNavigate();

    const dialogs = useDialogs();
    const notifications = useNotifications();

    const [channel, setChannel] = React.useState<ChannelTO | null>(null);
    const [isLoading, setIsLoading] = React.useState(true);
    const [error, setError] = React.useState<Error | null>(null);

    const loadData = React.useCallback(async () => {
        if (!employeeId) return
        setError(null);
        setIsLoading(true);

        try {
            const showData = await repository.get(employeeId);

            setChannel(showData);
        } catch (showDataError) {
            setError(showDataError as Error);
        }
        setIsLoading(false);
    }, [employeeId]);

    React.useEffect(() => {
        loadData();
    }, [loadData]);

    const handleEmployeeEdit = React.useCallback(() => {
        navigate(`/employees/${employeeId}/edit`);
    }, [navigate, employeeId]);

    const handleEmployeeDelete = React.useCallback(async () => {
        if (!channel || !employeeId) {
            return;
        }

        const confirmed = await dialogs.confirm(
            `Do you wish to delete ${channel.name}?`,
            {
                title: `Delete employee?`,
                severity: 'error',
                okText: 'Delete',
                cancelText: 'Cancel',
            },
        );

        if (confirmed) {
            setIsLoading(true);
            try {
                await repository.delete(employeeId);

                navigate('/employees');

                notifications.show('Employee deleted successfully.', {
                    severity: 'success',
                    autoHideDuration: 3000,
                });
            } catch (deleteError) {
                notifications.show(
                    `Failed to delete employee. Reason:' ${(deleteError as Error).message}`,
                    {
                        severity: 'error',
                        autoHideDuration: 3000,
                    },
                );
            }
            setIsLoading(false);
        }
    }, [channel, dialogs, employeeId, navigate, notifications]);

    const handleBack = React.useCallback(() => {
        navigate('/employees');
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

        return channel ? (
            <Box sx={{flexGrow: 1, width: '100%'}}>
                <Grid container spacing={2} sx={{width: '100%'}}>
                    <Grid size={{xs: 12, sm: 6}}>
                        <Paper sx={{px: 2, py: 1}}>
                            <Typography variant="overline">Name</Typography>
                            <Typography variant="body1" sx={{mb: 1}}>
                                {channel.name}
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
                            onClick={handleEmployeeEdit}
                        >
                            Edit
                        </Button>
                        <Button
                            variant="contained"
                            color="error"
                            startIcon={<DeleteIcon/>}
                            onClick={handleEmployeeDelete}
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
        channel,
        handleBack,
        handleEmployeeEdit,
        handleEmployeeDelete,
    ]);

    const pageTitle = `Employee ${employeeId}`;

    return (
        <PageContainer
            title={pageTitle}
            breadcrumbs={[
                {title: 'Employees', path: '/employees'},
                {title: pageTitle},
            ]}
        >
            <Box sx={{display: 'flex', flex: 1, width: '100%'}}>{renderShow}</Box>
        </PageContainer>
    );
}
