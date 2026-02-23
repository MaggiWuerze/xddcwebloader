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
import {dataGridCustomizations, formInputCustomizations, sidebarCustomizations,} from './theme/customizations';

const router = createHashRouter([
    {
        Component: DashboardLayout,
        children: [
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
            // Fallback route for the example routes in dashboard sidebar items
            {
                path: '*',
                Component: BotList,
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
