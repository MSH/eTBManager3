
import React from 'react';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import Fa from './fa';


/**
 * A simple link that displays a tooltip at the top of the link
 */
export default class LinkTooltip extends React.Component {

	render() {
		const classes = [];
		if (this.props.muted) {
			classes.push('lnk-muted');
		}

		if (this.props.circle) {
			classes.push('lnk-circle');
		}

		if (this.props.className) {
			classes.push(this.props.className);
		}

		return (
			<OverlayTrigger placement="top" overlay={<Tooltip id="tt">{this.props.toolTip}</Tooltip>}>
				<a className={classes.join(' ')} onClick={this.props.onClick}>
					<Fa icon={this.props.icon}/>
				</a>
			</OverlayTrigger>
			);
	}
}

LinkTooltip.propTypes = {
	onClick: React.PropTypes.func,
	className: React.PropTypes.string,
	icon: React.PropTypes.string,
	circle: React.PropTypes.bool,
	muted: React.PropTypes.bool,
	toolTip: React.PropTypes.string
};

LinkTooltip.defaultProps = {
	circle: true,
	muted: true
};
