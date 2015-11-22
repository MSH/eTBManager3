
import React from 'react';
import { RouteView } from '../components/router';
import { LOGOUT, TB_SET } from '../core/actions';

/** Pages of the public module */
import Home from './home';


/**
 * Initial page that declare all routes of the module
 */
export default class Routes extends React.Component {

	constructor() {
		super();
		this._onAppChange = this._onAppChange.bind(this);
	}

	componentDidMount() {
		this.props.app.add(this._onAppChange);
	}

	componentDidUmount() {
		this.props.app.remove(this._onAppChange);
	}

	_onAppChange(action) {
		if (action === LOGOUT) {
			this.props.app.dispatch(TB_SET, { toolbarContent: null });
		}
	}

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
