
import React from 'react';
import { RouteView } from '../components/router';
import WaitIcon from '../components/wait-icon';
import { WORKSPACE_CHANGING, WORKSPACE_CHANGE } from '../core/actions';
import { app } from '../core/app';

/** Pages of the public module */
import HomeRoutes from './home/routes';
import ReportRoutes from './reports/routes';
import AdminRoutes from './admin/routes';
import UserSettings from './others/user-settings';


/**
 * Initial page that declare all routes of the module
 */
export default class Routes extends React.Component {

	constructor() {
		super();
		this._onAppChange = this._onAppChange.bind(this);
	}

	componentDidMount() {
		app.add(this._onAppChange);
	}

	componentDidUmount() {
		app.remove(this._onAppChange);
	}

	_onAppChange(action) {
		if (action === WORKSPACE_CHANGING) {
			this.setState({ changing: true });
			return;
		}

		if (action === WORKSPACE_CHANGE) {
			this.setState({ changing: false });
			return;
		}
	}

	render() {

		const changing = this.state && this.state.changing;
		if (changing) {
			return <WaitIcon />;
		}

		const routesInfo = [
			{ path: '/home', view: HomeRoutes },
			{ path: '/reports', view: ReportRoutes },
			{ path: '/admin', view: AdminRoutes },
			{ path: '/usersettings', view: UserSettings }
			];

		// playground for dev is available just in dev module
		if (__DEV__) {
			const DevIndex = require('./dev/index');
			routesInfo.push({ path: '/dev', view: DevIndex.default, title: 'Developers playground' });
		}

		const routes = RouteView.createRoutes(routesInfo);

		return (
			<RouteView id="routes-index" routes={routes} />
			);
	}
}
