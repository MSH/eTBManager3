
import React from 'react';
import { RouteView } from '../../components/router';

/** Pages of the public module */
import Index from './index';


/**
 * Initial page that declare all routes of the module
 */
export default class HomeRoutes extends React.Component {

	render() {
		const routes = RouteView.createRoutes([
			{ path: '/index', view: Index, title: __('home'), default: true }
		]);

		return (
			<RouteView routes={routes} />
			);
	}
}
