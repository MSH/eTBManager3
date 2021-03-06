
import React from 'react';
import PageContent from '../page-content';

import UserSessions from './reports/usersessions';
import OnlineUsers from './reports/onlineusers';
import CommandHistory from './reports/cmdhistory';
import ErrorLog from './reports/errorlog';

/**
 * Options of the left menu in the reports page
 * @type {Array}
 */
const menu = [
    {
        title: __('admin.websessions'),
        icon: 'users',
        perm: 'ONLINE',
        path: '/onlineusers',
        view: OnlineUsers
    },
    {
        title: __('admin.reports.usersession'),
        icon: 'file-text-o',
        perm: 'USERSESREP',
        path: '/usersessions',
        view: UserSessions
    },
    {
        title: 'Command history',
        icon: 'file-text-o',
        perm: 'CMDHISTORY',
        path: '/cmdhistory',
        view: CommandHistory
    },
    {
        title: 'Command statistics',
        icon: 'file-text-o',
        perm: 'CMDSTATISTICS',
        path: 'cmdstatistics'
    },
    {
        title: 'Error log report',
        icon: 'bug',
        perm: 'ERRORLOGREP',
        path: '/errorlog',
        view: ErrorLog
    }
];

/**
 * Reports page of the administration module
 */
export default class Reports extends React.Component {

    render() {
        return (
            <PageContent route={this.props.route}
                menu={menu}
                title={__('admin') + ' - ' + __('admin.reports')}
                path="/sys/admin/reports" />
        );
    }
}

Reports.propTypes = {
    app: React.PropTypes.object,
    route: React.PropTypes.object
};
