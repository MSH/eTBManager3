
import React from 'react';
import { RouteView } from '../components/router.jsx';

/** Pages of the public module */
import Login from './login.jsx';
import UserReg from './user-reg.jsx';
import ForgotPwd from './forgot-pwd.jsx';
import ResetPwd from './reset-pwd.jsx';


/**
 * The page controller of the public module
 */
export default class Routes extends React.Component {

	render() {
		const routes = RouteView.createRoutes([
			{ path: '/login', view: Login },
            { path: '/forgotpwd', view: ForgotPwd },
            { path: '/userreg', view: UserReg },
            { path: '/resetpwd/{id}', view: ResetPwd }
		]);

		return <RouteView routes={routes} />;
	}
}
