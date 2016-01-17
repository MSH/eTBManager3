
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
						required: true,
						type: 'yesNo',
						label: __('EntityState.ACTIVE'),
						defaultValue: true,
						size: { sm: 4 }
					},
					{
						el: 'subtitle',
						label: 'Unit address',
						size: { sm: 12 }
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
						property: 'address.adminUnit',
						label: __('AdministrativeUnit'),
						type: 'adminUnit',
						required: true,
						size: { sm: 6 }
					},
					{
						property: 'address.zipCode',
						label: __('Address.zipCode'),
						type: 'string',
						size: { sm: 3 }
					},
					{
						property: 'customId',
						type: 'string',
						label: __('form.customId'),
						size: { sm: 3 }
					},
					{
						property: 'receiveFromManufacturer',
						type: 'bool',
						label: __('Unit.receiveFromManufacturer')
					},
					{
						property: 'tbFacility',
						type: 'bool',
						label: __('Tbunit.tbFacility')
					},
					{
						property: 'drtbFacility',
						type: 'bool',
						label: __('Tbunit.drtbFacility')
					},
					{
						property: 'ntmFacility',
						type: 'bool',
						label: __('Tbunit.ntmFacility')
					},
					{
						property: 'notificationUnit',
						type: 'bool',
						label: __('Tbunit.notificationUnit')
					},
					{
						el: 'subtitle',
						label: 'Medicine order settings',
						size: { sm: 12 }
					},
					{
						property: 'supplier',
						type: 'unit',
						label: __('Unit.supplier'),
						size: { sm: 6 }
					},
					{
						property: 'authorizer',
						type: 'unit',
						label: __('Unit.authorizer'),
						size: { sm: 6 }
					},
					{
						property: 'numDaysOrder',
						type: 'int',
						label: __('Tbunit.numDaysOrder'),
						defaultValue: 120,
						size: { sm: 8 },
						controlSize: 4
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
						type: 'yesNo',
						label: __('EntityState.ACTIVE'),
						defaultValue: true,
						size: { sm: 4 }
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
						property: 'address.adminUnit',
						label: __('AdministrativeUnit'),
						type: 'adminUnit',
						size: { sm: 6 }
					},
					{
						property: 'address.zipCode',
						label: __('Address.zipCode'),
						type: 'string',
						size: { sm: 3 }
					},
					{
						property: 'customId',
						type: 'string',
						label: __('form.customId'),
						size: { sm: 3 }
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
