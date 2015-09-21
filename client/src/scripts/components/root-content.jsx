
import React from 'react';
import RouteView from '../components/RouteView.jsx';


/**
 * Modules that are part of the system
 */
import InitMod from '../init/index';
import PubMod from '../pub/index';
import SysMod from '../sys/index';

import Hello from '../init/hello.jsx';


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
            {path: '/pub', viewResolver: this.openPublic},
            {path: '/init', viewResolver: this.openInit},
            {path: '/app', viewResolver: this.openApp},
            {path: '/hello', view: Hello}
        ];

        return (
            <div className='app-content'>
                <RouteView key={1} id={'root'} routes={routers} />
            </div>
        );
    }
}
