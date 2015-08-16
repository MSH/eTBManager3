'use strict';

import React from 'react';
import Hello from './component.jsx';
import Request from 'superAgent';


/**
* This is the temporary main page
*
**/
var Main = React.createClass({
	routeListener: function(data, done) {
		var path = data.path;

		var view;
		if (path === '/app') {
			AppModule.init(path, done);
		}
		else {
			PubModule.init(path, done);
		}
	},


	render: function() {
		var routers = [
			{path: '/pub', handler: this.routeListener},
			{path: '/app', handler: this.routeListener}
		];

		return (
			<RouterView routes={routers} />
		);
	}
});


/**
 * This is the application enty proint
 */

// load the style sheet in use
require('../styles/theme.css');

// get system information
Request
	.get('/api/sys/info')
	.end(function(err, res) {
		if (err) {
			alert('Error: ' + err);
		}
		else {
			run(res.body);
		}
	});


/**
 * Execute the system passing as argument the system information
 * @param  {[type]} status [description]
 * @return {[type]}        [description]
 */
function run(info) {
	console.log(info);
	var main = React.createElement(Main);

	// the element in the document to be replaced by the code
	var content = document.getElementById('content');
	React.render(main, content);

	router.listen();
	console.log(router.getHash());
	router.check(router.getHash());
}
