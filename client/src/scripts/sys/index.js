
import Server from '../commons/server.js';
import { AUTHENTICATED } from '../core/actions';

var view;

export function init(data) {

	return view || new Promise(resolve => {
		require.ensure('./routes.jsx', function(require) {
			var Routes = require('./routes.jsx');

			// get information about the session
			Server.post('/api/sys/session')
			.then(res => {
				data.app.dispatch(AUTHENTICATED, { session: res });
				resolve(Routes);
			});
		});

	});
}
