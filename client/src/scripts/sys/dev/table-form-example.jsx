
import React from 'react';
import { Row, Col, Button } from 'react-bootstrap';
import { Card } from '../../components/index';
import { app } from '../../core/app';
import Form from '../../forms/form';

const tfschema = {
			layout: [
				{
					property: 'iniDate',
					required: true,
					type: 'date',
					label: __('Period.iniDate'),
					size: { md: 4 }
				},
				{
					property: 'action',
					required: false,
					type: 'listBox',
					label: __('form.action'),
					options: app.getState().app.lists.CommandAction, //TODOMS se fosse lista dinamica, o table-form vai fazer a quantidade de linhas em requisições para preencher, enquanto era necessária apenas uma
					size: { md: 4 }
				},
				{
					property: 'type',
					required: false,
					type: 'string',
					max: 50,
					label: __('admin.reports.cmdhistory.cmdevent'),
					size: { md: 4 }
				}
			]
		};

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
		const ctitles = [
			{
				title: __('Period.iniDate'),
				size: { sm: 3 },
				align: 'center'
			},
			{
				title: __('form.action'),
				size: { sm: 3 },
				align: 'center'
			},
			{
				title: __('User'),
				size: { sm: 3 },
				align: 'center'
			},
			{
				title: __('admin.reports.cmdhistory.cmdevent'),
				size: { sm: 3 },
				align: 'center'
			}
		];


		const fschema = {
					defaultProperties: {
						formlist: [] //TODOMSR: vide regimens.jsx
					},
					layout: [
						{
							property: 'type',
							required: false,
							type: 'string',
							max: 50,
							label: __('admin.reports.cmdhistory.cmdevent'),
							size: { sm: 6 }
						},
						{
							property: 'formlist',
							type: 'tableform',
							fschema: tfschema,
							ctitles: ctitles,
							iniRowsQtt: 2,
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
