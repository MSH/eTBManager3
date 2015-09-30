'use strict';

import Request from 'superagent';


export default {
	/**
	 * Create a request GET to the given URL
	 * @param  {String} url The remote address
	 * @return {Request}    Request object
	 */
	get: function(url, callback) {
		let res = Request
			.get(url)
			.use(auth)
			.use(errorHandler);

        if (callback) {
            res.end(callback);
        }
        return res;
	},

	/**
	 * Create a request POST to the given URL
	 * @param  {String} url The remote address
	 * @return {Request}    Request object
	 */
	post: function(url, data, callback) {
		let res = Request
			.post(url)
            .send(data)
		//	.set('Content-type', 'application/json')
			.use(auth)
			.use(errorHandler);

        if (callback) {
            res.end(callback);
        }
        return res;
	}
};


function auth(req) {
	// authentication token is available ?
	var authToken = window.app.getCookie('autok');
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
            alert(err);
        }
	};

	return req;
}

