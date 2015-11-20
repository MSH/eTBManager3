
/**
 * This is the base initialization of the Init module. The module is loaded asynchronously and load
 * the home module, where the page content is displayed
 * @param path the path to be displayed
 * @param done must be called with the view to be displayed
 */

var view;

export function init() {

	if (view) {
		return view;
	}

	return new Promise((resolve) => {
		require.ensure(['./home.jsx'], function(require) {
			const Home = require('./home.jsx');
			view = Home;
			resolve(Home);
		});

	});
}
