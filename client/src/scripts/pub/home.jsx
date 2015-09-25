'use strict';

import React from 'react';
import { RouteView } from '../components/router.jsx';


/**
 * The page controller of the public module
 */
export default class Home extends React.Component {

	render() {
		//let routes = [
		//	{ path: '/login', view: Welcome },
         //   { path: '/forgotpwd', view: InitOptions},
         //   { path: '/userreg', view: NewWorkspace }
		//];

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