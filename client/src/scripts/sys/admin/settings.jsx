
import React from 'react';
import PageContent from './page-content';

import SysSetup from './settings/sys-setup';
import Workspaces from './settings/workspaces';

/**
 * Settings page of the administration module
 */
export default class Settings extends React.Component {

	constructor(props) {
		super(props);
		this.menu = [
			{
				title: __('admin.workspaces'),
				perm: 'WORKSPACES',
				icon: 'globe',
				path: '/workspaces',
				view: Workspaces
			},
			{
				title: __('admin.syssetup'),
				perm: 'SYSSETUP',
				icon: 'wrench',
				path: '/syssetup',
				view: SysSetup
			}
		];
	}

	render() {
		return (
			<PageContent route={this.props.route}
				menu={this.menu}
				title={__('admin.config')}
				path="/sys/admin/settings" />
			);
	}
}

Settings.propTypes = {
	route: React.PropTypes.object
};

