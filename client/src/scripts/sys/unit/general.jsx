import React from 'react';
import PageContent from '../page-content';

import DashboardView from '../packages/dashboard/dashboard-view';
// import Activities from './general-activities';


const menu = [
    {
        title: 'Dashboard',
        path: '/dashboard',
        icon: 'dashboard',
        view: DashboardView,
        default: true
    // },
    // {
    //     title: 'Activities',
    //     path: 'activities',
    //     icon: 'flash',
    //     view: Activities
    }
];

export default class UnitGeneral extends React.Component {

    render() {
        return (
            <PageContent
                route={this.props.route}
                menu={menu}
                path={this.props.route.path}
                viewProps={{ scope: 'UNIT', scopeId: this.props.scopeId }} />
        );
    }
}

UnitGeneral.propTypes = {
    scopeId: React.PropTypes.string,
    route: React.PropTypes.object
};
