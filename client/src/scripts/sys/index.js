
import { TB_SET } from '../core/actions';

var view;

export function init(data) {

	return view || new Promise(resolve => {
		require.ensure(['./routes.jsx', './toolbar-content.jsx'], function(require) {
			var Routes = require('./routes.jsx');

			if (data.app.session.isAuthenticated()) {
				return resolve(Routes);
			}

			// authenticate the user with the server
			data.app.session.authenticate()
			.then(() => {
				// set the content of the toolbar
				const ToolbarContent = require('./toolbar-content.jsx');

				// dispatch to the toolbar
				data.app.dispatch(TB_SET, { toolbarContent: ToolbarContent.default });

				// return the list of routes
				resolve(Routes);
			});
		});

	});
}
