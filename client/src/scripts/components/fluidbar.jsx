/**
 * Fluidbar is a component that displays a simple solid vertical bar
 */

import React from 'react';

export default class Fluidbar extends React.Component {

	render() {
		let className = 'fluidbar';
		if (this.props.bsStyle) {
			className += ' bg-' + this.props.bsStyle;
		}
		else {
			className += ' fluidbar-default';
		}

		return (
			<div className={className}>
				{this.props.children}
			</div>
			);
	}
}

Fluidbar.propTypes = {
	bsStyle: React.PropTypes.string,
	children: React.PropTypes.any
};
