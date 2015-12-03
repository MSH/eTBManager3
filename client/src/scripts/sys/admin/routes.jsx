
import React from 'react';
import { RouteView } from '../../components/router';

/** Pages of the public module */
import Tables from './tables';
import Reports from './reports';
import Settings from './settings';


/**
 * Initial page that declare all routes of the module
 */
export default class AdminRoutes extends React.Component {

	render() {
		const routes = RouteView.createRoutes([
            {
				path: '/tables',
				title: __('admin.tables'),
				view: Tables
            },
			{
				path: '/settings',
				title: __('admin.config'),
				view: Settings
			},
			{
				path: '/reports',
				title: __('admin') + ' - ' + __('admin.reports'),
				view: Reports
			}
		]);

		return (
			<RouteView routes={routes} />
			);
	}
}
