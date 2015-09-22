'use strict';

import Http from '../commons/http';
import { navigator } from '../components/router.jsx';

export const APP_RUN = 'app-run';

/**
 * Run the application
 */
export function runApp() {

	return function(dispatch) {
		dispatch({
			type: APP_RUN,
			fetching: true
		});

		setTimeout( () => {
		    // get system information
		    Http.get('/api/sys/info')
		        .end(
		        	(err, res) => onReceiveStatus(dispatch, res.body)
		       	);
		}, 1000);

	};
}


/**
 * Called when client receives the status from the server
 * @param  {[type]} dispatch The dispatch object
 * @param  {[type]} res      The response from the server
 */
function onReceiveStatus(dispatch, res) {
	if (res.state === 'NEW') {
		navigator.goto('/init/welcome');
	}

	dispatch({
		type: APP_RUN,
		data: res
	});
}