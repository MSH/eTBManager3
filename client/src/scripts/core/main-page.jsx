
import React from 'react';
import { RouteView } from '../components/router';
import PageNotFound from './page-not-found';

import Footer from './footer';
import WaitIcon from '../components/wait-icon';
import ErrorView from './error-view';
import AppMessageDlg from './app-message-dlg';

/**
 * Modules that are part of the system
 */
import { init as initInit } from '../init/index';
import { init as initPub } from '../pub/index';
import { init as initSys } from '../sys/index';


/**
 * The root content of the application
 *
 **/
export default class MainPage extends React.Component {

    /**
     * Open the public module
     */
    openPublic(path) {
        return initPub(path);
    }

    /**
     * Open the initialization module
     */
    openInit(path) {
        return initInit(path);
    }

    /**
     * Open the application home page
     */
    openApp(path) {
        return initSys(path);
    }

    render() {
        var routers = RouteView.createRoutes([
            { path: '/pub', viewResolver: this.openPublic },
            { path: '/init', viewResolver: this.openInit },
            { path: '/sys', viewResolver: this.openApp }
        ]);

        return (
            <div>
                <div className="app-content">
                    <RouteView key={1}
                        routes={routers}
                        loadingView={<WaitIcon/>}
                        pageNotFoundView={PageNotFound} />

                </div>
                <Footer />
                <ErrorView />
                <AppMessageDlg />
            </div>);
    }
}
