import React from 'react';
import PageContent from '../page-content';
import WorkspaceSettings from './general-settings';
import UnitsCard from '../packages/others/units-card';
import DashboardView from '../packages/dashboard/dashboard-view';


/**
 * List of all items displayed in the left side box
 */
const menu = [
    {
        title: __('Dashboard'),
        icon: 'dashboard',
        path: '/dashboard',
        view: DashboardView,
        default: true
    },
/*
    {
        title: __('admin.users'),
        icon: 'user',
        path: '/users',
        view: UnderConstruction
    },
*/
    {
        title: __('admin.tbunits'),
        icon: 'hospital-o',
        path: '/units',
        view: UnitsCard
    },
    {
        title: __('admin.config'),
        icon: 'cogs',
        path: '/settings',
        view: WorkspaceSettings
    }
];

export default class General extends React.Component {

    render() {
        return (
            <PageContent
                route={this.props.route}
                menu={menu}
                path={this.props.route.path}
                viewProps={{ scope: 'WORKSPACE' }} />
        );
    }
}

General.propTypes = {
    route: React.PropTypes.object
};
