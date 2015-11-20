
import React from 'react';

// pattern to extract the params from the route
let paramsPattern = /{(\w+)}/g;

// the current path being resolved by the route
let currPath;
let errorPath;


export class RouteView extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			routes: createRouteTable(this.props.routes)
		};
//		this.onHashChange = this.onHashChange.bind(this);
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
		this.isRoot = router.subscribe(this);
	}


	componentWillUnmount() {
		this.mounted = false;
		router.unsubscribe(this);
	}

	/**
	 * Called by navigator when the has changed
	 * @param  {[type]} path [description]
	 * @return {[type]}      [description]
	 */
	onHashChange(path) {
		// resolve the view by the path
        currPath = path;
		// let res = this.resolveView(path);
		// if (!res) {
		// 	return;
		// }

		this.setState({ view: null, route: null,  params: null, loading: false });

		// if (res.view || res.loading) {
		// 	this.setState(res);
		// }
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

        // if nothing was defined, just return null
        if (!path) {
            return null;
        }

		// search for route according to the given path
		let route = this.findRoute(path);
		console.log('PATH = ' + path + ', ROUTES = ', this.state.routes);
		// route was found ?
		if (!route) {
            if (!errorPath) {
                errorPath = this.props.errorPath;
            }
			if (errorPath) {
				router.goto(errorPath);
			}

			return null;
		}

		var res = {
			view: undefined,
			route: route,
			path: path,
			params: this.resolveParams(route, path),
			loading: false
		};

		if (route.view) {
			res.view = route.view;
			return res;
		}

		// has an external resolver defined in route parameters
		if (route.viewResolver) {
			const comp = this;
			const resolved = route.viewResolver(path);

			// returned a promise ?
			if (resolved && typeof resolved.then === 'function') {
				// when view is resolved, refresh the component
				resolved.then(view => {
					res.view = view;
					res.loading = false;
					comp.setState(res);
				});

				res.view = null;
				res.loading = true;
			}
			else {
				res.view = resolved;
			}
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
				params[pname] = res[i + 1];
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

		// indicate if the rout is being asynchronously loaded
		var loading = this.state.loading;

		// view is not resolved?
		if (!View) {
			// resolve the view by the current path
			let res = this.resolveView(currPath);
			if (res) {
				View = res.view;
				route = res.route;
				params = res.params;
				loading = res.loading;
			}
		}

		// if is root set the errorPath for the next calls
		if (this.isRoot) {
			errorPath = this.props.errorPath;
		}

		// create custom element
		if (View) {
			// properties to be passed to the view are set ?
			let viewProps = {};
			if (this.props.viewProps) {
				viewProps = this.props.viewProps;
			}

			// params were set ?
			if (params) {
				viewProps.params = { params };
			}

			// update the current path for the next calls
			if (route) {
				currPath = currPath.replace(route.pathExp, '');
			}

			if (typeof View == 'object' && View.default) {
				View = View.default;
			}

			return <View {...viewProps} ></View>;
		}
		else {
			const loadingView = this.props.loadingView;
			return loading && loadingView? loadingView: <div/>;
		}
	}
}

RouteView.propTypes = {
    loadingView: React.PropTypes.object,
    errorPath: React.PropTypes.string,
    routes: React.PropTypes.array,
    viewProps: React.PropTypes.object
};



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
			routes[r.path] = {
                pathExp: new RegExp(p),
                path: r.path,
                view: r.view,
                params: params,
                viewResolver: r.viewResolver
            };
		});
	}

	return routes;
}


/**
 * Navigator is responsible for monitoring the location.hash object. Once a change is
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
            var hash = self.hash();
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
	hash() {
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
		console.log('GOTO = ' + path);
		if (path === '/pagenotfound') {
			console.log('?');
		}
		location.href = '#' + path;
	}

	unsubscribe(rootView) {
		if (rootView === this.observer) {
			this.observer = null;
		}
	}
}

let router = new Router();

export { router };
