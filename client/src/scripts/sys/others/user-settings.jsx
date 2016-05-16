
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { Card, FormDialog } from '../../components';


export default class UserSettings extends React.Component {
	render() {
		const schema = {
			layout: [
				{
					property: 'name',
					label: 'Name',
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
					label: __('user.email'),
					type: 'string',
					required: true,
					size: { md: 12, newLine: true }
				},
				{
					property: 'timeZone',
					label: __('User.timeZone'),
					type: 'select',
					options: [
						{ id: '1', name: 'Value 1' },
						{ id: '2', name: 'Value 2' },
						{ id: '3', name: 'Value 3' }
					]
				}
			]
		};

		const doc = {};

		return (
			<Grid fluid>
				<Row className="mtop-2x">
					<Col md={5} mdOffset={3}>
						<Card title={__('usersettings')}>
							<FormDialog schema={schema} doc={doc} hideCancel />
						</Card>
					</Col>
				</Row>
			</Grid>
			);
	}
}

UserSettings.propTypes = {

};
