
import React from 'react';
import CRUD from '../../commons/crud';
import CrudView from './crud-view';
import Profile from '../../components/profile';

const crud = new CRUD('unit');

// definition of the form fields to edit substances
const editorDef = {
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
			property: 'line',
			required: true,
			type: 'string',
			options: 'MedicineLine',
			label: __('MedicineLine'),
			size: { sm: 6 }
		},
		{
			property: 'active',
			type: 'boolean',
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
export class Units extends React.Component {

	cellRender(item) {
		return (
			<Profile type={item.type.toLowerCase()}
				title={item.name}
				subtitle={item.adminUnit.p0.name}
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
