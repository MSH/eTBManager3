
import React from 'react';
import { Input } from 'react-bootstrap';
import formControl from './form-control';

class TextControl extends React.Component {

	static typeName() {
		return 'text';
	}

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
	}

	onChange() {
		const value = this.refs.input.getValue();

		this.props.onChange({ schema: this.props.schema, value: value });
	}

	render() {
		const sc = this.props.schema;

		return	(
			<Input ref="input"
				label={sc.label}
				type="textarea"
				onChange={this.onChange}
				help={sc.errors}
				value={this.props.value}
				style={{ minHeight: '100px' }}
				bsStyle={sc.errors ? 'error' : null} />
			);
	}
}


TextControl.propTypes = {
	value: React.PropTypes.string,
	schema: React.PropTypes.object,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any
};

export default formControl(TextControl);
