
import React from 'react';
import fieldControlWrapper from './field-control';
import { Input } from 'react-bootstrap';
import FormUtils from '../form-utils';
import { stringValidator, numberValidator } from './validators';

/**
 * Used in the Form library. Provide input data of string and number types
 */
class InputControl extends React.Component {


	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
		this.focus = this.focus.bind(this);
	}

	validate() {
		const schema = this.props.schema;
		const value = this.refs.input.getValue();

		return schema.type === 'string' ?
			stringValidator(schema, value) :
			numberValidator(schema, value);
	}


	/**
	 * Set the component focus
	 * @return {[type]} [description]
	 */
	focus() {
		this.refs.input.getInputDOMNode().focus();
	}

	/**
	 * Check if type being handled in a number
	 * @return {Boolean} True if type is supposed to be a number
	 */
	isNumericType() {
		return ['number', 'int', 'float'].indexOf(this.props.schema.type) >= 0;
	}

	/**
	 * Called when user changes the value in the control
	 * @return {[type]} [description]
	 */
	onChange() {
		const sc = this.props.schema;
		let value = this.refs.input.getValue();

		// if it is an empty string, so return null
		if (!value) {
			value = null;
		}
		else if (this.isNumericType()) {
			// if is a number, convert to number
			if (!isNaN(value)) {
				value = Number(value);
			}
		}

		this.props.onChange({ schema: sc, value: value });
	}


	render() {
		const sc = this.props.schema;
		const errors = this.props.errors;

		const wrapperClazz = sc.controlSize ? 'size-' + sc.controlSize : null;

		const ctype = sc.password ? 'password' : 'text';

		return	(
			<Input ref="input"
				label={FormUtils.labelRender(sc.label, sc.required)}
				type={ctype}
				onChange={this.onChange}
				value={this.props.value}
				help={errors}
				wrapperClassName={wrapperClazz}
				bsStyle={errors ? 'error' : null} />
			);
	}

}

InputControl.options = {
	supportedTypes: ['string', 'number', 'int', 'float']
};


InputControl.propTypes = {
	value: React.PropTypes.any,
	schema: React.PropTypes.object,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	resources: React.PropTypes.any
};

export default fieldControlWrapper(InputControl);
