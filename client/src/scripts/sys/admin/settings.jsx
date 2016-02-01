
import React from 'react';
import PageContent from './page-content';
import { app } from '../../core/app';


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
				path: '/workspaces'
			},
			{
				title: __('admin.syssetup'),
				perm: 'SYSSETUP',
				icon: 'wrench',
				path: '/syssetup'
			},
			{
				title: __('admin.setup') + ' ' + app.getState().session.workspaceName,
				perm: 'WORKSPACE_EDT',
				icon: 'cog',
				path: '/wssetup'
			}
		];
	}

	render() {
		return (
			<PageContent route={this.props.route}
				menu={this.menu}
				title={__('admin.config')}
				path="/sys/admin/tables" />
			);
	}
}

Settings.propTypes = {
	route: React.PropTypes.object
};

