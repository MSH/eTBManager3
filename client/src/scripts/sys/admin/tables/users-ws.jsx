
import React from 'react';
import CrudView from '../../crud/crud-view';
import CRUD from '../../../commons/crud';
import Profile from '../../../components/profile';


const crud = new CRUD('userws');

// definition of the form fields to edit substances
const editorDef = {
	layout: [
		{
			property: 'login',
			required: true,
			type: 'string',
			textCase: 'uppercase',
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
			defaultValue: true,
			size: { sm: 6 }
		},
		{
			property: 'unitId',
			required: true,
			type: 'unit',
			label: __('UserWorkspace.unit'),
			refreshOnChange: 'view',
			size: { sm: 6 }
		},
		{
			property: 'comments',
			type: 'string',
			max: 200,
			label: __('global.comments'),
			size: { sm: 6 }
		},
		{
			property: 'playOtherUnits',
			type: 'bool',
			label: __('UserWorkspace.playOtherUnits'),
			size: { sm: 6 }
		},
		{
			property: 'administrator',
			type: 'bool',
			label: __('UserWorkspace.administrator'),
			size: { sm: 6 }
		},
		{
			property: 'profiles',
			type: 'multiSelect',
			options: 'profiles',
			label: __('UserWorkspace.profiles'),
			size: { sm: 12 },
			required: true
		},
		{
			id: 'view',
			property: 'view',
			type: 'select',
			options: 'userViews',
			label: __('UserView'),
			params: {
				unitId: doc => doc.unitId
			},
			required: true
		}
	],
	title: doc => doc && doc.id ? __('admin.users.edt') : __('admin.users.new')
};


/**
 * The page controller of the public module
 */
export default class UsersWs extends React.Component {

	constructor(props) {
		super(props);
		this.options = this.options.bind(this);
		this.execOption = this.execOption.bind(this);
		this.blockUnblockUser = this.blockUnblockUser.bind(this);
		this.state = {
			options: [
				{
					label: 'Send new password',
					onClick: this.execOption
				},
				{
					label: 'Change password',
					onClick: this.execOption
				},
				{
					label: 'Block/Unblock user',
					onClick: this.blockUnblockUser
				}
			]
		};
	}

	blockUnblockUser(index, item) {
		const doc = { state: true };
		crud.update(item.id, doc);
	}

	execOption(index) {
		alert('Not implemented: ' + index);
	}

	options(item) {
		return [
				{
					label: 'Send new password',
					onClick: this.execOption
				},
				{
					label: 'Change password',
					onClick: this.execOption
				},
				{
					label: 'Block/Unblock user',
					onClick: this.blockUnblockUser
				}
			];
	}

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
				pageSize={50}
				options={this.options}
				title={data.title}
				editorSchema={editorDef}
				onCellRender={this.cellRender}
				perm={data.perm} />
			);
	}
}

UsersWs.propTypes = {
	route: React.PropTypes.object
};
