
import { TB_SET } from '../core/actions';
import { app } from '../core/app';
import { authenticate, isAuthenticated } from '../core/session';

var view;

export function init() {

	return view || new Promise(resolve => {
		require.ensure(['./routes', './form-ext/registration', './toolbar-content'], function(require) {
			var Routes = require('./routes');
			var FormExtension = require('./form-ext/registration');

			FormExtension.register();

			// check if user was already authenticated, to avoid multiple requests to the server
			// of data already requested
			if (isAuthenticated()) {
				return resolve(Routes);
			}

			// authenticate the user with the server
			authenticate()
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
