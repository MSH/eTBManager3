
import React from 'react';
import CrudView from './crud-view';
import CRUD from '../../commons/crud';
import Profile from '../../components/profile';


const crud = new CRUD('userws');

// definition of the form fields to edit substances
const editorDef = {
	layout: [
		{
			property: 'login',
			required: true,
			type: 'string',
			max: 30,
			label: __('User.login'),
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
			property: 'email',
			required: true,
			type: 'string',
			email: true,
			max: 100,
			label: __('User.email'),
			size: { sm: 6 }
		},
		{
			property: 'sendSystemMessages',
			type: 'yesNo',
			label: __('User.sendSystemMessages'),
			size: { sm: 6 }
		}
	],
	title: doc => doc && doc.id ? __('admin.users.edt') : __('admin.users.new')
};


/**
 * The page controller of the public module
 */
export default class UsersWs extends React.Component {

	cellRender(item) {
		const sub = (
			<div>
				{item.unit.name}
			</div>
			);

		return (
			<Profile type="user" title={item.name} subtitle={sub} size="small"/>
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

UsersWs.propTypes = {
	route: React.PropTypes.object
};
