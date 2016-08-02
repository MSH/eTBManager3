
import React from 'react';
import { Grid, Row, Col, Alert } from 'react-bootstrap';
import { Card, FormDialog } from '../../components';
import { server } from '../../commons/server';

const schema = {
	controls: [
		{
			property: 'password',
			label: __('changepwd.passinuse'),
			type: 'string',
			required: true,
			password: true,
			size: { md: 12 }
		},
		{
			property: 'newPassword',
			label: __('changepwd.newpass'),
			type: 'string',
			required: true,
			password: true,
			size: { md: 12 }
		},
		{
			property: 'repeatNewPassword',
			label: __('changepwd.newpass2'),
			type: 'string',
			required: true,
			password: true,
			size: { md: 12 }
		}
	]
};

export default class ChangePassword extends React.Component {

	constructor(props) {
		super(props);

		this.saveNewPassword = this.saveNewPassword.bind(this);
		this.state = { doc: {} };
	}

	saveNewPassword() {
		// hide any message, if displayed
		this.setState({ msg: null });
		const doc = this.state.doc;

		if (doc.newPassword !== doc.repeatNewPassword) {
			return Promise.reject([{ field: 'repeatNewPassword', msg: __('changepwd.wrongpass2') }]);
		}

		// save new password
		const self = this;

		return server.post('/api/sys/changepassword', this.state.doc)
		.then(res => {
			if (res && res.errors) {
				return Promise.reject(res.errors);
			}
			self.setState({ msg: 1, doc: {} });
			return true;
		});
	}

	render() {
		return (
			<Grid fluid>
				<Row className="mtop-2x">
					<Col md={5} mdOffset={3}>
						<Card title={__('changepwd')}>
							{
								this.state.msg &&
								<Alert bsStyle="success">{__('changepwd.success1')}</Alert>
							}
							<FormDialog schema={schema} doc={this.state.doc}
								hideCancel
								onConfirm={this.saveNewPassword} />
						</Card>
					</Col>
				</Row>
			</Grid>
			);
	}
}

ChangePassword.propTypes = {

};
