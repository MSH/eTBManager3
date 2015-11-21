
import React from 'react';
import { RouteView } from '../components/router.jsx';

import Welcome from './welcome.jsx';
import InitOptions from './initoptions.jsx';
import NewWorkspace from './newworkspace.jsx';


/**
 * The home page of the initialization module
 */
export default class Home extends React.Component {

	render() {
		const routes = [
			{ path: '/welcome', view: Welcome },
            { path: '/initoptions', view: InitOptions },
            { path: '/newworkspace', view: NewWorkspace }
		];

		const viewProps = {
			app: this.props.app
		};

		return (
			<div>
				<RouteView routes={routes} viewProps={viewProps} />
			</div>
			);
	}
}

Home.propTypes = {
    app: React.PropTypes.object
};
