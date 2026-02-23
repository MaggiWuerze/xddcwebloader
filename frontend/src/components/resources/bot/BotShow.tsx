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
import PageContainer from '../../pagecontainer/PageContainer';
import {BotRepository as repository} from '../../../data/botRepository';
import {BotTO} from "../../../api/rest";

export default function BotShow() {
    const {botId} = useParams();
    const navigate = useNavigate();

    const dialogs = useDialogs();
    const notifications = useNotifications();

    const [bot, setBot] = React.useState<BotTO | null>(null);
    const [isLoading, setIsLoading] = React.useState(true);
    const [error, setError] = React.useState<Error | null>(null);

    const loadData = React.useCallback(async () => {
        setError(null);
        setIsLoading(true);
        if (botId === undefined) return;
        try {
            const showData = await repository.get(botId);
            console.log(showData);
            if (showData === undefined) return;
            setBot(showData);
        } catch (showDataError) {
            console.error(showDataError);
            setError(showDataError as Error);
        }
        console.log(bot);
        setIsLoading(false);
    }, [botId]);

    React.useEffect(() => {
        loadData();
    }, [loadData]);

    const handleBotEdit = React.useCallback(() => {
        navigate(`/bot/${botId}/edit`);
    }, [navigate, botId]);

    const handleBotDelete = React.useCallback(async () => {
        if (!bot) {
            return;
        }

        const confirmed = await dialogs.confirm(
            `Do you wish to delete ${bot.name}?`,
            {
                title: `Delete Bot?`,
                severity: 'error',
                okText: 'Delete',
                cancelText: 'Cancel',
            },
        );

        if (confirmed) {
            setIsLoading(true);
            try {
                if (botId === undefined) return;
                await repository.delete(botId);

                navigate('/bot');

                notifications.show('Bot deleted successfully.', {
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
    }, [bot, dialogs, botId, navigate, notifications]);

    const handleBack = React.useCallback(() => {
        navigate('/bot');
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

        return bot ? (
            <Box sx={{flexGrow: 1, width: '100%'}}>
                <Grid container spacing={2} sx={{width: '100%'}}>
                    <Grid size={{xs: 12, sm: 6}}>
                        <Paper sx={{px: 2, py: 1}}>
                            <Typography variant="overline">Name</Typography>
                            <Typography variant="body1" sx={{mb: 1}}>
                                {bot.name}
                            </Typography>
                        </Paper>
                    </Grid>
                    <Grid size={{xs: 12, sm: 6}}>
                        <Paper sx={{px: 2, py: 1}}>
                            <Typography variant="overline">Age</Typography>
                            <Typography variant="body1" sx={{mb: 1}}>
                                {bot.channel?.name}
                            </Typography>
                        </Paper>
                    </Grid>
                    <Grid size={{xs: 12, sm: 6}}>
                        <Paper sx={{px: 2, py: 1}}>
                            <Typography variant="overline">Department</Typography>
                            <Typography variant="body1" sx={{mb: 1}}>
                                {bot.server?.name}
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
                            onClick={handleBotEdit}
                        >
                            Edit
                        </Button>
                        <Button
                            variant="contained"
                            color="error"
                            startIcon={<DeleteIcon/>}
                            onClick={handleBotDelete}
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
        bot,
        handleBack,
        handleBotEdit,
        handleBotDelete,
    ]);

    const pageTitle = `Bot ${botId}`;

    return (
        <PageContainer
            title={pageTitle}
            breadcrumbs={[
                {title: 'Bots', path: '/bot'},
                {title: pageTitle},
            ]}
        >
            <Box sx={{display: 'flex', flex: 1, width: '100%'}}>{renderShow}</Box>
        </PageContainer>
    );
}
