
import React from 'react';
import CrudView from '../crud-view';
import CRUD from '../../../commons/crud';
import PermissionTree from './permission-tree';

const crud = new CRUD('userprofile');

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
			property: 'permissions',
			type: PermissionTree,
			size: { sm: 12 }
		}
	],
	title: doc => doc && doc.id ? __('admin.regimens.edt') : __('admin.regimens.new')
};


/**
 * The page controller of the public module
 */
export default class UserProfiles extends React.Component {

	cellRender(item) {
		return (
			<div>
				{item.name}
			</div>
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

UserProfiles.propTypes = {
	route: React.PropTypes.object
};
