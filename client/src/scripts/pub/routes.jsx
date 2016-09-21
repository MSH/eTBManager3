
import React from 'react';
import { RouteView } from '../components/router';

/** Pages of the public module */
import Login from './login';
import SelfReg from './self-reg';
import ForgotPwd from './forgot-pwd';
import ResetPwd from './reset-pwd';
import ConfirmEmail from './confirm-email';


/**
 * The page controller of the public module
 */
export default class Routes extends React.Component {

    render() {
        const routes = RouteView.createRoutes([
            { path: '/login', view: Login },
            { path: '/forgotpwd', view: ForgotPwd },
            { path: '/selfreg', view: SelfReg },
            { path: '/resetpwd/{id}', view: ResetPwd },
            { path: '/confirmemail/{id}', view: ConfirmEmail }
        ]);

        return <RouteView routes={routes} />;
    }
}
