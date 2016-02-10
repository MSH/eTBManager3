
import React from 'react';
import CrudView from '../crud-view';
import CRUD from '../../../commons/crud';
import Profile from '../../../components/profile';
import { app } from '../../../core/app';

const crud = new CRUD('product');

// definition of the form fields to edit substances
const editorDef = {
	editors: {
		'PRODUCT': {
			label: __('Product'),
			defaultProperties: {
				type: 'PRODUCT'
			},
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
					property: 'active',
					type: 'bool',
					label: __('EntityState.ACTIVE'),
					defaultValue: true,
					required: true
				},
				{
					property: 'customId',
					type: 'string',
					label: __('form.customId'),
					size: { sm: 3 }
				}
			],
			title: doc => doc.id ? __('admin.products.edt') : __('admin.products.new')
		},
		'MEDICINE': {
			label: __('Medicine'),
			defaultProperties: {
				type: 'MEDICINE'
			},
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
					max: 50,
					label: __('form.customId'),
					size: { sm: 3 }
				},
				{
					property: 'active',
					type: 'yesNo',
					required: true,
					label: __('EntityState.ACTIVE'),
					defaultValue: true,
					size: { sm: 6 }
				},
				{
					property: 'category',
					type: 'select',
					required: true,
					options: app.getState().app.lists.MedicineCategory,
					label: __('MedicineCategory'),
					size: { sm: 6, newLine: true }
				},
				{
					property: 'line',
					type: 'select',
					required: true,
					options: app.getState().app.lists.MedicineLine,
					label: __('MedicineLine'),
					size: { sm: 6 }
				},
				{
					property: 'substances',
					label: __('Medicine.components'),
					type: 'multi-select',
					options: 'substances'
				}
			],
			title: doc => doc && doc.id ? __('admin.meds.edt') : __('admin.meds.new')
		}
	},
	select: item => item.type
};

/**
 * The page controller of the public module
 */
export default class Products extends React.Component {

	cellRender(item) {
		return (
			<Profile title={item.shortName} subtitle={item.name}
				size="small" type={item.type.toLowerCase()} />
			);
	}

	render() {
		// get information about the route of this page
		const data = this.props.route.data;

		return (
			<CrudView crud={crud}
				title={data.title}
				editorDef={editorDef}
				onCellRender={this.cellRender}
				perm={data.perm} />
			);
	}
}

Products.propTypes = {
	route: React.PropTypes.object
};
