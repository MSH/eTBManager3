
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
        return initPub({ app: this.props.app, path: path });
    }

    /**
     * Open the initialization module
     */
    openInit(path) {
        return initInit({ app: this.props.app, path: path });
    }

    /**
     * Open the application home page
     */
    openApp(path) {
        return initSys({ app: this.props.app, path: path });
    }

    render() {
        const app = this.props.app;

        // pass reference of the application to the child view
        const viewProps = {
            app: app
        };

        var routers = [
            { path: '/pub', viewResolver: this.openPublic.bind(this) },
            { path: '/init', viewResolver: this.openInit.bind(this) },
            { path: '/sys', viewResolver: this.openApp.bind(this) },
            { path: '/pagenotfound', view: PageNotFound }
        ];

        return (
            <div>
                <Toolbar app={app} />
                <div className="app-content">

                <RouteView key={1} viewProps={viewProps}
                    routes={routers}
                    loadingView={<WaitIcon/>}
                    errorPath="/pagenotfound" />

                </div>
                <Footer />
            </div>);
    }
}

MainPage.propTypes = {
    app: React.PropTypes.object
};
