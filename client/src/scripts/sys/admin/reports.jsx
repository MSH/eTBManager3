
import React from 'react';
import PageContent from './page-content';

import ReportsSession from './reports/sessionreport';
import OnlineUsers from './reports/onlineusers';

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
		path: '/reportssession',
		view: ReportsSession
	},
	{
		title: 'Command history',
		icon: 'file-text-o',
		perm: 'CMDHISTORY',
		path: 'cmdhistory'
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
		path: 'errorlogrep'
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
				title={__('admin.reports')}
				path="/sys/admin/reports" />
			);
	}
}

Reports.propTypes = {
	app: React.PropTypes.object,
	route: React.PropTypes.object
};
