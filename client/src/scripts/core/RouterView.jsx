'use strict';

import React from 'react';
import Router from './router';

import Test from '../component.jsx';


// the parent component that last rendered
var pcomp;

export default class RouterView extends React.Component {
	constructor(props) {
		super(props);
		this.addRoutes();

		this.state = { content: undefined };
		this.findRoute = this.findRoute.bind(this);
		this.addRoutes = this.findRoute.bind(this);
		this.removeRoutes = this.removeRoutes.bind(this);
		this.routeHandler = this.routeHandler.bind(this);
	}

	findRoute(path) {
		for (var i = 0; i < this.props.routes.length; i++) {
			var r = this.props.routes[i];
			if (r.path === path) {
				return r;
			}
		}
	}

	addRoutes() {
		this.proute = pcomp ? pcomp: Router;

		var lst = this.props.routes;
		if (lst) {
			this.routes = {};
			for (var i = 0; i < lst.length; i++) {
				var r = lst[i];
				this.proute.add(r.path, this.routeHandler, this);
			}
		}
	}

	removeRoutes() {
		console.log('removing routes from ' + this.path);
		var lst = this.props.routes;
		for (var i = 0; i < lst.length; i++) {
			var r = lst[i];
			this.proute.remove(r.path);
		}
	}

	routeHandler(data, done) {
		pcomp = data.route;

		var route = this.findRoute(data.route.path);
		var st = {content: undefined, params: data.params};
		if (route) {
			if (route.handler) {
				var self = this;
				route.handler({path: route.path, params: data.params}, function(view) {
					console.log('state set');
					self.setState({content: view, params: data.params});
					done();
				});
				return;
			}
			else {
				st.content = route.view;
			}
		}
		this.setState(st);
		done();
	}

	componentDidMount() {
		console.log('mounting...');
	}

	componentWillUnmount() {
		console.log('unmounting...');
		this.removeRoutes();
	}

	render() {
		// create custom element
		if (this.state.content) {
			var Content = this.state.content;
			return (
				<Content></Content>
//				<Content router={this.router} ref={this.state.content} params={this.state.params}></Content>
				);
		}
		else {
			return (<div key='emptyDiv'></div>);
		}
	}
}
