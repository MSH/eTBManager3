
import React from 'react';
import { RouteView } from '../../components/router';

/** Pages of the public module */
import Index from './index';
import DatePickerExamples from './date-picker-examples';


/**
 * Initial page that declare all routes of the module
 */
export default class TempRoutes extends React.Component {

	render() {
		const routes = RouteView.createRoutes([
			{
				path: '/index',
				view: Index,
				title: 'Developer playground'
			},
			{
				path: '/datepicker',
				view: DatePickerExamples,
				title: 'Date picker examples'
			}
		]);

		return (
			<RouteView routes={routes} />
			);
	}
}
