
import React from 'react';
import { RouteView } from '../../components/router';

/** Pages of the public module */
import Index from './index';
import Unit from './unit';

import NewNotif from './cases/newnotif';
import Details from './cases/details';


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
			},
			{
				path: '/cases/newnotif',
				view: NewNotif,
				title: __('cases.newnotif')
			},
			{
				path: '/cases/details/{id}',
				view: Details,
				title: __('cases.details')
			}
		]);

		return (
			<RouteView routes={routes} />
			);
	}
}
