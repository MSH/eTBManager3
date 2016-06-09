
import React from 'react';
import { Grid, Row, Col, Alert } from 'react-bootstrap';
import { Card, FormDialog, WaitIcon } from '../../components';
import { server } from '../../commons/server';


export default class UserSettings extends React.Component {

	constructor(props) {
		super(props);

		this.saveSettings = this.saveSettings.bind(this);
	}

	componentWillMount() {
		const self = this;

		// get user settings
		server.get('/api/sys/usersettings')
		.then(res => {
			self.setState({ doc: res });
		});

		this.setState({ doc: null });
	}


	saveSettings() {
		// hide any message, if displayed
		this.setState({ msg: null });

		// save preferences
		const self = this;

		return server.post('/api/sys/usersettings', this.state.doc)
		.then(res => {
			if (res && res.errors) {
				return Promise.reject(res.errors);
			}
			self.setState({ msg: 1 });
			return true;
		});
	}

	render() {
		const doc = this.state.doc;

		if (!doc) {
			return <WaitIcon type="card" />;
		}

		const schema = {
			layout: [
				{
					property: 'name',
					label: __('User.name'),
					type: 'string',
					required: true,
					size: { md: 12 }
				},
				{
					property: 'mobile',
					label: __('global.mobile'),
					type: 'string',
					required: false,
					size: { md: 6, newLine: true }
				},
				{
					property: 'email',
					label: __('User.email'),
					type: 'string',
					required: true,
					size: { md: 12, newLine: true }
				},
				{
					property: 'timeZone',
					label: __('User.timeZone'),
					type: 'select',
					options: 'timeZones'
				}
			]
		};

		return (
			<Grid fluid>
				<Row className="mtop-2x">
					<Col md={5} mdOffset={3}>
						<Card title={__('usersettings')}>
							{
								this.state.msg &&
								<Alert bsStyle="success">{__('usersettings.savemsg')}</Alert>
							}
							<FormDialog schema={schema} doc={doc}
								hideCancel
								onConfirm={this.saveSettings} />
						</Card>
					</Col>
				</Row>
			</Grid>
			);
	}
}

UserSettings.propTypes = {

};
