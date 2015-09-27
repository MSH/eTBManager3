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

        // get system information
        Http.get('/api/sys/info')
            .end(
            (err, res) => onReceiveStatus(dispatch, res.body)
        );

	};
}


/**
 * Called when client receives the status from the server
 * @param  {[type]} dispatch The dispatch object
 * @param  {[type]} res      The response from the server
 */
function onReceiveStatus(dispatch, res) {
    switch (res.state) {
        // if it is a new instance, go to the initialization module
        case 'NEW': gotoModule('/init', '/welcome');
            break;
        // if ready, go to the home page
        case 'READY': gotoModule('/sys', '/home');
            break;
        // default module is the login page
        default:
            gotoModule('/pub', '/login');
    }

	dispatch({
		type: APP_RUN,
		data: res
	});
}

function gotoModule(modpath, pagepath) {
    var hash = navigator.hash();
    if (hash.indexOf(modpath) !== 0) {
        navigator.goto(modpath + pagepath);
    }
}
