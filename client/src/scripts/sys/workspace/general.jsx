import React from 'react';
import UnderConstruction from '../under-construction';
import PageContent from '../page-content';
import WorkspaceSettings from './general-settings';
import WokspaceTbUnits from './general-tbunits';

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
        view: WokspaceTbUnits
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
            path={this.props.route.path} />
        );
    }
}

General.propTypes = {
    route: React.PropTypes.object
};
