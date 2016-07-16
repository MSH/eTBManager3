
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
			size: { sm: 6, newLine: true }
		},
		{
			property: 'repeatNewPassword',
			label: __('changepwd.newpass2'),
			type: 'string',
			required: true,
			password: true,
			size: { sm: 6, newLine: true }
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
		this.onCancel = this.onCancel.bind(this);
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
			self.props.onClose(__('changepwd.success1'));
			self.setState({doc: {}});
			return true;
		});
	}

	onCancel() {
		this.setState({doc: {}});
		this.props.onClose(null);
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
				onCancel={this.onCancel}
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
