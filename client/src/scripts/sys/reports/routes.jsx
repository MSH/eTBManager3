
import React from 'react';
import { RouteView } from '../../components/router';

/** Pages of the module */
import Index from './index';


/**
 * Initial page that declare all routes of the module
 */
export default class ReportRoutes extends React.Component {

	render() {
		const routes = [
			{ path: '/index', view: Index }
		];

		const viewProps = {
			app: this.props.app
		};

		return (
			<RouteView routes={routes} viewProps={viewProps} />
			);
	}
}

ReportRoutes.propTypes = {
	app: React.PropTypes.object
};
