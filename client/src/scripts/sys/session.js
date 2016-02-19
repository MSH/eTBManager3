
import { server } from '../commons/server';
import { LOGOUT, AUTHENTICATED, WORKSPACE_CHANGE, WORKSPACE_CHANGING } from '../core/actions';
import { app } from '../core/app';

/**
 * Initialize the session module by wiring the session to the app action dispatcher
 * @return {[type]} [description]
 */
export function initSession() {
	app.add(actionHandler);
}

function actionHandler(act, data) {
	if (act === AUTHENTICATED) {
		return data;
	}

	if (act === LOGOUT) {
		return { session: null };
	}

	if (act === WORKSPACE_CHANGE) {
		app.goto('/sys/home/index');
		return data;
	}
}

/**
 * Check if user was already authenticated
 * @return {Boolean} True if user is already authenticated
 */
export function	isAuthenticated() {
	const data = getSessionData();
	return data !== undefined && data !== null;
}

/**
 * Check if a given permission is allowed
 * @param  {string}  perm The permission code to check
 * @return {Boolean}      Return true if permission is granted
 */
export function hasPerm(perm) {
	const session = getSessionData();
	return session !== null && (session.administrator || session.permissions.indexOf(perm) >= 0);
}


/**
 * Perform a user logout in the system, forcing him to login again
 * @return {Promise} Promise that will be resolved when logout is finished
 */
export function logout() {
	const autk = window.app.getCookie('autk');

	// inform server to register logout of the authentication token
	if (autk) {
		// call server to register logout
		return server.get('/api/auth/logout?tk=' + autk)
		.then(() => {
			// clear authentication token in the cookies
			window.app.setCookie('autk', null);

			// inform the system about the logout
			app.dispatch(LOGOUT);
		});
	}

	// return an empty promise if there is no logout information
	return new Promise(resolve => resolve(null));
}

/**
 * Request user session data to the server based on its authentication token.
 * A promise is returned and will return the result of the operation - In case the
 * authentication token is valid, it will return the session data, otherwise,
 * the promise will be rejected
 * @return {Promise} A promise containing the session data
 */
export function authenticate() {
	return server.post('/api/sys/session')
	.then(res => {
		app.dispatch(AUTHENTICATED, { session: res });
		return res;
	});
}


export function changeWorkspace(wsid) {
	app.dispatch(WORKSPACE_CHANGING);

	return server.post('/api/sys/changews/' + wsid)
	.then(res => {
		const authToken = res.authToken;
		window.app.setCookie('autk', authToken);
		app.dispatch(WORKSPACE_CHANGE, { session: res.session });
	});
}


/**
 * Return user session data stored by the application
 * @return {[type]} [description]
 */
function getSessionData() {
	return app.getState().session;
}

