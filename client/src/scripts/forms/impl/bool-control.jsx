
import React from 'react';
import { Input } from 'react-bootstrap';
import fieldControlWrapper from './field-control';

class BoolControl extends React.Component {

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
	}

	onChange() {
		const id = this.props.schema;
		const comp = this.refs[id];
		const value = comp.getChecked();

		this.props.onChange({ id: id, value: value });
	}

	render() {
		const sc = this.props.schema;

		return	(
			<Input ref={sc.id}
				label={sc.label}
				type="checkbox"
				onChange={this.onChange}
				help={sc.errors}
				checked={this.props.value}
				bsStyle={sc.errors ? 'error' : null} />
			);
	}
}


BoolControl.options = {
	supportedTypes: 'bool',
	defaultValue: false
};


BoolControl.propTypes = {
	value: React.PropTypes.bool,
	schema: React.PropTypes.object,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	resources: React.PropTypes.any
};

export default fieldControlWrapper(BoolControl);
