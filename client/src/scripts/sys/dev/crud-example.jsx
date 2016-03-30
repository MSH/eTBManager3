
import React from 'react';
import { Button } from 'react-bootstrap';
import { Card, Profile } from '../../components/index';

import { CrudController, CrudTable, CrudPagination, CrudForm, CrudMessage, CrudGrid } from '../crud';

import { generateName, mockCrud } from '../mock-data';

// definition of the form fields to edit substances
const editorDef = {
	layout: [
		{
			property: 'name',
			required: true,
			type: 'string',
			max: 20,
			label: __('form.shortName'),
			size: { sm: 6 }
		},
		{
			property: 'status',
			required: true,
			type: 'select',
			label: 'Status',
			options: 'substances',
			size: { sm: 3 }
		},
		{
			property: 'quantity',
			type: 'number',
			label: 'Quantity',
			size: { sm: 3 }
		}
	],
	title: doc => doc && doc.id ? 'Editing record' : 'New record'
};


/**
 * Initial page that declare all routes of the module
 */
export default class CrudExample extends React.Component {

	constructor(props) {
		super(props);

		this.openNewForm = this.openNewForm.bind(this);

		this.state = { };
	}

	componentWillMount() {
		const crud = mockCrud();
		crud.on(this.crudEvent);

		const controller = new CrudController(crud, { pageSize: 20 });
		controller.initList();
		const controller2 = new CrudController(crud, { pageSize: 20 });
		controller2.initList();

		this.setState({ controller: controller, controller2: controller2 });
	}

	rowClick(item) {
		alert(item.name);
	}

	crudEvent(evt) {
		if (evt === 'query') {
			const lst = [];
			for (var i = 0; i < 20; i++) {
				const res = generateName();
				lst.push({
					id: '12345-' + i,
					name: res.name,
					gender: res.gender,
					status: 'Status of ' + res.name,
					quantity: Math.random() * 1000 + 1000
				});
			}
			return { count: 843, list: lst };
		}

		if (evt === 'get-edit') {
			return {
				name: 'Teste',
				gender: 'MALE',
				status: 'test',
				quantity: 10
			};
		}
		return null;
	}


	expandRender(item) {
		return (<div>
					<dl className="dl-horizontal">
						<dt>{'Patient: '}</dt>
						<dd>{item.name}</dd>
						<dt>{'Status: '}</dt>
						<dd>{item.status}</dd>
						<dt>{'Quantity: '}</dt>
						<dd>{item.quantity.toLocaleString('en', { maximumFractionDigits: 2 })}</dd>
					</dl>
				</div>);
	}

	openNewForm() {
		this.state.controller.openForm();
	}

	gridCellRender(item) {
		return (
			<Profile size="small" title={item.name} type="user" />
			);
	}

	render() {
		// the columns of the table
		const columns = [
			{
				title: 'Patient',
				content: item => <Profile size="small" title={item.name} type="user" />,
				size: { sm: 6 }
			},
			{
				title: 'Status (center alignment)',
				content: 'status',
				size: { sm: 3 },
				align: 'center'
			},
			{
				title: 'Quantity',
				content: item => item.quantity.toLocaleString('en', { maximumFractionDigits: 2 }),
				size: { sm: 3 },
				align: 'right'
			}
		];

		const controller = this.state.controller;
		const ctrl2 = this.state.controller2;

		return (
			<div>
				<CrudForm controller={controller} schema={editorDef} openOnNew />
				<Card title="CRUD Table">
					<Button className="pull-right" onClick={this.openNewForm}>
						{__('action.add')}
					</Button>
					<CrudMessage controller={controller} />
					<CrudPagination controller={controller} showCounter />
					<CrudTable columns={columns}
						editorSchema={editorDef}
						controller={controller}
						onExpandRender={this.expandRender} />
					<CrudPagination controller={controller} />
				</Card>

				<Card title="CRUD grid">
					<CrudMessage controller={ctrl2} />
					<CrudPagination controller={ctrl2} showCounter />
					<CrudGrid controller={ctrl2}
						onRender={this.gridCellRender}
						onExpandRender={this.expandRender}
						editorSchema={editorDef} />
					<CrudPagination controller={ctrl2} />
				</Card>
			</div>
			);
	}
}
