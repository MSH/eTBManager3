'use strict';

//import Home from './home.jsx';

exports.init = function(path, done) {

//	done(Home);

	require.ensure('./home.jsx', function(require) {
		var Home = require('./home.jsx');
		done(Home);
	});
};