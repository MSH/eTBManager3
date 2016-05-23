
import React from 'react';
import ReactDOM from 'react-dom';
import MainPage from './main-page';
import { server, onRequestError } from '../commons/server';
import Storage from './storage';
import { ERROR } from './actions';
import { router } from '../components/router';
import moment from 'moment';

/**
 * Reference to the application
 */
var app;
var LANG_KEY = 'lang';
var AUTHTOKEN_KEY = 'autk';


export { app };


export function init(customApp) {
	app = customApp;
}

export class App {

	constructor() {
		this.listeners = [];
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

	setState(state) {
		this.storage.setState(state);
	}

	/**
	 * Dispatch an action, changing the state of the application
	 * @param {[type]} action The action that generated the state change
	 * @param {[type]} state  The new state to be merged with the current one
	 */
	dispatch(action, state) {
		if (__DEV__) {
			console.log(action, state);
		}
		this.storage.dispatch(action, state);
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

		// set right locale in moment lib
		moment.locale(this.getLang());

		// call server to get system status
		server.post('/api/sys/info?list=1', {})
		.then(res => {
			// create storage that will keep application state
			self.storage = new Storage({ app: res });

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

	getCookie(cname) {
        var name = cname + '=';
        var s = '; ' + document.cookie;
        var vals = s.split('; ' + name);
        if (vals.length === 2) {
            return vals.pop().split(';').shift();
        }
        return null;
    }

    setCookie(name, value, days) {
        var s = name + '=' + value;
        if (days) {
            var d = new Date();
            d.setTime(d.getTime() + (days * 24 * 60 * 60 * 1000));
            s += '; expires=' + d.toUTCString();
        }
        document.cookie = s;
    }

    /**
     * Return the current language
     * @return {[type]} [description]
     */
    getLang() {
        return window.app.language;
    }

    /**
     * Change the current language
     * @param {[type]} value [description]
     */
    setLang(value) {
        this.setCookie(LANG_KEY, value);
        window.location.reload();
    }

    /**
     * Get authentication token to be sent to the client
     * @returns {*}
     */
    getAuthToken() {
        return this.getCookie(AUTHTOKEN_KEY);
    }

    setAuthToken(value) {
        this.setCookie(AUTHTOKEN_KEY, value);
    }
}


function gotoModule(modpath, pagepath) {
    var hash = router.hash();

    if (hash.indexOf(modpath) !== 0) {
        router.goto(modpath + pagepath);
    }
}
