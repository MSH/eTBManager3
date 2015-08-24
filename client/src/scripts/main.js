'use strict';

import React from 'react';
import Http from './core/http';
import RouterView from './core/RouterView.jsx';
import Router from './core/router';

/**
 * Modules that are part of the system
 */
import InitMod from './init/index';
import PubMod from './pub/index';
import AppMod from './app/index';


/**
* Main component entry point
*
**/
class Main extends React.Component {
	/**
	 * Open the public module
	 */
	openPublic (data, done) {
		PubMod.init(data.path, done);
	}

	/**
	 * Open the initialization module
	 */
	openInit (data, done) {
		InitMod.init(data.path, done);
	}

	/**
	 * Open the application home page
	 */
	openApp(data, done) {
		AppMod.init(data.path, done);
	}

	render() {
		var routers = [
			{path: '/pub', handler: this.openPublic},
			{path: '/init', handler: this.openInit},
			{path: '/app', handler: this.openApp}
		];

		return (
			<RouterView routes={routers} />
		);
	}
}


/**
 * This is the application enty proint
 */

// load the style sheet in use
require('../styles/theme.css');
require('../styles/app.css');


// get system information
Http.get('/api/sys/info')
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
	var path;
	switch (info.state) {
		case 'NEW': path = '/init/welcome';
			break;
		case 'AUTH_REQUIRED': path = '/pub/login';
			break;
		case 'READY': path = '/app/index';
			break;
	}

	var main = React.createElement(Main);

	// the element in the document to be replaced by the code
	var content = document.getElementById('content');
	React.render(main, content);

	Router.listen();
	if (Router.getHash() === path) {
		Router.check(path);
	}
	else {
		Router.navigate(path);
	}
//	console.log(Router.getHash());
}
