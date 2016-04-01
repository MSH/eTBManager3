import React from 'react';
import { Grid, Col, Row } from 'react-bootstrap';
import { Card, FormDialog } from '../../../components';

export default class SysSetup extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			schema: {
				layout: [
					{
						property: 'systemURL',
						label: __('SystemConfig.systemURL'),
						type: 'string',
						max: 200,
						size: { sm: 12 }
					},
					{
						property: 'pageRootURL',
						label: __('SystemConfig.pageRootURL'),
						type: 'string',
						max: 200,
						size: { sm: 12 }
					},
					{
						property: 'systemMail',
						label: __('SystemConfig.systemMail'),
						type: 'string',
						max: 200,
						size: { sm: 12 }
					},
					{
						property: 'adminMail',
						label: __('SystemConfig.adminMail'),
						type: 'string',
						max: 200,
						size: { sm: 12 }
					},
					{
						property: 'allowRegPage',
						label: __('SystemConfig.allowRegPage'),
						type: 'bool',
						max: 200,
						size: { sm: 12 }
					},
					{
						property: 'workspaceId',
						label: __('Workspace'),
						type: 'select',
						options: 'workspaces',
						visible: doc => doc.allowRegPage,
						size: { sm: 7 }
					},
					{
						property: 'tbunit',
						label: __('Unit'),
						type: 'unit',
						workspaceId: doc => doc.workspaceId,
						visible: doc => !!doc.workspaceId,
						size: { sm: 7 }
					}
				]
			}
		};
	}

	render() {
		return (
				<Grid fluid>
					<Row>
						<Col sm={8}>
			<Card title={this.props.route.data.title}>
							<FormDialog
								wrapType={'none'}
								schema={this.state.schema}
								doc={{}}
							/>
			</Card>
						</Col>
					</Row>
				</Grid>
			);
	}
}

SysSetup.propTypes = {
	route: React.PropTypes.object
};
