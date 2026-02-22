import CssBaseline from '@mui/material/CssBaseline';
import {createHashRouter, RouterProvider} from 'react-router';
import DashboardLayout from './components/dashboard/DashboardLayout';
import BotList from './components/resources/bot/BotList';
import BotShow from './components/resources/bot/BotShow';
import BotCreate from './components/resources/bot/BotCreate';
import BotEdit from './components/resources/bot/BotEdit';
import NotificationsProvider from './hooks/useNotifications/NotificationsProvider';
import DialogsProvider from './hooks/useDialogs/DialogsProvider';
import AppTheme from './theme/AppTheme';
import {dataGridCustomizations, formInputCustomizations, sidebarCustomizations,} from './theme/customizations/';
import DownloadList from "./components/resources/download/DownloadList";
import ChannelList from "./components/resources/channel/ChannelList";
import ChannelShow from "./components/resources/channel/ChannelShow";
import ChannelCreate from "./components/resources/channel/ChannelCreate";
import ChannelEdit from "./components/resources/channel/ChannelEdit";
import ServerEdit from "./components/resources/server/ServerEdit";
import ServerList from "./components/resources/server/ServerList";
import ServerShow from "./components/resources/server/ServerShow";
import ServerCreate from "./components/resources/server/ServerCreate";
import DownloadCreate from "./components/resources/download/DownloadCreate";
import SearchList from "./components/resources/search/SearchList";

const router = createHashRouter([
    {
        Component: DashboardLayout,
        children: [
            //server routes
            {
                path: '/server',
                Component: ServerList,
            },
            {
                path: '/server/:serverId',
                Component: ServerShow,
            },
            {
                path: '/server/new',
                Component: ServerCreate,
            },
            {
                path: '/server/:serverId/edit',
                Component: ServerEdit,
            },
            //channel routes
            {
                path: '/channel',
                Component: ChannelList,
            },
            {
                path: '/channel/:channelId',
                Component: ChannelShow,
            },
            {
                path: '/channel/new',
                Component: ChannelCreate,
            },
            {
                path: '/channel/:channelId/edit',
                Component: ChannelEdit,
            },
            //bot routes
            {
                path: '/bot',
                Component: BotList,
            },
            {
                path: '/bot/:botId',
                Component: BotShow,
            },
            {
                path: '/bot/new',
                Component: BotCreate,
            },
            {
                path: '/bot/:botId/edit',
                Component: BotEdit,
            },
            //download routes
            {
                path: '/downloads/active',
                Component: DownloadList,
            },
            {
                path: '/downloads/finished',
                Component: DownloadList,
            },
            {
                path: '/downloads/cancelled',
                Component: DownloadList,
            },
            {
                path: '/downloads/new',
                Component: DownloadCreate,
            },
            //Search routes
            {
                path: '/search',
                Component: SearchList,
            },
            // Fallback route for the example routes in dashboard sidebar items
            {
                path: '*',
                Component: DownloadList,
            },
        ],
    },
]);

const themeComponents = {
    ...dataGridCustomizations,
    ...sidebarCustomizations,
    ...formInputCustomizations,
};

export default function CrudDashboard(props: { disableCustomTheme?: boolean }) {
    return (
        <AppTheme {...props} themeComponents={themeComponents}>
            <CssBaseline enableColorScheme/>
            <NotificationsProvider>
                <DialogsProvider>
                    <RouterProvider router={router}/>
                </DialogsProvider>
            </NotificationsProvider>
        </AppTheme>
    );
}