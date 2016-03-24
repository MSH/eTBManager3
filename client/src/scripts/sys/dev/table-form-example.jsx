
import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { Card, TableForm } from '../../components/index';
import { app } from '../../core/app';

import { generateName } from '../mock-data';

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

		this.state = { rowsQuantity: 1, docs: [], errorsarr: [] };
		this.addRow = this.addRow.bind(this);
		this.remRow = this.remRow.bind(this);
	}

	addRow() {
		var quantity = this.state.rowsQuantity + 1;
		this.setState({ rowsQuantity: quantity });
	}

	remRow() {
		var quantity = this.state.rowsQuantity - 1;
		this.setState({ rowsQuantity: quantity });
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
							<TableForm
								fschema={fschema}
								rowsQuantity={this.state.rowsQuantity}
								addRow={this.addRow}
								remRow={this.remRow}
								docs={this.state.docs}
								errorsarr={this.state.errorsarr} />
						</Col>
					</Row>
				</Card>
			</div>
			);
	}
}
