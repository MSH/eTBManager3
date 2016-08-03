/**
 * Simple wrapper for font-awesome icons
 */

import React from 'react';

export default class Fa extends React.Component {

	render() {
		const p = this.props;
		const clazz = 'fa fa-fw' +
			(p.icon ? ' fa-' + p.icon : '') +
//			(p.size ? ' fa-' + p.size + 'x' : '') +
			(p.spin ? ' fa-spin' : '') +
			(p.className ? ' ' + p.className : '');

		const style = p.size ? { fontSize: p.size + 'em' } : null;

		return <i className={clazz} style={style} />;
//		return <i className={clazz} />;
	}
}

Fa.propTypes = {
	icon: React.PropTypes.string,
	size: React.PropTypes.number,
	spin: React.PropTypes.bool,
	className: React.PropTypes.string
};
