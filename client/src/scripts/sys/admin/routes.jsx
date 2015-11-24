
import React from 'react';
import { RouteView } from '../../components/router';

/** Pages of the public module */
import Tables from './tables';


/**
 * Initial page that declare all routes of the module
 */
export default class AdminRoutes extends React.Component {

	render() {
		const routes = [
			{ path: '/tables', view: Tables }
		];

		return (
			<RouteView routes={routes} />
			);
	}
}
