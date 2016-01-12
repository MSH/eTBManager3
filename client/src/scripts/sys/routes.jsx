
import React from 'react';
import { RouteView } from '../components/router';
import WaitIcon from '../components/wait-icon';
import { WORKSPACE_CHANGING, WORKSPACE_CHANGE } from '../core/actions';
import { app } from '../core/app';

/** Pages of the public module */
import HomeRoutes from './home/routes';
import ReportRoutes from './reports/routes';
import AdminRoutes from './admin/routes';


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
			return this.setState({ changing: true });
		}

		if (action === WORKSPACE_CHANGE) {
			return this.setState({ changing: false });
		}

		// if (action === LOGOUT) {
		// 	return app.dispatch(TB_SET, { toolbarContent: null });
		// }
	}

	openHome() {
		return HomeRoutes;
	}

	openReports() {
		return ReportRoutes;
	}

	openAdmin() {
		return AdminRoutes;
	}

	render() {
		const changing = this.state && this.state.changing;
		if (changing) {
			return <WaitIcon />;
		}

		const routes = RouteView.createRoutes([
			{ path: '/home', viewResolver: this.openHome.bind(this) },
			{ path: '/reports', viewResolver: this.openReports.bind(this) },
			{ path: '/admin', viewResolver: this.openAdmin.bind(this) }
		]);

		return (
			<RouteView id="routes-index" routes={routes} />
			);
	}
}
