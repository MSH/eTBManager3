
import Server from '../commons/server';
import { LOGOUT, AUTHENTICATED } from './actions';

/**
 * Function to handle user session
 */
export default class Session {
	/**
	 * Default constructor of the session.
	 * @param  {App} app Instance of the application object
	 */
	constructor(app) {
		this.app = app;
	}

	/**
	 * Check if a given permission is allowed
	 * @param  {string}  perm The permission code to check
	 * @return {Boolean}      Return true if permission is granted
	 */
	hasPerm(perm) {
		const session = this.getSessionData();
		return session !== null && (session.administrator || session.indexOf(perm) >= 0);
	}

	/**
	 * Perform login into the system. Returns a promise that will indicate if user
	 * @param  {String} user the user account
	 * @param  {String} pwd  the user password
	 * @return {Promise}      Promise that will be resolved with the authentication token, or null if failed
	 */
	login(user, pwd) {
		return Server.post('/api/auth/login', { username: user, password: pwd })
		.then(data => {
			if (!data.success) {
				return null;
			}

			// register the authentication token in the cookies
			const authToken = data.authToken;
			window.app.setCookie('autk', authToken);
			return authToken;
		});
	}

	/**
	 * Perform a user logout in the system, forcing him to login again
	 * @return {Promise} Promise that will be resolved when logout is finished
	 */
	logout() {
		const autk = window.app.getCookie('autk');

		// inform server to register logout of the authentication token
		if (autk) {
			const self = this;

			// call server to register logout
			return Server.get('/api/auth/logout?tk=' + autk)
			.then(() => {
				// clear authentication token in the cookies
				window.app.setCookie('autk', null);

				const app = self.app;

				// inform the system about the logout
				app.dispatch(LOGOUT, { session: null });
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
	authenticate() {
		return Server.post('/api/sys/session')
		.then(res => {
			this.app.dispatch(AUTHENTICATED, { session: res });
			return res;
		});
	}

	/**
	 * Return user session data stored by the application
	 * @return {[type]} [description]
	 */
	getSessionData() {
		return this.app.getState().session;
	}
}
