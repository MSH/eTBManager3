
import React from 'react';
import CRUD from '../../commons/crud';
import CrudView from './crud-view';
import { app } from '../../core/app';
import Form from '../../forms/form';


const crud = new CRUD('substance');

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
			options: app.getState().app.lists.MedicineLine,
			label: __('MedicineLine'),
			size: { sm: 6 }
		},
		{
			property: 'dstResultForm',
			type: 'yesNo',
			label: __('Substance.dstResultForm'),
			size: { newLine: true, sm: 4 },
			required: true,
			defaultValue: true
		},
		{
			property: 'prevTreatmentForm',
			type: 'yesNo',
			label: __('Substance.prevTreatmentForm'),
			size: { sm: 4 },
			required: true,
			defaultValue: true
		},
		{
			property: 'active',
			type: 'yesNo',
			label: __('EntityState.ACTIVE'),
			defaultValue: true,
			required: true,
			size: { sm: 4 }
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
export class Substances extends React.Component {

	constructor(props) {
		super(props);
		this.cellRender = this.cellRender.bind(this);

	}

	_itemClass(item) {
		switch (item.line) {
			case 'FIRST_LINE': return 'bg-success';
			case 'SECOND_LINE': return 'bg-danger';
			default: return 'bg-warning';
		}
	}

	cellRender(item) {
		const opts = app.getState().app.lists.MedicineLine;
		const medline = opts[item.line];
		const className = 'pull-right status-box text-small ' + this._itemClass(item);

		return (
			<div>
				<div className={className}>{medline}</div>
				<b>{item.shortName}</b>
				<div className="text-muted">{item.name}</div>
			</div>
			);
	}

	collapseCellRender(item) {
		const YesNo = Form.types.yesNo;
		return (
			<div>
				<hr/>
				<dl className="text-small dl-horizontal text-muted">
					<dt>{__('form.displayorder') + ':'}</dt>
					<dd>{item.displayOrder}</dd>
					<dt>{__('Substance.dstResultForm') + ':'}</dt>
					<dd><YesNo value={item.dstResultForm} noForm /></dd>
					<dt>{__('Substance.prevTreatmentForm') + ':'}</dt>
					<dd><YesNo value={item.prevTreatmentForm} noForm /></dd>
					<dt>{__('EntityState.ACTIVE') + ':'}</dt>
					<dd><YesNo value={item.active} noForm /></dd>
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

Substances.propTypes = {
	route: React.PropTypes.object
};
