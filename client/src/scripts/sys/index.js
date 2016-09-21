
import { authenticate, isAuthenticated, initSession } from './session';

export function init() {

	return new Promise((resolve, reject) => {
		require.ensure(['./routes', './types/init'], require => {
			var Routes = require('./routes');
			var Types = require('./types/init');

			Types.register();

			// initialize session
			initSession();

			// check if user was already authenticated, to avoid multiple requests to the server
			// of data already requested
			if (isAuthenticated()) {
				return resolve(Routes);
			}

			// authenticate the user with the server
			return authenticate()
			.then(() => {
				// return the list of routes
				return resolve(Routes);
			})
			.catch(err => reject(err));
		});

	});
}
