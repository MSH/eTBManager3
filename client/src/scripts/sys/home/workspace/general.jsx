import React from 'react';
import UnderConstruction from '../under-construction';
import PageContent from '../../page-content';
import WorkspaceSettings from './general-settings';

/**
 * List of all items displayed in the left side box
 */
const menu = [
    {
        title: __('Dashboard'),
        icon: 'dashboard',
        path: '/dashboard',
        view: UnderConstruction
    },
    {
        title: __('admin.users'),
        icon: 'user',
        path: '/users',
        view: UnderConstruction
    },
    {
        title: __('admin.units'),
        icon: 'hospital-o',
        path: '/units',
        view: UnderConstruction
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
            path="/sys/home/workspace/general" />
        );
    }
}

General.propTypes = {
    route: React.PropTypes.object
};
