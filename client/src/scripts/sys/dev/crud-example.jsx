
import React from 'react';
import { Row, Col, Button } from 'react-bootstrap';
import { Card, Profile } from '../../components/index';

import { CrudController, CrudTable, CrudPagination, CrudForm, CrudMessage } from '../crud';

import { generateName, mockCrud } from '../mock-data';

// definition of the form fields to edit substances
const editorDef = {
	layout: [
		{
			property: 'shortName',
			required: true,
			type: 'string',
			max: 20,
			label: __('form.shortName'),
			size: { sm: 3 }
		},
		{
			property: 'name',
			required: true,
			type: 'string',
			max: 200,
			label: __('form.name'),
			size: { sm: 6 }
		},
		{
			property: 'customId',
			type: 'string',
			max: 20,
			label: __('form.customId'),
			size: { sm: 3 }
		},
		{
			property: 'active',
			type: 'yesNo',
			label: __('EntityState.ACTIVE'),
			size: { sm: 5 },
			defaultValue: true
		}
	],
	title: doc => doc && doc.id ? __('admin.sources.edit') : __('admin.sources.new')
};


/**
 * Initial page that declare all routes of the module
 */
export default class CrudExample extends React.Component {

	constructor(props) {
		super(props);

		this.state = { };
	}

	componentWillMount() {
		const crud = mockCrud();
		crud.on(this.crudEvent);

		const controller = new CrudController(crud, { pageSize: 20 });
		controller.initList();

		this.setState({ controller: controller });
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
					name: res.name,
					gender: res.gender,
					status: 'Status of ' + res.name,
					quantity: Math.random() * 1000 + 1000
				});
			}
			return { count: 843, list: lst };
		}

		if (evt === 'get-edit') {
			return null;
		}
		return null;
	}


	collapseRender(item) {
		return (<div>
					<hr/>
						<dl className="text-small dl-horizontal text-muted">
							<dt>{'Patient: '}</dt>
							<dd>{item.name}</dd>
							<dt>{'Status: '}</dt>
							<dd>{item.status}</dd>
							<dt>{'Quantity: '}</dt>
							<dd>{item.quantity.toLocaleString('en', { maximumFractionDigits: 2 })}</dd>
						</dl>
					<hr/>
				</div>);
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

		return (
			<div>
				<CrudForm controller={controller} schema={editorDef} />
				<Card title="CRUD Table">
					<Button className="pull-right" onClick={controller.openNewForm}>
						{__('action.add')}
					</Button>
					<Row>
						<Col md={12}>
							<CrudMessage controller={controller} />
							<CrudPagination controller={controller} showCounter />
							<CrudTable columns={columns} controller={controller} />
							<CrudPagination controller={controller} />
						</Col>
					</Row>
				</Card>
			</div>
			);
	}
}
