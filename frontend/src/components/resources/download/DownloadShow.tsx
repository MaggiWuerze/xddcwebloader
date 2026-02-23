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
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {useNavigate, useParams} from 'react-router';
import {useDialogs} from '../../../hooks/useDialogs/useDialogs';
import useNotifications from '../../../hooks/useNotifications/useNotifications';
import {DownloadRepository} from '../../../data/downloadRepository';
import PageContainer from '../../pagecontainer/PageContainer';
import {DownloadTO} from "../../../api/rest";
import CancelledIcon from "@mui/icons-material/Cancel";
import {downloadUpdatesClient} from "../../../api/stomp/DownloadUpdatesClient";

export default function DownloadShow() {
    const {downloadId} = useParams();
    const navigate = useNavigate();

    const dialogs = useDialogs();
    const notifications = useNotifications();

    const [download, setDownload] = React.useState<DownloadTO | null>(null);
    const [isLoading, setIsLoading] = React.useState(true);
    const [error, setError] = React.useState<Error | null>(null);

    const loadData = React.useCallback(async () => {
        if (!downloadId) return
        setError(null);
        setIsLoading(true);

        try {
            const data = await DownloadRepository.get(downloadId);
            setDownload(data);
        } catch (showDataError) {
            setError(showDataError as Error);
        }
        setIsLoading(false);
    }, [downloadId]);

    React.useEffect(() => {
        loadData();
    }, [loadData]);

    const handleEmployeeEdit = React.useCallback(() => {
        navigate(`/download/${downloadId}/edit`);
    }, [navigate, downloadId]);

    const handleBack = React.useCallback(() => {
        navigate('/download');
    }, [navigate]);

    React.useEffect(() => {
        if (!downloadId) return;

        const sub = downloadUpdatesClient.subscribeDownloadEvents(`download-show-${downloadId}`, (evt) => {
            const payloadId = evt.payload?.id;
            if (!payloadId || payloadId !== downloadId) return;

            if (evt.type === 'delete') {
                navigate('/download');
                return;
            }

            setDownload(prev => (prev ? ({...prev, ...evt.payload} as DownloadTO) : prev));
        });

        return () => sub.unsubscribe();
    }, [downloadId, navigate]);

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

        return download ? (
            <Box sx={{flexGrow: 1, width: '100%'}}>
                <Grid container spacing={2} sx={{width: '100%'}}>
                    <Grid size={{xs: 12, sm: 6}}>
                        <Paper sx={{px: 2, py: 1}}>
                            <Typography variant="overline">File Name</Typography>
                            <Typography variant="body1" sx={{mb: 1}}>
                                {download.filename}
                            </Typography>
                        </Paper>
                    </Grid>
                    <Grid size={{xs: 12, sm: 6}}>
                        <Paper sx={{px: 2, py: 1}}>
                            <Typography variant="overline">File Size</Typography>
                            <Typography variant="body1" sx={{mb: 1}}>
                                {download.filesize}
                            </Typography>
                        </Paper>
                    </Grid>
                    <Grid size={{xs: 12, sm: 6}}>
                        <Paper sx={{px: 2, py: 1}}>
                            <Typography variant="overline">Status</Typography>
                            <Typography variant="body1" sx={{mb: 1}}>
                                {download.status}
                            </Typography>
                        </Paper>
                    </Grid>
                    <Grid size={{xs: 12, sm: 6}}>
                        <Paper sx={{px: 2, py: 1}}>
                            <Typography variant="overline">Avergae Speed</Typography>
                            <Typography variant="body1" sx={{mb: 1}}>
                                {download.averageSpeed}
                            </Typography>
                        </Paper>
                    </Grid>
                    <Grid size={{xs: 12, sm: 6}}>
                        <Paper sx={{px: 2, py: 1}}>
                            <Typography variant="overline">Time Remaining</Typography>
                            <Typography variant="body1" sx={{mb: 1}}>
                                {download.timeRemaining}
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
                            startIcon={<CancelledIcon/>}
                            onClick={handleEmployeeEdit}
                        >
                            Edit
                        </Button>
                    </Stack>
                </Stack>
            </Box>
        ) : null;
    }, [
        isLoading,
        error,
        download,
        handleBack,
        handleEmployeeEdit,
    ]);

    const pageTitle = `Employee ${downloadId}`;

    return (
        <PageContainer
            title={pageTitle}
            breadcrumbs={[
                {title: 'Employees', path: '/download'},
                {title: pageTitle},
            ]}
        >
            <Box sx={{display: 'flex', flex: 1, width: '100%'}}>{renderShow}</Box>
        </PageContainer>
    );
}
