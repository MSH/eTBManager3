'use strict';

import Http from '../commons/http';

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

		// setTimeout(() => dispatch({	type: 'app-info', fetching: false}),
		// 	1000);

		setTimeout( () => {
//			dispatch({ type: APP_RUN, data: 'ok'});
		    // get system information
		    Http.get('/api/sys/info')
		        .end((err, res) =>
		            dispatch({
		            	type: APP_RUN,
		            	data: res.body
		            })
		        );
		}, 1000);

	};
}