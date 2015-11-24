
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
		const routes = [
			{ path: '/tables', view: Tables },
			{ path: '/settings', view: Settings },
			{ path: '/reports', view: Reports }
		];

		return (
			<RouteView routes={routes} />
			);
	}
}
