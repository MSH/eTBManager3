import React from 'react';
import PageContent from '../page-content';
import DashboardView from '../packages/dashboard/dashboard-view';
import UnitsCard from '../packages/others/units-card';


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
    {
        title: __('admin.tbunits'),
        icon: 'hospital-o',
        path: '/units',
        view: UnitsCard
    }
];

/**
 * General sub-view of the administrative unit page
 */
export default class General extends React.Component {

    render() {
        return (
        <PageContent
            route={this.props.route}
            menu={menu}
            path={this.props.route.path}
            viewProps={{ scope: 'ADMINUNIT', scopeId: this.props.scopeId }}
            queryParams={{ id: this.props.scopeId }} />
        );
    }
}

General.propTypes = {
    scopeId: React.PropTypes.string.isRequired,
    route: React.PropTypes.object
};
