import React from 'react';
import SidebarContent from '../../sidebar-content';

import Dashboard from './general-dashboard';
import Activities from './general-activities';


const menu = [
    {
        title: 'Dashboard',
        path: '/dashboard',
        icon: 'dashboard',
        view: Dashboard,
        default: true
    },
    {
        title: 'Activities',
        path: 'activities',
        icon: 'flash',
        view: Activities
    }
];

export default class UnitGeneral extends React.Component {

    render() {
        return (
            <SidebarContent menu={menu} path="/sys/home/unit/general" route={this.props.route} />
            );
    }
}

UnitGeneral.propTypes = {
    route: React.PropTypes.object
};
