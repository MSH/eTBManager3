import React from 'react';
import { Alert } from 'react-bootstrap';
import { MessageDlg } from '../../components';

export default class CrudMessage extends React.Component {

	constructor(props) {
		super(props);

		this.hideMessage = this.hideMessage.bind(this);
		this.confirmDlgClose = this.confirmDlgClose.bind(this);
	}

	componentWillMount() {
		const self = this;

		const handler = this.props.controller.on((evt, data) => {
			if (evt === 'show-msg') {
				self.setState({ msg: data });
				return;
			}

			if (evt === 'confirm-delete') {
				self.setState({ confirm: data });
				return;
			}
		});
		self.setState({ handler: handler });
	}

	componentWillUnmount() {
		this.props.controller.removeListener(this.state.handler);
	}

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
