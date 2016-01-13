
import React from 'react';

// pattern to extract the params from the route
const paramsPattern = /{(\w+)}/g;

// the current path being resolved by the route
let // currPath,
	errorPath;


/**
 * React component responsible for displaying the content of a route
 */
export class RouteView extends React.Component {

	constructor(props) {
		super(props);
		this.state = {};
	}

	getChildContext() {
		const path = this._currentPath();
		const route = this.props.routes.find(path);

		const rpath = (this.context.path ? this.context.path : '') +
			(route ? route.data.path : '');

		return { path: rpath };
	}

	componentWillMount() {
		this.isRoot = router.subscribe(this);
		// set the errorPath page
		if (this.isRoot && this.props.errorPath) {
			errorPath = this.props.errorPath;
		}
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
			hash = hash.replace(this.context.path, '');
		}
		return hash;
	}

	static createRoutes(data) {
		return new RouteList(data);
	}

	/**
	 * Called by navigator when the has changed
	 * @return {[type]}      [description]
	 */
	onHashChange() {
		this.setState({ view: null, route: null,  params: null });
	}

	resolveView() {
		const path = this._currentPath();

		// no path to be resolved ?
		if (!path) {
			return null;
		}

		const route = this.props.routes.find(path);

		// route to the current path was not found ?
		if (!route) {
			if (errorPath) {
				router.goto(errorPath);
			}
			return null;
		}

		let View = this.state ? this.state.view : null;

		if (!View) {
			// view is resolved ?
			if (route.view) {
				View = route.view;
			}
			else {
				const self = this;
				route.resolveView(path)
				.then(view => self.setState({ view: view, route: route }));
			}
		}

		return { view: View, route: route };
	}

	/**
	 * Render the view
	 * @return {Component} The rendered react view
	 */
	render() {
		const res = this.resolveView();

		// no route was found ?
		if (!res) {
			return <div/>;
		}

		// get any route in the state of the component
		let View = res.view;
		const route = res.route;

		// view is still being resolved?
		if (!View) {
			// show loading component while view is being resolved
			const loadingView = this.props.loadingView;
			return loadingView ? loadingView : <div/>;
		}

		// properties to be passed to the view are set ?
		let viewProps = {};
		if (this.props.viewProps) {
			viewProps = this.props.viewProps;
		}

		// get any param defined in the page
		const path = this._currentPath();
		const params = route.resolveParams(path);

		// get the next path
		const forpath = path.replace(route.pathExp, '');

		// include information about the route
		viewProps.route = {
			hash: router.hash(),
			params: params ? params : {},
			path: route.data.path,
			forpath: forpath,
			data: route.data
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
    errorPath: React.PropTypes.string,
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
		for (var i = 0; i < this.list.length; i++) {
			const r = this.list[i];
			// given path matches the route path ?
			if (r.pathExp.test(path)) {
				return r;
			}
		}
	}
}

/**
 * Represents a route in the system
 */
export class Route {
	constructor(data, index) {
		this.data = data;

		// create pattern to make it easier to identify routes
		const p = '^' + data.path.replace(/\//g, '\\\/').replace(/{\w+}/g, '(\\w+)');
		this.pathExp = new RegExp(p);

		// get the list of params without the { }
		let params = data.path.match(paramsPattern);

		if (params) {
			params = params.map((item) => item.substring(1).split('}')[0]);
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

		this._resPromise = new Promise((resolve, reject) => {
			if (data.view) {
				return resolve(data.view);
			}

			if (data.viewResolver) {
				return resolve(data.viewResolver(data.path, this));
			}
			reject('No view or viewResolver');
		});

		return this._resPromise;
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
	 * @param  {object} queryParams The query params
	 * @return {[type]}        [description]
	 */
	goto(path, queryParams) {
		location.href = '#' + path;
	}

	unsubscribe(rootView) {
		if (rootView === this.observer) {
			this.observer = null;
		}
	}
}

const router = new Router();

export { router };
