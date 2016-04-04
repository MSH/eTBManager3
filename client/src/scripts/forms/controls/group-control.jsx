import React from 'react';
import formControl from './form-control';

class GroupControl extends React.Component {

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

export default formControl(GroupControl);
