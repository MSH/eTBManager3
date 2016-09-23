
import React from 'react';
import ReactDOM from 'react-dom';
import MainPage from './main-page';
import { server, onRequestError } from '../commons/server';
import Storage from './storage';
import { ERROR, SHOW_MESSAGE } from './actions';
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
        window.onhashchange = () => window.scrollTo(0, 0);
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
        return listener;
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

            // if system is not initialized yet, so there is no other way to go
            if (res.state === 'NEW') {
                gotoModule('/init', '/welcome');
            }

            // there is no page pointed in the url ?
            if (!window.location.hash) {
                // if ready, go to the main page
                if (res.state === 'READY') {
                    gotoModule('/sys', '/home/index');
                }
                else {
                    // if not, go to the login page
                    gotoModule('/pub', '/login');
                }
            }

            // render the main page
            ReactDOM.render(
                <MainPage />,
                document.getElementById('content'));
        });
    }

    /**
     * Show standard dialog message, in order to display information to the user
     * @param  {[type]} props [description]
     * @return {[type]}       [description]
     */
    messageDlg(props) {
        return new Promise(resolve => {
            // close function called when the dialog is closing
            const closeFunc = evt => {
                resolve(evt);
            };

            const dlgProps = Object.assign({}, props, { onClose: closeFunc });

            this.dispatch(SHOW_MESSAGE, dlgProps);
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
