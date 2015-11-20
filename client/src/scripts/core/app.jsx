
import React from 'react';
import ReactDOM from 'react-dom';
import MainPage from './main-page';
import Server from '../commons/server';
import Storage from './storage';
import { APP_INIT, ERROR } from './actions';
import { router } from '../components/router';


export default class App {

	constructor() {
		this.listeners = [];
		this.storage = new Storage({ fetching: true });
		Server.setErrorHandler(this._serverErrorHandler.bind(this));
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
		Server.post('/api/sys/info', {})
		.then(res => {
			self.dispatch(APP_INIT, { app: res });
			// according to app state, go to specific module
			switch (res.state) {
				// if it is a new instance, go to the initialization module
				case 'NEW': gotoModule('/init', '/welcome');
					break;
				// if ready, go to the home page
				case 'READY':
					gotoModule('/sys', '/home');
					break;
				// default module is the login page
				default:
					gotoModule('/pub', '/login');
			}

			// render the main page
			ReactDOM.render(
				<MainPage app={self} />,
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
