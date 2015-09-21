
import React from 'react';

// pattern to extract the params from the route
let paramsPattern = /{(\w+)}/g;

// the current path being resolved by the route
let currPath;
let errorPath;


export default class RouteView extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			routes: createRouteTable(this.props.routes)
		};
	}

	/**
	 * Search for the route by the given path
	 * @param  {[type]} path [description]
	 * @return {[type]}      [description]
	 */
	findRoute(path) {
		for (var r in this.state.routes) {
			let rout = this.state.routes[r];
			// given path matches the route path ?
			if (rout.pathExp.test(path)) {
				return rout;
			}
		}
	}

	componentDidMount() {
		this.mounted = true;
		this.isRoot = navigator.subscribe(this);
	}


	componentWillUnmount() {
		this.mounted = false;
		navigator.unsubscribe(this);
	}

	/**
	 * Called by navigator when the has changed
	 * @param  {[type]} path [description]
	 * @return {[type]}      [description]
	 */
	onHashChange(path) {
		// resolve the view by the path
		let res = this.resolveView(path);
		if (!res) {
			return;
		}

		currPath = path;

		if (res.view) {
			this.setState(res);
		}
	}

	/**
	 * Resolve the view by the path. If view resolution is asynchronous,
	 * a call to setState will be made when view is resolved
	 * @param  {[type]} path [description]
	 * @return {[type]}      [description]
	 */
	resolveView(path) {
		if (!path) {
			path = this.props.defaultPath;
		}
		// search for route according to the given path
		let route = this.findRoute(path);
		// route was found ?
		if (!route) {
			if (errorPath) {
				navigator.goto(errorPath);
			}
			return;
		}

		var res = {
			view: undefined,
			route: route,
			path: path,
			params: this.resolveParams(route, path)
		};

		if (route.view) {
			res.view = route.view;
			return res;
		}

		if (route.viewResolver) {
			let comp = this;
			// called if view resolution is asynchronous
			let done = (view) => {
				res.view = view;
				comp.setState(res);
			};
			res.view = route.vireResolver(path, done);
		}
		return res;
	}


	/**
	 * Resolve the params based on the given route and path
	 * @param  {[type]} route [description]
	 * @param  {[type]} path  [description]
	 * @return {[type]}       [description]
	 */
	resolveParams(route, path) {
		var res = path.match(route.pathExp);
		if (res && route.params) {
			let params = {};
			for (var i = 0; i < route.params.length; i++) {
				// get the param name
				var pname = route.params[i];
				var pvalue = res[i + 1];
				params[pname] = pvalue;
			}
			return params;
		}
	}


	/**
	 * Render component
	 */
	render() {
		let View = this.state.view;
		let route = this.state.route;
		let params = this.state.params;

		// view is not available and is not the root view?
		if (!View && !this.isRoot) {
			// resolve the view by the current path
			let res = this.resolveView(currPath);
			if (res) {
				View = res.view;
				route = res.route;
				params = res.params;
			}
		}

		// if is root set the errorPath for the next calls
		if (this.isRoot) {
			errorPath = this.props.errorPath;
		}

		// create custom element
		if (View) {
			console.log(this.state);
			// update the current path for the next calls
			currPath = currPath.replace(route.pathExp, '');
			return (
				<div>
					{currPath}
				<View params={params}></View>
				</div>
			);
		}
		else {
			return null;
		}
	}
}



/**
 * Create the list of routes to be stored by the component
 * @param  {array} lst List of routes informed in the prop.routes
 * @return {object}     List of compiled routes
 */
function createRouteTable(lst) {
	// mount the list of routes
	let routes = {};
	if (lst) {
		lst.forEach(r => {
			// create pattern to make it easier to identify routes
			let p = '^' + r.path.replace(/\//g, '\\\/').replace(/{\w+}/g, '(\\w+)');
			// get the list of params without the { }
			let params = r.path
				.match(paramsPattern);

			if (params) {
				params = params.map((item) => item.substring(1).split('}')[0]);
			}

			// create route object
			var route = {
				pathExp: new RegExp(p),
				path: r.path,
				view: r.view,
				params: params,
				viewResolver: r.viewResolver
			};
			routes[r.path] = route;
		});
	}

	return routes;
}


/**
 * Navigator is responsible for monitoring the location.hash object. Once a change is
 * found, the navigator notifies the root RouteView in order to update the view
 */
class Navigator {
	/**
	 * Initialize the navigator passing a callback function that will receive notification
	 * about changes in the URL. The initialization is just done once (during app startup),
	 * and subsequence calls will be useless
	 * @param  {Function} callback [description]
	 * @return {[type]}            [description]
	 */
	subscribe(rootView) {
		let res = !this.observer;
		if (res) {
			this.observer = rootView;
		}

		if (this.initialized) {
			return res;
		}

		// MUST IMPROVE THAT TO USE SOMETHING MORE MODERN
        var self = this;
        var current;
        var fn = function() {
            var hash = self.getHash();
            if(current !== hash && self.observer) {
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
	getHash() {
		let s = location.hash;
		return s[0] === '#'? s.substring(1): s;
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

let navigator = new Navigator();
