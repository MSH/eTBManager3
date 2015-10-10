'use strict';

import React from 'react';
import { RouteView } from '../components/router.jsx';

/** Pages of the public module */
import Home from './home.jsx';


/**
 * The page controller of the public module
 */
export default class Routes extends React.Component {

	render() {
		let routes = [
			{ path: '/home', view: Home }
		];

		let viewProps = {
			dispatch: this.props.dispatch,
			appState: this.props.appState
		};

		return (
            <RouteView routes={routes} viewProps={viewProps}>
            </RouteView>
			);
	}
}