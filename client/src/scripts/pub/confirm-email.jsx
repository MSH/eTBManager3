import React from 'react';
import SetPassword from './set-password';


export default class ConfirmEmail extends React.Component {

	render() {
		const token = this.props.route.params.id;
		if (!token) {
			return null;
		}

		return (
			<SetPassword
				title={__('action.enterpwd')}
				comments={__('changepwd.msg.1')}
				token={token}
			/>);
	}
}

ConfirmEmail.propTypes = {
	route: React.PropTypes.object
};
