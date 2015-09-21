import React from 'react';
import RouteView from '../components/RouteView.jsx';

import Hello2 from './hello2.jsx';


export default class Hello extends React.Component {
	render() {
		let routes = [
			{path: '/hello2/{num}', view: Hello2 }
		];

		return (
			<div>
				<h1>Hello</h1>
				<RouteView key={2} id={'child1'} routes={routes}>
				</RouteView>
			</div>
			);
	}
}