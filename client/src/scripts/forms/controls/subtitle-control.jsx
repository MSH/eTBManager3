import React from 'react';
import formControl from './form-control';

class SubtitleControl extends React.Component {

	static typeName() {
		return 'subtitle';
	}

	render() {
		return <div className="subtitle">{this.props.schema.label}</div>;
	}
}

SubtitleControl.propTypes = {
	schema: React.PropTypes.object.isRequired
};

export default formControl(SubtitleControl);
