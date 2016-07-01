
import React from 'react';
import CrudView from '../../crud/crud-view';
import CRUD from '../../../commons/crud';
import Profile from '../../../components/profile';
import { Label, Alert } from 'react-bootstrap';

import UserWsChangePwd from './user-ws-changepwd';
import { server } from '../../../commons/server';
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
		this.state = { userChangePwd: null };

		this.options = this.options.bind(this);

		this.sendNewPassword = this.sendNewPassword.bind(this);
		this.blockUnblockUser = this.blockUnblockUser.bind(this);
		this.openChangePassword = this.openChangePassword.bind(this);
		this.closeChangePassword = this.closeChangePassword.bind(this);
	}

	options(item) {
		const options = [
				{
					label: 'Send new password',
					onClick: this.sendNewPassword
				},
				{
					label: 'Change password',
					onClick: this.openChangePassword
				},
				{
					label: item.active === false ? 'Unblock user' : 'Block user',
					onClick: this.blockUnblockUser
				}
			];

		return options;
	}

	sendNewPassword(index, item) {
		const self = this;
		const doc = {userWsId: item.id};
		
		return server.post('/api/tbl/userws/resetpwd', doc)
		.then(res => {
			if (res && res.errors) {
				return Promise.reject(res.errors);
			}
			const msg = __('changepwd.emailsent') + ' ' + item.name;
			self.setState({ infoMsg: msg });
			setTimeout(() => { self.setState({ infoMsg: null }); }, 4000);
			return true;
		});
	}

	blockUnblockUser(index, item, cell) {
		const newState = !item.active;
		const doc = { active: newState };
		crud.update(item.id, doc)
		.then(() => {
				item.active = newState;
				cell.forceUpdate();
			});
	}

	openChangePassword(index, item) {
		this.setState({ userChangePwd: item });
	}

	closeChangePassword(res) {
		this.setState({ userChangePwd: null, infoMsg: res });
		const self = this;
		setTimeout(() => { self.setState({ infoMsg: null }); }, 4000);
	}

	cellRender(item) {
		const sub = (
			<div>
				<div>
					{item.unit.name}
				</div>
			</div>
			);

		return (
			<div>
				<div className="pull-right">
					{
						!item.active && <Label className="mright" bsStyle="danger">{'Blocked'}</Label>
					}
					{
						item.passwordExpired && <Label className="mright" bsStyle="warning">{'Password Expired'}</Label>
					}
					{
						!item.emailConfirmed && <Label className="mright" bsStyle="warning">{'Waiting E-mail Validation'}</Label>
					}
				</div>
				<Profile type="user" title={item.name} subtitle={sub} size="small"/>
			</div>
			);
	}

	render() {
		// get information about the route of this page
		const data = this.props.route.data;

		return (
			<div>

				{
					!!this.state.infoMsg &&
					<Alert bsStyle="success">{this.state.infoMsg}</Alert>
				}

				<CrudView crud={crud}
					pageSize={50}
					options={this.options}
					title={data.title}
					editorSchema={editorDef}
					onCellRender={this.cellRender}
					perm={data.perm} />

				<UserWsChangePwd userws={this.state.userChangePwd}
					show={!!this.state.userChangePwd}
					onClose={this.closeChangePassword} />
			</div>
			);
	}
}

UsersWs.propTypes = {
	route: React.PropTypes.object
};
