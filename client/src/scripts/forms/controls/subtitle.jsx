import React from 'react';
import FormUtils from '../form-utils';

export default class Subtitle extends React.Component {

	static elementName() {
		return 'subtitle';
	}

	static snapshot(snapshot, doc) {
		FormUtils.propEval(snapshot, 'label', doc);
	}

	render() {
		return <div className="subtitle">{this.props.schema.label}</div>;
	}
}

Subtitle.propTypes = {
	schema: React.PropTypes.object.isRequired
};
