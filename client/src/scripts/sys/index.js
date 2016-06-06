
import { TB_SET } from '../core/actions';
import { app } from '../core/app';
import { authenticate, isAuthenticated, initSession } from './session';

export function init() {

	console.log('home init');

	return new Promise(resolve => {
		require.ensure(['./routes', './toolbar-content', './types/init'], require => {
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
				// set the content of the toolbar
				const ToolbarContent = require('./toolbar-content');

				// dispatch to the toolbar
				app.dispatch(TB_SET, { toolbarContent: ToolbarContent.default });

				// return the list of routes
				resolve(Routes);
			});
		});

	});
}
