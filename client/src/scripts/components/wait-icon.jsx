
import React from 'react';

/**
 * Display an animated wait icon in the middle of the page
 */
export default class WaitIcon extends React.Component {

	render() {
		if (this.props.type === 'page') {
			return (
				<div className="cssload-loading center">
					<i/>
					<i/>
					<i/>
				</div>
			);
		}

		const className = 'fa fa-circle-o-notch fa-spin';
		if (this.props.type === 'card') {
			return <div className="wait-card"><i className={className + ' wait-icon-card'} /></div>;
		}

		return <i className={className + ' wait-icon-field'} />;
	}
}

WaitIcon.propTypes = {
	type: React.PropTypes.oneOf(['page', 'field', 'card'])
};

WaitIcon.defaultProps = {
	type: 'page'
};
