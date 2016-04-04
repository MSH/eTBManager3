
import React from 'react';
import { Input } from 'react-bootstrap';

export default class BoolControl extends React.Component {

	static typeName() {
		return 'bool';
	}

	static defaultValue() {
		return false;
	}

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
	}

	onChange() {
		const value = this.refs.input.getChecked();

		this.props.onChange({ schema: this.props.schema, value: value });
	}

	render() {
		const sc = this.props.schema;

		return	(
			<Input ref="input"
				label={sc.label}
				type="checkbox"
				onChange={this.onChange}
				help={sc.errors}
				checked={this.props.value}
				bsStyle={sc.errors ? 'error' : null} />
			);
	}
}


BoolControl.propTypes = {
	value: React.PropTypes.bool,
	schema: React.PropTypes.object,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	resources: React.PropTypes.any
};
