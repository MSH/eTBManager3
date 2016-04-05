import React from 'react';
import formControl from './form-control';

export default class GroupControl extends React.Component {

	static typeName() {
		return 'group';
	}

	static children(snapshot) {
		return snapshot.layout;
	}

	render() {
		// actually it doesn't render anything
		return null;
	}
}

GroupControl.propTypes = {
	schema: React.PropTypes.object.isRequired
};
