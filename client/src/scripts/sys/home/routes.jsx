
import React from 'react';
import { RouteView } from '../../components/router';

/** Pages of the public module */
import Index from './index';
import Unit from './unit';


/**
 * Initial page that declare all routes of the module
 */
export default class HomeRoutes extends React.Component {

	render() {
		const routes = RouteView.createRoutes([
			{
				path: '/index',
				view: Index,
				title: __('home'),
				default: true
			},
			{
				path: '/unit',
				view: Unit,
				title: 'Unit'
			}
		]);

		return (
			<RouteView routes={routes} />
			);
	}
}
