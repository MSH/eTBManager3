/**
 * Handle requests to the server
 */

import Request from 'superagent';

/**
 * Custom error handler, defined by the application
 */
var customErrorHandler;

export default {
	/**
	 * Create a request GET to the given URL
	 * @param  {String} url The remote address
     * @param  {function} callback called when server answers
	 * @return {Request}    Request object
	 */
	get: function(url) {
		return new Promise((resolve, reject) => {
			Request
				.get(url)
				.use(auth)
				.use(errorHandler)
				.end((err, res) => {
					if (err) {
						return reject(err);
					}
					return resolve(res.body);
				});
		});
	},

	/**
	 * Create a request POST to the given URL
	 * @param  {String} url The remote address
	 * @return {Request}    Request object
	 */
	post: function(url, data) {
		return new Promise((resolve, reject) => {
			Request
				.post(url)
				.send(data)
			//	.set('Content-type', 'application/json')
				.use(auth)
				.use(errorHandler)
				.end((err, res) => {
					if (err) {
						reject(err);
					}
					resolve(res.body);
				});
		});
	},

	setErrorHandler: function(handler) {
		customErrorHandler = handler;
	}
};


function auth(req) {
	// authentication token is available ?
	var authToken = window.app.getAuthToken();
	if (authToken) {
		req.set('X-Auth-Token', authToken);
	}

	return req;
}


function errorHandler(req) {
	var cb = req.callback;

	req.callback = function(err, res) {
        cb.call(req, err, res);

        if (err) {
			if (customErrorHandler) {
				customErrorHandler(err);
			}
			else {
				alert(err);
			}
        }
	};

	return req;
}

