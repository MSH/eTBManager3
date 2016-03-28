import React from 'react';
import { Alert } from 'react-bootstrap';

export default class CrudMessage extends React.Component {

	constructor(props) {
		super(props);

		this.hideMessage = this.hideMessage.bind(this);
	}

	componentWillMount() {
		const self = this;

		const handler = this.props.controller.on((evt, data) => {
			if (evt === 'show-msg') {
				self.setState({ msg: data });
			}
		});
		self.setState({ handler: handler, msg: null });
	}

	componentWillUnmount() {
		this.props.controller.removeListener(this.state.handler);
	}

	hideMessage() {
		this.setState({ msg: null });
	}

	render() {
		return this.state.msg ?
			<Alert bsStyle="warning"
				onDismiss={this.hideMessage}
				style={{ marginTop: '10px', marginBottom: '10px' }}>
				{this.state.msg}
			</Alert> :
			null;
	}
}

CrudMessage.propTypes = {
	controller: React.PropTypes.object.isRequired
};
