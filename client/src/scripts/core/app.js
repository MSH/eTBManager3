/**
 * This is the main application module. It starts application execution, the navigation system
 * and call the initial action that will return the server status.
 * Created by rmemoria on 29/8/15.
 **/
'use strict';


import React from 'react';
import {Dispatcher} from 'flux';
import Http from './http';
import Actions from './app-actions.js';
import Const from './app-constants.js';
import RootContent from '../commons/root-content.jsx';
import Router from './router.js';

/** The main application class */

class App {
    /** Application constructor  **/
    constructor() {
        this.dispatcher = new Dispatcher();
    }

    /**
     *  Wrapper for dispatcher.dispatch
     * */
    dispatch(data) {
        this.dispatcher.dispatch(data);
    }

    /**
     * Run the application. Must be called just once to start the application
     */
    run(elemId) {
        // link to app configuration
        this.config = window.appcfg;

    	// the element in the document to be replaced by the code
    	var mainElement = document.getElementById(elemId);
    	var rootContent = React.createElement(RootContent);
    	React.render(rootContent, mainElement);

    	// get information about the system. The return of this action will be handled by the stores
    	Actions.requestServerStatus();

    	// register the dispatch to handle system state
        var app = this;
        var token = this.dispatcher.register(function(action) {
           if (action.type === Const.SYS_INFO) {
                handleSysInfo(app, action.data);
                // since it is called just once, remove the registration
                app.dispatcher.unregister(token);
            }
        });
    }
}


/**
 * Called by the getState action once the application initializes. Get information about the server status
 * @param app the instance of the application
 * @param info the information sent from the server
 **/
function handleSysInfo(app, info) {
	// store the state of the system
	app.state = info.state;

	var path;
	switch (info.state) {
		case 'NEW': path = '/init/welcome';
			break;
		case 'AUTH_REQUIRED': path = '/pub/login';
			break;
		case 'READY': path = '/app/index';
		    break;
	}

    window.alert(info.state);
	Router.listen();
	if (Router.getHash() === path) {
		Router.check(path);
	}
	else {
		Router.navigate(path);
	}
}


/**
 * Create an instance of the application that will be available for all the client side
 * @type {App}
 */
var app = new App();

export default app;

