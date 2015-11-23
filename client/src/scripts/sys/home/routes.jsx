
import React from 'react';
import { RouteView } from '../../components/router';

/** Pages of the public module */
import Index from './index';


/**
 * Initial page that declare all routes of the module
 */
export default class HomeRoutes extends React.Component {

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

HomeRoutes.propTypes = {
	app: React.PropTypes.object
};
