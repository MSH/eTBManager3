
import React from 'react';
import { FormDialog } from '../../../components/index';
import { server } from '../../../commons/server';

const schema = {
	layout: [
		{
			property: 'newPassword',
			label: __('changepwd.newpass'),
			type: 'string',
			required: true,
			password: true,
			size: { md: 12 }
		},
		{
			property: 'repeatNewPassword',
			label: __('changepwd.newpass2'),
			type: 'string',
			required: true,
			password: true,
			validate: doc => doc.newPassword !== doc.repeatNewPassword ? 'BIRRRL' : 'MUTANTE',
			validateMessage: __('changepwd.wrongpass2'),
			size: { md: 12 }
		}
	]
};

/**
 * The page controller of the public module
 */
export default class UserWsChangePwd extends React.Component {

	constructor(props) {
		super(props);

		this.state = { doc: {} };
		this.changePassword = this.changePassword.bind(this);
	}

	changePassword() {
		const doc = this.state.doc;

		if (doc.newPassword !== doc.repeatNewPassword) {
			return Promise.reject([{ field: 'repeatNewPassword', msg: __('changepwd.wrongpass2') }]);
		}

		// set userId
		doc.userWsId = this.props.userws.id;

		// save password
		const self = this;

		return server.post('/api/tbl/userws/updatepwd', this.state.doc)
		.then(res => {
			if (res && res.errors) {
				return Promise.reject(res.errors);
			}
			self.props.onClose(true);
			return true;
		});
	}

	render() {
		if (!this.props.userws) {
			return null;
		}

		schema.title = __('changepwd') + ' - ' + this.props.userws.name;

		return (
			<FormDialog
				schema={schema}
				doc={this.state.doc}
				onConfirm={this.changePassword}
				onCancel={this.props.onClose}
				confirmCaption={__('changepwd')}
				wrapType={'modal'}
				modalShow={this.props.show}/>
		);
	}
}

UserWsChangePwd.propTypes = {
	userws: React.PropTypes.object,
	show: React.PropTypes.bool,
	onClose: React.PropTypes.func
};
