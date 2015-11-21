
import React from 'react';
import { RouteView } from '../components/router.jsx';

/** Pages of the public module */
import Home from './home.jsx';


/**
 * Initial page that declare all routes of the module
 */
export default class Routes extends React.Component {

	render() {
		const routes = [
			{ path: '/home', view: Home }
		];

		const viewProps = {
			app: this.props.app
		};

		return (
            <RouteView routes={routes} viewProps={viewProps} />
			);
	}
}

Routes.propTypes = {
	app: React.PropTypes.object
};
