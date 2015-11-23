
import React from 'react';
import { RouteView } from '../components/router';
import { LOGOUT, TB_SET } from '../core/actions';
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
		if (action === LOGOUT) {
			app.dispatch(TB_SET, { toolbarContent: null });
		}
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
		const routes = [
			{ path: '/home', viewResolver: this.openHome.bind(this) },
			{ path: '/reports', viewResolver: this.openReports.bind(this) },
			{ path: '/admin', viewResolver: this.openAdmin.bind(this) }
		];

		return (
			<RouteView routes={routes} />
			);
	}
}
