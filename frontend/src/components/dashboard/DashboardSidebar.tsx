import * as React from 'react';
import {useTheme} from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import Toolbar from '@mui/material/Toolbar';
//Resource icons
import BotIcon from '@mui/icons-material/SmartToy';
import SearchIcon from '@mui/icons-material/Search';
import ChannelIcon from '@mui/icons-material/Tag';
import ServerIcon from '@mui/icons-material/Public';
import BarChartIcon from '@mui/icons-material/BarChart';
//Download Icons
import DownloadIcon from '@mui/icons-material/Download';
import RunningIcon from '@mui/icons-material/Download';
import FinishedIcon from '@mui/icons-material/Check';
import CancelledIcon from '@mui/icons-material/Cancel';
import {matchPath, useLocation} from 'react-router';
import DashboardSidebarContext from '../../context/DashboardSidebarContext';
import {DRAWER_WIDTH, MINI_DRAWER_WIDTH} from '../../constants';
import DashboardSidebarPageItem from './sidebar/DashboardSidebarPageItem';
import DashboardSidebarHeaderItem from './sidebar/DashboardSidebarHeaderItem';
import DashboardSidebarDividerItem from './sidebar/DashboardSidebarDividerItem';
import {getDrawerSxTransitionMixin, getDrawerWidthTransitionMixin,} from '../../mixins';

export interface DashboardSidebarProps {
    expanded?: boolean;
    setExpanded: (expanded: boolean) => void;
    disableCollapsibleSidebar?: boolean;
    container?: Element;
}

export default function DashboardSidebar({
                                             expanded = true,
                                             setExpanded,
                                             disableCollapsibleSidebar = false,
                                             container,
                                         }: DashboardSidebarProps) {
    const theme = useTheme();

    const {pathname} = useLocation();

    const [expandedItemIds, setExpandedItemIds] = React.useState<string[]>([]);

    const isOverSmViewport = useMediaQuery(theme.breakpoints.up('sm'));
    const isOverMdViewport = useMediaQuery(theme.breakpoints.up('md'));

    const [isFullyExpanded, setIsFullyExpanded] = React.useState(expanded);
    const [isFullyCollapsed, setIsFullyCollapsed] = React.useState(!expanded);

    React.useEffect(() => {
        if (expanded) {
            const drawerWidthTransitionTimeout = setTimeout(() => {
                setIsFullyExpanded(true);
            }, theme.transitions.duration.enteringScreen);

            return () => clearTimeout(drawerWidthTransitionTimeout);
        }

        setIsFullyExpanded(false);

        return () => {
        };
    }, [expanded, theme.transitions.duration.enteringScreen]);

    React.useEffect(() => {
        if (!expanded) {
            const drawerWidthTransitionTimeout = setTimeout(() => {
                setIsFullyCollapsed(true);
            }, theme.transitions.duration.leavingScreen);

            return () => clearTimeout(drawerWidthTransitionTimeout);
        }

        setIsFullyCollapsed(false);

        return () => {
        };
    }, [expanded, theme.transitions.duration.leavingScreen]);

    const mini = !disableCollapsibleSidebar && !expanded;

    const handleSetSidebarExpanded = React.useCallback(
        (newExpanded: boolean) => () => {
            setExpanded(newExpanded);
        },
        [setExpanded],
    );

    const handlePageItemClick = React.useCallback(
        (itemId: string, hasNestedNavigation: boolean) => {
            if (hasNestedNavigation && !mini) {
                setExpandedItemIds((previousValue) =>
                    previousValue.includes(itemId)
                        ? previousValue.filter(
                            (previousValueItemId) => previousValueItemId !== itemId,
                        )
                        : [...previousValue, itemId],
                );
            } else if (!isOverSmViewport && !hasNestedNavigation) {
                setExpanded(false);
            }
        },
        [mini, setExpanded, isOverSmViewport],
    );

    const hasDrawerTransitions =
        isOverSmViewport && (!disableCollapsibleSidebar || isOverMdViewport);

    const getDrawerContent = React.useCallback(
        (viewport: 'phone' | 'tablet' | 'desktop') => (
            <React.Fragment>
                <Toolbar/>
                <Box
                    component="nav"
                    aria-label={`${viewport.charAt(0).toUpperCase()}${viewport.slice(1)}`}
                    sx={{
                        height: '100%',
                        display: 'flex',
                        flexDirection: 'column',
                        justifyContent: 'space-between',
                        overflow: 'auto',
                        scrollbarGutter: mini ? 'stable' : 'auto',
                        overflowX: 'hidden',
                        pt: !mini ? 0 : 2,
                        ...(hasDrawerTransitions
                            ? getDrawerSxTransitionMixin(isFullyExpanded, 'padding')
                            : {}),
                    }}
                >
                    <List
                        dense
                        sx={{
                            padding: mini ? 0 : 0.5,
                            mb: 4,
                            width: mini ? MINI_DRAWER_WIDTH : 'auto',
                        }}
                    >
                        <DashboardSidebarPageItem
                            id="reports"
                            title="Resources"
                            icon={<BarChartIcon/>}
                            href="/reports"
                            selected={!!matchPath('/reports', pathname)}
                            defaultExpanded={!!matchPath('/downloads', pathname)}
                            expanded={expandedItemIds.includes('reports')}
                            nestedNavigation={
                                <List
                                    dense
                                    sx={{
                                        padding: 0,
                                        my: 1,
                                        pl: mini ? 0 : 1,
                                        minWidth: 240,
                                    }}
                                >
                                    <DashboardSidebarPageItem
                                        id="bots"
                                        title="Bots"
                                        icon={<BotIcon/>}
                                        href="/bot"
                                        selected={!!matchPath('/bot/*', pathname)}
                                    />
                                    <DashboardSidebarPageItem
                                        id="channels"
                                        title="Channels"
                                        icon={<ChannelIcon/>}
                                        href="/channel"
                                        selected={!!matchPath('/channel/*', pathname)}
                                    />
                                    <DashboardSidebarPageItem
                                        id="servers"
                                        title="Servers"
                                        icon={<ServerIcon/>}
                                        href="/server"
                                        selected={!!matchPath('/server/*', pathname)}
                                    />
                                    <DashboardSidebarPageItem
                                        id="search-engines"
                                        title="Search Engines"
                                        icon={<SearchIcon/>}
                                        href="/search-engine"
                                        selected={!!matchPath('/search-engine/*', pathname)}
                                    />
                                </List>
                            }
                        />
                        <DashboardSidebarDividerItem/>
                        <DashboardSidebarHeaderItem>Downloads</DashboardSidebarHeaderItem>
                        <DashboardSidebarPageItem
                            id="downloads"
                            title="All Downloads"
                            icon={<DownloadIcon/>}
                            href="/downloads"
                            selected={!!matchPath('/downloads/', pathname) || pathname === '/'}
                        />
                        <DashboardSidebarPageItem
                            id="active-downloads"
                            title="Active"
                            icon={<RunningIcon/>}
                            href="/downloads/active"
                            selected={!!matchPath('/downloads/active/*', pathname)}
                        />
                        <DashboardSidebarPageItem
                            id="finished-downloads"
                            title="Finished"
                            icon={<FinishedIcon/>}
                            href="/downloads/finished"
                            selected={!!matchPath('/downloads/finished/*', pathname)}
                        />
                        <DashboardSidebarPageItem
                            id="cancelled-downloads"
                            title="Cancelled"
                            icon={<CancelledIcon/>}
                            href="/downloads/cancelled"
                            selected={!!matchPath('/downloads/cancelled/*', pathname)}
                        />
                    </List>
                </Box>
            </React.Fragment>
        ),
        [mini, hasDrawerTransitions, isFullyExpanded, expandedItemIds, pathname],
    );

    const getDrawerSharedSx = React.useCallback(
        (isTemporary: boolean) => {
            const drawerWidth = mini ? MINI_DRAWER_WIDTH : DRAWER_WIDTH;

            return {
                displayPrint: 'none',
                width: drawerWidth,
                flexShrink: 0,
                ...getDrawerWidthTransitionMixin(expanded),
                ...(isTemporary ? {position: 'absolute'} : {}),
                [`& .MuiDrawer-paper`]: {
                    position: 'absolute',
                    width: drawerWidth,
                    boxSizing: 'border-box',
                    backgroundImage: 'none',
                    ...getDrawerWidthTransitionMixin(expanded),
                },
            };
        },
        [expanded, mini],
    );

    const sidebarContextValue = React.useMemo(() => {
        return {
            onPageItemClick: handlePageItemClick,
            mini,
            fullyExpanded: isFullyExpanded,
            fullyCollapsed: isFullyCollapsed,
            hasDrawerTransitions,
        };
    }, [
        handlePageItemClick,
        mini,
        isFullyExpanded,
        isFullyCollapsed,
        hasDrawerTransitions,
    ]);

    return (
        <DashboardSidebarContext.Provider value={sidebarContextValue}>
            <Drawer
                container={container}
                variant="temporary"
                open={expanded}
                onClose={handleSetSidebarExpanded(false)}
                ModalProps={{
                    keepMounted: true, // Better open performance on mobile.
                }}
                sx={{
                    display: {
                        xs: 'block',
                        sm: disableCollapsibleSidebar ? 'block' : 'none',
                        md: 'none',
                    },
                    ...getDrawerSharedSx(true),
                }}
            >
                {getDrawerContent('phone')}
            </Drawer>
            <Drawer
                variant="permanent"
                sx={{
                    display: {
                        xs: 'none',
                        sm: disableCollapsibleSidebar ? 'none' : 'block',
                        md: 'none',
                    },
                    ...getDrawerSharedSx(false),
                }}
            >
                {getDrawerContent('tablet')}
            </Drawer>
            <Drawer
                variant="permanent"
                sx={{
                    display: {xs: 'none', md: 'block'},
                    ...getDrawerSharedSx(false),
                }}
            >
                {getDrawerContent('desktop')}
            </Drawer>
        </DashboardSidebarContext.Provider>
    );
}
