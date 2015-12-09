/**
 * Simple wrapper for font-awesome icons
 */

import React from 'react';

export default class Fa extends React.Component {

	render() {
		const p = this.props;
		const clazz = 'fa fa-fw fa-' +
			p.icon +
			(p.size ? 'fa-' + p.size + 'x' : '') +
			(p.spin ? 'fa-spin' : '');

		return <i className={clazz} />;
	}
}

Fa.propTypes = {
	icon: React.PropTypes.string,
	size: React.PropTypes.string,
	spin: React.PropTypes.bool
};
