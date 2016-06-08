
import React from 'react';

// pattern to extract the params from the route
const paramsPattern = /{(\w+)}/g;


/**
 * React component responsible for displaying the content of a route
 */
export class RouteView extends React.Component {

	static createRoutes(data) {
		return new RouteList(data);
	}

	constructor(props) {
		super(props);
		this.state = {};
	}

	/**
	 * Return the path from the left side to the current route
	 * @return {string} The relative path or null if route was not found
	 */
	getChildContext() {
		const route = this.state.route;
		if (!route) {
			return null;
		}

		const rpath = (this.context.path ? this.context.path : '') +
			(route ? route.data.path : '');

		return { path: rpath };
	}

	componentWillMount() {
		router.subscribe(this);
		this.resolveView();
	}


	componentWillReceiveProps() {
		this.resolveView();
	}


	componentWillUnmount() {
		router.unsubscribe(this);
	}

	/**
	 * Return the current path being resolved by this path
	 * @return {[type]} [description]
	 */
	_currentPath() {
		let hash = router.hash();
		if (this.context.path) {
			const s = hash.split('?');
			hash = s.shift();
			// const params = s.shift();

			// check if the context is beyond hash (using default views in routes)
			if (this.context.path.startsWith(hash)) {
				hash = '';
			}
			else {
				hash = hash.replace(this.context.path, '');
			}

		//	hash += params ? '?' + params : '';
		}
		return hash;
	}

	/**
	 * Called by navigator when the has changed
	 * @return {[type]}      [description]
	 */
	onHashChange() {
		this.resolveView();
	}

	onPageNotFound() {
		if (this.props.pageNotFoundView) {
			this.setState({ view: this.props.pageNotFoundView });
		}
	}

	resolveView() {
		// get the current path, i.e, the path from here ahead
		let path = this._currentPath();

		// search for a suitable route for this path
		const route = this.props.routes.find(path);

		// route to the current path was not found ?
		if (!route) {
			// if there was a path but route was not found, show page not found
			if (path) {
				router.showPageNotFound();
			}
			return;
		}

		if (!path) {
			path = route.data.path;
		}

		const View = route.view;

		const params = route.resolveParams(path);

		if (View) {
			this.setState({ view: View, route: route, path: path, params: params });
			return;
		}

		const self = this;
		const res = route.resolveView(path);

		// result is a promise ?
		if (res.then) {
			res.then(view => self.setState({ view: view, route: route, path: path, params: params }));
			return;
		}

		this.setState({ view: res, route: route, path: path, params: params });
	}

	/**
	 * Render the view
	 * @return {Component} The rendered react view
	 */
	render() {
		let View = this.state.view;
		const route = this.state.route;

		// no view resolved?
		if (!View) {
			const loadingView = this.props.loadingView;
			// is resolving view, i.e, route != null ?
			return route && loadingView ? loadingView : null;
		}

		const params = this.state.params;
		const path = this._currentPath();
		const forpath = path.replace(route.pathExp, '');

		// set the properties to be passed to the view that will be rendered
		const viewProps = Object.assign({}, this.props.viewProps);
		viewProps.route = {
			hash: router.hash(),
			params: params ? params : {},
			path: route.data.path,
			forpath: forpath,
			data: route.data,
			queryParam: getParameterByName
		};

		if (typeof View === 'object' && View.default) {
			View = View.default;
		}

		// set the title, if available
		if (route.data.title) {
			document.title = route.data.title;
		}

		return <View {...viewProps} />;
	}
}

RouteView.propTypes = {
    loadingView: React.PropTypes.object,
    pageNotFoundView: React.PropTypes.func,
    routes: React.PropTypes.object,
    viewProps: React.PropTypes.object
};

RouteView.childContextTypes = {
	path: React.PropTypes.string
};

RouteView.contextTypes = {
	path: React.PropTypes.string
};


/**
 * Declare a list of routes for a given view
 */
export class RouteList {
	constructor(lst) {
		let index = 0;
		this.list = lst.map(r => new Route(r, index++));
	}

	/**
	 * Search for a route by its path
	 * @param  {string} path The path to be tested
	 * @return {object}      Object containing information about the route
	 */
	find(path) {
		// no path defined ?
		if (!path) {
			// search for default path
			return this.list.find(r => r.data.default);
		}

		for (var i = 0; i < this.list.length; i++) {
			const r = this.list[i];
			// given path matches the route path ?
			if (r.pathExp.test(path)) {
				return r;
			}
		}
		return null;
	}
}

/**
 * Represents a route in the system
 */
export class Route {
	constructor(data, index) {
		this.data = data;

		// create pattern to make it easier to identify routes
		const p = '^' + data.path.replace(/\//g, '\\\/').replace(/{\w+}/g, '([\\w\-]+)');
		this.pathExp = new RegExp(p);

		// get the list of params without the { }
		let params = data.path.match(paramsPattern);

		if (params) {
			params = params.map(item => item.substring(1).split('}')[0]);
		}

		this.params = params;
		this.index = index;
	}


	/**
	 * Resolve the params based on the given route and path
	 * @param  {[type]} route [description]
	 * @param  {[type]} path  [description]
	 * @return {[type]}       [description]
	 */
	resolveParams(path) {
		var res = path.match(this.pathExp);
		if (res && this.params) {
			const params = {};
			for (var i = 0; i < this.params.length; i++) {
				// get the param name
				var pname = this.params[i];
				params[pname] = res[i + 1];
			}
			return params;
		}
		return null;
	}

	/**
	 * Resolve the view of the route. View is resolved using a promise, even if it is
	 * immediatelly available
	 * @return {Promise} Promise to resolve the view
	 */
	resolveView() {
		const data = this.data;

		// is being resolved by a promise ?
		if (this._resPromise) {
			return this._resPromise;
		}

		// check if view is available
		if (data.view) {
			return data.view;
		}

		// check if resolver is defined
		if (data.viewResolver) {
			const res = data.viewResolver(data.path, this);

			const self = this;
			if (res.then) {
				this._resPromise = res;
				res.then(ret => {
					delete self._resPromise;
					return ret;
				});
			}
			return res;
		}

		return null;

		// this._resPromise = new Promise((resolve, reject) => {
		// 	if (data.view) {
		// 		return resolve(data.view);
		// 	}

		// 	if (data.viewResolver) {
		// 		return resolve(data.viewResolver(data.path, this));
		// 	}
		// 	return reject('No view or viewResolver');
		// });

		// const self = this;
		// this._resPromise.then(res => {
		// 	delete self._resPromise;
		// 	return res;
		// });

		// return this._resPromise;
	}
}


/**
 * Router is responsible for monitoring the location.hash object. Once a change is
 * found, the navigator notifies the root RouteView in order to update the view
 */
class Router {
	/**
	 * Initialize the navigator passing a callback function that will receive notification
	 * about changes in the URL. The initialization is just done once (during app startup),
	 * and subsequence calls will be useless
	 * @param  {Function} callback [description]
	 * @return {[type]}            [description]
	 */
	subscribe(rootView) {
		const res = !this.observer;
		if (res) {
			this.observer = rootView;
		}

		if (this.initialized) {
			return res;
		}

		// MUST IMPROVE THAT TO USE SOMETHING MORE MODERN
        var self = this;
        var current = self.hash();
        var fn = function() {
            var hash = self.hash();
            if (current !== hash && self.observer) {
                current = hash;
                self.observer.onHashChange(current);
            }
        };
        clearInterval(this.interval);
        // set the interval to check every 1/10 of second
        this.interval = setInterval(fn, 100);
		this.initialized = true;
		this.rootView = rootView;

		return res;
	}

	/**
	 * Return the current hash
	 * @return {[type]} [description]
	 */
	hash() {
		const s = location.hash;
		return s[0] === '#' ? s.substring(1) : s;
	}

	/**
	 * Navigate to a given path
	 * @param  {String} path   The path to go to
	 * @return {[type]}        [description]
	 */
	goto(path) {
		location.href = '#' + path;
	}

	unsubscribe(rootView) {
		if (rootView === this.observer) {
			this.observer = null;
		}
	}

	/**
	 * Called when part of the page is not found
	 * @return {[type]} [description]
	 */
	showPageNotFound() {
		return this.rootView.onPageNotFound();
	}
}

const router = new Router();

function getParameterByName(pname) {
    const url = window.location.href;

    const name = pname.replace(/[\[\]]/g, '\\$&');
    const regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');
    const results = regex.exec(url);

    if (!results) {
		return null;
    }

    if (!results[2]) {
		return '';
    }
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

export { router };
