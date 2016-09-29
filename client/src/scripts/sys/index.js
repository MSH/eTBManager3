
import { authenticate, isAuthenticated, initSession } from './session';

/**
 * Bootstrap code for the system module. It loads the main
 * code of the application
 */
export function init() {

    return new Promise((resolve, reject) => {
        require.ensure(['./routes', './packages/types/init'], require => {
            var Routes = require('./routes');
            var Types = require('./packages/types/init');

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
