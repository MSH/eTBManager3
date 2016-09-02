import { Label } from 'react-bootstrap';

/**
 * Simple wrapper to display the case state
 */

import React from 'react';

export default class CaseState extends React.Component {

	render() {
		let style;

		switch (this.props.state.id) {
			case 'NOT_ONTREATMENT':
				style = 'warning';
				break;
			case 'ONTREATMENT':
				style = 'danger';
				break;
			case 'CLOSED':
				style = 'default';
				break;
			default:
				return null;
		}

		return <Label bsStyle={style}>{this.props.state.name}</Label>;
	}
}

CaseState.propTypes = {
	state: React.PropTypes.object.isRequired
};
