
import React from 'react';
import { RouteView } from '../components/router';
import { WaitIcon, observer } from '../components';
import { WORKSPACE_CHANGING, WORKSPACE_CHANGE } from '../core/actions';
import LanguageSel from './user/language-sel';
import WorkspaceSel from './user/workspace-sel';
import Toolbar from './toolbar';

/** Pages of the system module */
import AdminRoutes from './admin/routes';

import WorkspaceView from './workspace';
import AdminUnitView from './adminunit';
import UnitView from './unit';
import CaseView from './case';
import CaseNewView from './case/new';

import Settings from './user/settings';
import ChangePassword from './user/change-password.jsx';

import Sync from './sync';

import Start from './start';

/**
 * Initial page that declare all routes of the module
 */
class Routes extends React.Component {

    handleEvent(action) {
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
            { path: '/adminunit', view: AdminUnitView },
            { path: '/admin', view: AdminRoutes },
            { path: '/workspace', view: WorkspaceView },
            { path: '/unit', view: UnitView },
            { path: '/case/new', view: CaseNewView },
            { path: '/case', view: CaseView },
            { path: '/user/settings', view: Settings },
            { path: '/user/changepassword', view: ChangePassword },
            { path: '/sync', view: Sync },
            { path: '/start', view: Start }
        ];

        // playground for dev is available just in dev module
        if (__DEV__) {
            const DevIndex = require('./dev/index');
            routesInfo.push({ path: '/dev', view: DevIndex.default, title: 'Developers playground' });
        }

        const routes = RouteView.createRoutes(routesInfo);

        return (
            <div>
                <Toolbar />
                <div className="tb-margin">
                    <RouteView id="routes-index" routes={routes} />
                </div>
                <LanguageSel />
                <WorkspaceSel />
            </div>
            );
    }
}

export default observer(Routes);
