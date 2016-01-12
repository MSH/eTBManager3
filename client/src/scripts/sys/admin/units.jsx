
import React from 'react';
import CRUD from '../../commons/crud';
import CrudView from './crud-view';
import Profile from '../../components/profile';
import Types from '../../forms/types';

const crud = new CRUD('unit');

// definition of the form fields to edit substances
const editorDef = {
	editors: {
		// TB units editor
		tbunit: {
			label: __('Tbunit'),
			layout: [
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
						defaultValue: true
					},
					{
						property: 'address.address',
						label: __('Address.address'),
						type: 'string',
						size: { newLine: true, sm: 6 }
					},
					{
						property: 'address.complement',
						label: __('Address.complement'),
						type: 'string',
						size: { sm: 6 }
					},
					{
						property: 'address.zipCode',
						label: __('Address.zipCode'),
						type: 'string',
						size: { sm: 3 }
					},
					{
						property: 'address.adminUnit',
						label: __('AdministrativeUnit'),
						type: 'adminUnit',
						size: { sm: 6 }
					},
					{
						property: 'customId',
						type: 'string',
						label: __('form.customId'),
						size: { sm: 3 }
					}
			],
			title: doc => doc && doc.id ? __('admin.tbunits.edt') : __('admin.tbunits.new')
		},
		lab: {
			label: __('Laboratory'),
			layout: [
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
						defaultValue: true
					},
					{
						property: 'customId',
						type: 'string',
						label: __('form.customId'),
						size: { sm: 3 }
					},
					{
						property: 'address.address',
						label: __('Address.address'),
						type: 'string'
					},
					{
						property: 'address.complement',
						label: __('Address.complement'),
						type: 'string'
					},
					{
						property: 'address.zipCode',
						label: __('Address.zipCode'),
						type: 'string',
						size: { sm: 6 }
					},
					{
						property: 'address.adminUnit',
						label: __('AdministrativeUnit'),
						type: 'adminUnit',
						size: { sm: 6 }
					}
			],
			title: doc => doc && doc.id ? __('admin.labs.edt') : __('admin.labs.new')
		}
	},
	select: item => item.type
};


/**
 * The page controller of the public module
 */
export default class Units extends React.Component {

	cellRender(item) {
		const auname = Types.list.adminUnit.displayText(item.adminUnit);

		return (
			<Profile type={item.type.toLowerCase()}
				title={item.name}
				subtitle={auname}
				size="small" />
			);
	}

	render() {
		// get information about the route of this page
		const data = this.props.route.data;

		return (
			<CrudView title={data.title}
				crud={crud}
				onCellRender={this.cellRender}
				editorDef={editorDef}
				perm={data.perm} />
			);
	}
}

Units.propTypes = {
	route: React.PropTypes.object
};
