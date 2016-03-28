
import React from 'react';
import { Row, Col, Button } from 'react-bootstrap';
import { Card, TableForm } from '../../components/index';
import { app } from '../../core/app';

const fschema = {
			layout: [
				{
					property: 'iniDate',
					required: true,
					type: 'date',
					label: __('Period.iniDate'),
					size: { md: 3 }
				},
				{
					property: 'action',
					required: false,
					type: 'select',
					label: __('form.action'),
					options: app.getState().app.lists.CommandAction,
					size: { md: 3 }
				},
				{
					property: 'userId',
					required: false,
					type: 'select',
					label: __('User'),
					options: 'users',
					size: { md: 3 }
				},
				{
					property: 'type',
					required: false,
					type: 'string',
					max: 50,
					label: __('admin.reports.cmdhistory.cmdevent'),
					size: { sm: 3 }
				}
			]
		};

/**
 * Initial page that declare all routes of the module
 */
export default class TableFormExample extends React.Component {

	constructor(props) {
		super(props);

		this.state = { rowsQuantity: 1, docs: [] };
		this.addRow = this.addRow.bind(this);
		this.remRow = this.remRow.bind(this);
		this.doSomething = this.doSomething.bind(this);
	}

	addRow() {
		var quantity = this.state.rowsQuantity + 1;
		this.setState({ rowsQuantity: quantity });
	}

	remRow() {
		if (this.state.rowsQuantity > 1) {
			var quantity = this.state.rowsQuantity - 1;
			this.setState({ rowsQuantity: quantity });
		}
	}

	doSomething() {
		alert('I will try to do something, but first will validate the form');

		const valid = this.refs.tableform.validate();
		if (valid !== true) {
			alert('Ouch! Form is invalid');
			return;
		}

		alert('Form is Valid, what I have to do?');
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

		return (
			<div>
				<Card title="Form Table">
					<Row>
						<Col md={12}>
							<TableForm ctitles={ctitles}
								fschema={fschema}
								rowsQuantity={this.state.rowsQuantity}
								addRow={this.addRow}
								remRow={this.remRow}
								docs={this.state.docs}
								ref="tableform" />
						</Col>
					</Row>
					<Row>
						<Col md={12}>
							<Button onClick={this.doSomething} bsStyle="primary">{'Validate'}</Button>
						</Col>
					</Row>
				</Card>
			</div>
			);
	}
}
