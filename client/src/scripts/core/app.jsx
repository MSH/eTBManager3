
import React from 'react';
import ReactDOM from 'react-dom';
import MainPage from './main-page';
import { server, onRequestError } from '../commons/server';
import Storage from './storage';
import { APP_INIT, ERROR } from './actions';
import { router } from '../components/router';

/**
 * Reference to the application
 */
var app;

export { app };

export function init(customApp) {
	app = customApp;
}

export class App {

	constructor() {
		this.listeners = [];
		// create storage that will keep application state
		this.storage = new Storage({ fetching: true });
		// attach API to handle user session tasks
		onRequestError(this._serverErrorHandler.bind(this));
	}

	/**
	 * Return the application state. The state shall not be changed here
	 * @return {object} Object containing the application state
	 */
	getState() {
		return this.storage.getState();
	}

	/**
	 * Dispatch an action, changing the state of the application
	 * @param {[type]} action The action that generated the state change
	 * @param {[type]} state  The new state to be merged with the current one
	 */
	dispatch(action, state) {
		this.storage.setState(action, state);
	}

	/**
	 * Add a listener that will be notified when application state changes
	 * @param {function} listener A function that will receive app state change notification
	 */
	add(listener) {
		this.storage.addListener(listener);
	}

	/**
	 * Remove a listener previously added with the add method
	 * @param  {function} listener The listener function previously added
	 */
	remove(listener) {
		this.storage.removeListener(listener);
	}

	/**
	 * Run the application
	 */
	run() {

		const self = this;

		// call server to get system status
		server.post('/api/sys/info?list=1', {})
		.then(res => {
			self.dispatch(APP_INIT, { app: res });
			// according to app state, go to specific module
			switch (res.state) {
				// if it is a new instance, go to the initialization module
				case 'NEW': gotoModule('/init', '/welcome');
					break;
				// if ready, go to the home page
				case 'READY':
					gotoModule('/sys', '/home/index');
					break;
				// default module is the login page
				default:
					gotoModule('/pub', '/login');
			}

			// render the main page
			ReactDOM.render(
				<MainPage />,
				document.getElementById('content'));
		});
	}

	/**
	 * Go to another page
	 * @param  {string} path The new page URL to go to
	 */
	goto(path) {
		router.goto(path);
	}

	/**
	 * Standard handler for server error requests
	 * @param  {object} err object containing information about the error, like status and message
	 */
	_serverErrorHandler(err) {
		if (err.status === 401) {
			this.goto('/pub/login');
		}
		else {
			this.dispatch(ERROR, { error: err.message });
		}
	}
}


function gotoModule(modpath, pagepath) {
    var hash = router.hash();

    if (hash.indexOf(modpath) !== 0) {
        router.goto(modpath + pagepath);
    }
}
