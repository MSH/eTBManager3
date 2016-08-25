
import React from 'react';
import { Row, Col, Button } from 'react-bootstrap';
import { Card, Profile } from '../../components/index';
import Form from '../../forms/form';
import moment from 'moment';

/**
 * Initial page that declare all routes of the module
 */
export default class TableFormExample extends React.Component {

	constructor(props) {
		super(props);

		this.state = { doc: {} };
		this.validate = this.validate.bind(this);
		this.clearIt = this.clearIt.bind(this);
		this.onChangeDoc = this.onChangeDoc.bind(this);
	}

	validate() {
		const self = this;

		const errors = self.refs.form.validate();
		this.setState({ errors: errors });
		if (errors) {
			return;
		}

		alert('form is valid');
	}

	clearIt() {
		this.setState({ doc: {} });
		this.forceUpdate();
	}

	onChangeDoc(doc) {
		this.forceUpdate();
		// display object in the console
		console.log(doc);
	}

	render() {

		// the columns of the table
		const readOnlyColumns = [
			{
				title: 'iniDate',
				content: item => <span>{moment(item.iniDate).format('L LT')}</span>,
				size: { sm: 6 }
			},
			{
				title: 'userName',
				content: item => <Profile size="small" title={item.userName} type="user" />,
				size: { sm: 6 }
			}
		];

		// The schema of table form
		const tfschema = {
			controls: [
				{
					property: 'iniDate',
					required: true,
					type: 'date',
					label: __('Period.iniDate'),
					size: { md: 4 }
				},
				{
					property: 'userName',
					required: false,
					type: 'string',
					label: __('User'),
					size: { md: 4 }
				}
			]
		};

		// the form schema
		const fschema = {
					defaultProperties: {
						type: 'Type test default prop',
						formList: [{ iniDate: new Date(), userName: 'Mauricio' }, { iniDate: new Date(), userName: 'Jesus' }, { iniDate: new Date(), userName: 'Santos' }]
					},
					controls: [
						{
							property: 'type',
							required: false,
							type: 'string',
							max: 50,
							label: __('admin.reports.cmdhistory.cmdevent'),
							size: { sm: 6 }
						},
						{
							property: 'formList',
							type: 'tableForm',
							fschema: tfschema,
							readOnlyColumns: readOnlyColumns,
							min: 2,
							max: 5,
							size: { sm: 12 }
						}
					]
				};

		return (
			<div>
				<Card title="Form Table">
					<Row>
						<Col md={12}>
							<Form ref="form"
								schema={fschema}
								doc={this.state.doc}
								onChange={this.onChangeDoc}
								errors={this.state.errors} readOnly/>
						</Col>
					</Row>
				</Card>
				<Card title="Form Table">
					<Row>
						<Col md={12}>
							<Form ref="form"
								schema={fschema}
								doc={this.state.doc}
								onChange={this.onChangeDoc}
								errors={this.state.errors} />
						</Col>
					</Row>
					<Row>
						<Col md={12}>
							<Button onClick={this.validate} bsStyle="primary">{'Validate'}</Button>
						</Col>
					</Row>
				</Card>
				<Card title="Document">
					<div>
					{JSON.stringify(this.state.doc, null, '    ')}
					</div>
					<Button onClick={this.clearIt}>{'Clear it'}</Button>
				</Card>
				{
					this.state.errors ?
					<Card title="Errors">
						{JSON.stringify(this.state.errors, null, '    ')}
					</Card> : null
				}
			</div>
			);
	}
}
