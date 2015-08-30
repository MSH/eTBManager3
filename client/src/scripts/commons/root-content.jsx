
import React from 'react';
import RouterView from '../core/RouterView.jsx';
import Toolbar from './toolbar.jsx';
import WaitIcon from './wait-icon.jsx';


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
        PubMod.init(data.path, done);
    }

    /**
     * Open the initialization module
     */
    openInit (data, done) {
        InitMod.init(data.path, done);
    }

    /**
     * Open the application home page
     */
    openApp(data, done) {
        SysMod.init(data.path, done);
    }

    render() {
        var routers = [
            {path: '/pub', handler: this.openPublic},
            {path: '/init', handler: this.openInit},
            {path: '/app', handler: this.openApp}
        ];

        return (
            <div>
                <Toolbar>
                </Toolbar>
                <div className='app-content'>
                    <RouterView routes={routers} />
                    <WaitIcon />
                </div>
            </div>
        );
    }
}
