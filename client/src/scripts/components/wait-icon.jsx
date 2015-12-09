
import React from 'react';

/**
 * Display an animated wait icon in the middle of the page
 */
export default class WaitIcon extends React.Component {

	render() {
		if (this.props.type === 'page') {
			return (
				<div className="cssload-loading center">
					<i></i>
					<i></i>
					<i></i>
				</div>
			);
		}

		return <i className="fa fa-circle-o-notch fa-spin wait-icon-field" />;
	}
}

WaitIcon.propTypes = {
	type: React.PropTypes.oneOf(['page', 'field'])
};

WaitIcon.defaultProps = {
	type: 'page'
};
