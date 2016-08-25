import React from 'react';

export default class SubtitleControl extends React.Component {

	static typeName() {
		return 'subtitle';
	}

	render() {
		const className = this.props.schema.level ? 'subtitle' + this.props.schema.level : 'subtitle';
		return <div className={className}>{this.props.schema.label}</div>;
	}
}

SubtitleControl.propTypes = {
	schema: React.PropTypes.object.isRequired
};
