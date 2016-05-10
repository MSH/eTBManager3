import React from 'react';
import { Alert } from 'react-bootstrap';
import { MessageDlg } from '../../components';
import controlWrapper from './crud-control-wrapper';

class CrudMessage extends React.Component {

	constructor(props) {
		super(props);

		this.hideMessage = this.hideMessage.bind(this);
		this.confirmDlgClose = this.confirmDlgClose.bind(this);

		this.state = { msg: null };
	}

	eventHandler(evt, data) {
		if (evt === 'show-msg') {
			this.setState({ msg: data });
			return;
		}

		if (evt === 'confirm-delete') {
			this.setState({ confirm: data });
			return;
		}
	}

	/**
	 * Hide the alert box displaying the message
	 */
	hideMessage() {
		this.setState({ msg: null });
	}

	confirmDlgClose(action) {
		if (action === 'yes') {
			this.props.controller.confirmDelete();
		}
		this.setState({ confirm: null });
	}

	render() {

		const msg = this.state.msg ?
			<Alert bsStyle="warning"
				onDismiss={this.hideMessage}
				style={{ marginTop: '10px', marginBottom: '10px' }}>
				{this.state.msg}
			</Alert> :
			null;

		const confirm = this.state.confirm;

		const confirmDlg = confirm ?
			<MessageDlg show
				title={confirm.title}
				message={confirm.msg}
				type="YesNo"
				onClose={this.confirmDlgClose} /> :
			null;

		return (
			<div>
				{msg}
				{confirmDlg}
			</div>
			);
	}
}

CrudMessage.propTypes = {
	controller: React.PropTypes.object.isRequired
};

export default controlWrapper(CrudMessage);
