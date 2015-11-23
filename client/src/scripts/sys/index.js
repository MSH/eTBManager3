
import { TB_SET } from '../core/actions';
import { app } from '../core/app';
import ActSession from '../core/act-session';

var view;

export function init() {

	return view || new Promise(resolve => {
		require.ensure(['./routes.jsx', './toolbar-content.jsx'], function(require) {
			var Routes = require('./routes.jsx');

			// check if user was already authenticated, to avoid multiple requests to the server
			// of data already requested
			if (ActSession.isAuthenticated()) {
				return resolve(Routes);
			}

			// authenticate the user with the server
			ActSession.authenticate()
			.then(() => {
				// set the content of the toolbar
				const ToolbarContent = require('./toolbar-content.jsx');

				// dispatch to the toolbar
				app.dispatch(TB_SET, { toolbarContent: ToolbarContent.default });

				// return the list of routes
				resolve(Routes);
			});
		});

	});
}
