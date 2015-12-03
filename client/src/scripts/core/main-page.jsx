
import React from 'react';
import { RouteView } from '../components/router';
import PageNotFound from './page-not-found';

import Toolbar from './toolbar';
import Footer from './footer';
import WaitIcon from '../components/wait-icon';

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
            { path: '/pub', viewResolver: this.openPublic.bind(this) },
            { path: '/init', viewResolver: this.openInit.bind(this) },
            { path: '/sys', viewResolver: this.openApp.bind(this) },
            { path: '/pagenotfound', view: PageNotFound }
        ]);

        return (
            <div>
                <Toolbar />
                <div className="app-content">

                <RouteView key={1}
                    routes={routers}
                    loadingView={<WaitIcon/>}
                    errorPath="/pagenotfound" />

                </div>
                <Footer />
            </div>);
    }
}
