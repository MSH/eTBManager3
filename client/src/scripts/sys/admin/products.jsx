
import React from 'react';
import CrudView from './crud-view';
import CRUD from '../../commons/crud';

const crud = new CRUD('product');

// definition of the form fields to edit substances
const editorDef = {
	editors: {
		'PRODUCT': {
			label: __('Product'),
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
					label: __('EntityState.ACTIVE')
				},
				{
					property: 'customId',
					type: 'string',
					label: __('form.customId'),
					size: { sm: 3 }
				}
			],
			title: doc => doc && doc.id ? __('admin.products.edt') : __('admin.products.new')
		},
		'MEDICINE': {
			label: __('Medicine'),
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
			<div>
				<b>{item.shortName}</b>
				<div className="text-muted">{item.name}</div>
			</div>
			);
	}

	render() {
		// get information about the route of this page
		const data = this.props.route.data;

//		tableDef.title = data.title;

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
