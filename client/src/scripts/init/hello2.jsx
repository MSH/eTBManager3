import React from 'react';

export default class Hello2 extends React.Component {
	render() {
		return (
			<h2>Hello 2 = {this.props.params.num}</h2>
			);
	}
}