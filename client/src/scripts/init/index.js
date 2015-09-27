'use strict';

/**
 * This is the base initialization of the Init module. The module is loaded asynchronously and load
 * the home module, where the page content is displayed
 * @param path the path to be displayed
 * @param done must be called with the view to be displayed
 */
exports.init = function(path, done) {

	require.ensure(['./home.jsx', './reducers'], function(require) {
		let Home = require('./home.jsx');
        let reducers = require('./reducers');
        reducers.init();
		done(Home);
	});
};