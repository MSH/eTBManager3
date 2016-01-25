
import React from 'react';
import CRUD from '../../commons/crud';
import CrudView from './crud-view';
import Form from '../../forms/form';


const crud = new CRUD('tag');

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
			property: 'line',
			required: true,
			type: 'string',
			options: 'MedicineLine',
			label: __('MedicineLine'),
			size: { sm: 6 }
		},
		{
			property: 'dstResultForm',
			type: 'bool',
			label: __('Substance.dstResultForm'),
			size: { newLine: true, sm: 6 },
			defaultValue: true
		},
		{
			property: 'prevTreatmentForm',
			type: 'bool',
			label: __('Substance.prevTreatmentForm'),
			size: { sm: 6 },
			defaultValue: true
		},
		{
			property: 'active',
			type: 'bool',
			label: __('EntityState.ACTIVE'),
			defaultValue: true
		},
		{
			property: 'customId',
			type: 'string',
			label: __('form.customId'),
			size: { sm: 3 }
		}
	],
	title: doc => doc && doc.id ? __('admin.substances.edt') : __('admin.substances.new')
};

/**
 * The page controller of the public module
 */
export default class Tags extends React.Component {

	constructor(props) {
		super(props);
		this.cellRender = this.cellRender.bind(this);
	}

	cellRender(item) {
		return item.name;
	}

	collapseCellRender(item) {
		return (
			<div>
				<hr/>
				<dl className="text-small dl-horizontal text-muted">
					<dt>{__('form.displayorder') + ':'}</dt>
					<dd>{item.displayOrder}</dd>
					<dt>{__('form.customId') + ':'}</dt>
					<dd>{item.customId}</dd>
				</dl>
				<hr/>
			</div>
			);
	}

	render() {
		// get information about the route of this page
		const data = this.props.route.data;

		return (
			<CrudView crud={crud}
				title={data.title}
				onCellRender={this.cellRender}
				onDetailRender={this.collapseCellRender}
				editorDef={editorDef}
				perm={data.perm} />
			);
	}
}

Tags.propTypes = {
	route: React.PropTypes.object
};
