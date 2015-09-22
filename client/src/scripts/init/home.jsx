'use strict';

import React from 'react';
import { RouteView } from '../components/router.jsx';
import Welcome from './welcome.jsx';


/**
 * The home page of the initialization module
 */
export default class Home extends React.Component {

	render() {
		let routes = [
			{ path: '/welcome', view: Welcome }
		];

		let viewProps = {
			dispatch: this.props.dispatch,
			appState: this.props.appState
		};

		return (
			<div>
	            <RouteView routes={routes} viewProps={viewProps}>
	            </RouteView>
			</div>
			);
	}
}