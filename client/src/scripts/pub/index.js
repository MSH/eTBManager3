/**
 * This is the base initialization of the Public module. The module is loaded asynchronously and load
 * the home module, where the page content is displayed
 * @param path the path to be displayed
 * @param done must be called with the view to be displayed
 */

var view;

export function init() {

	return view || new Promise(resolve => {
		require.ensure('./home', function(require) {
			var Home = require('./home');
			view = Home;
			resolve(Home);
		});
	});
}
