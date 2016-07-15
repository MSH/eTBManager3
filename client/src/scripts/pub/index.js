/**
 * This is the base initialization of the Public module. The module is loaded asynchronously and load
 * the home module, where the page content is displayed
 * @param path the path to be displayed
 * @param done must be called with the view to be displayed
 */

var view;

export function init() {

	return view || new Promise(resolve => {
		require.ensure('./routes', function(require) {
			var Routes = require('./routes');
			view = Routes;
			resolve(Routes);
		});
	});
}
