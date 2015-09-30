
import React from 'react';
import { RouteView } from '../components/router.jsx';
import PageNotFound from './page-not-found.jsx';


/**
 * Modules that are part of the system
 */
import InitMod from '../init/index';
import PubMod from '../pub/index';
import SysMod from '../sys/index';


/**
 * The root content of the application
 *
 **/
export default class RootContent extends React.Component {
    /**
     * Open the public module
     */
    openPublic (data, done) {
        PubMod.init(data, done);
    }

    /**
     * Open the initialization module
     */
    openInit (data, done) {
        InitMod.init(data, done);
    }

    /**
     * Open the application home page
     */
    openApp(data, done) {
        SysMod.init(data, done);
    }

    render() {
        var routers = [
            {path: '/pub', viewResolver: this.openPublic},
            {path: '/init', viewResolver: this.openInit},
            {path: '/app', viewResolver: this.openApp},
            {path: '/pagenotfound', view: PageNotFound}
        ];

        let viewProps = {
            dispatch: this.props.dispatch,
            appState: this.props.appState
        };

        return (
            <RouteView key={1} viewProps={viewProps} routes={routers} errorPath="/pagenotfound" />
        );
    }
}
