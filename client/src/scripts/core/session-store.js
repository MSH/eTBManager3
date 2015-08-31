/**
 * Created by rmemoria on 29/8/15.
 */

import {EventEmitter} from 'events';
import App from './app';
import Const from './app-constants';


/**
 * Store information about the current session.
 *
 * This is just the class definition. Object is stored in the app file
 */
class SessionStore extends EventEmitter {
    constructor() {
        super();
        this.loggedIn = false;

        registerDispatcher(this);
    }

    emitChange() {
        this.emit('change');
    }
}

var sessionStore = new SessionStore;
export default sessionStore;


/**
 * Register the session store in the main dispatch to receive information
 * about the actions that happen in the system
 * @param store the instance of SessionStore
 */
function registerDispatcher(store) {
    store.dispatchToken = App.dispatcher.register(function(action) {
        if (action.type === Const.LOGIN) {
            registerLogin(store, action.data);
            return;
        }

        if (action.type === Const.LOGOUT) {
            registerLogout(store);
            return;
        }
    });
}

/**
 * Register the login and emit an event about changes in the session store
 * @param store
 * @param loginData
 */
function registerLogin(store, loginData) {
    store.loggedIn = true;
    store.user = loginData.user;
    store.workspace = loginData.workspace;
    store.permissions = loginData.permissions;
    store.emitChange();
}


/**
 * Register the user logout
 * @param store
 */
function registerLogout(store) {
    store.loggedIn = false;
    store.user = undefined;
    store.workspace = undefined;
    store.permissions = undefined;
    store.emitChange();
}


/**
 * Called when the current workspace was changed
 * @param store The session store in use
 * @param data The data about the new workspace
 */
function changeWorkspace(store, data) {
    store.workspace = data.workspace;
    store.permissions = data.permissions;
    store.emitChange();
}
