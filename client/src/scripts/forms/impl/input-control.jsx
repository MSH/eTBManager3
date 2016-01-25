
import React from 'react';
import fieldControlWrapper from './field-control';
import { Input } from 'react-bootstrap';
import FormUtils from '../form-utils';
import { stringValidator, numberValidator } from './validators';


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
		else if (sc.type === 'number') {
			// if is a number, convert to number
			if (!isNaN(value)) {
				value = Number(value);
			}
		}

		this.props.onChange({ schema: sc, value: value });
	}


	getWrapperClass(sc) {
		if (!sc.controlSize) {
			return null;
		}

		switch (sc.controlSize) {
			case 2: return 'size-2';
			case 3: return 'size-3';
			case 4: return 'size-4';
			default:
		}

		if (__DEV__) {
			throw new Error('Invalid control size = ' + sc.controlSize);
		}

		return null;
	}


	render() {
		const sc = this.props.schema;
		const errors = this.props.errors;

		const options = FormUtils.createOptions(sc.options);

		let ctype;
		if (options) {
			ctype = 'select';
		}
		else {
			ctype = sc.password ? 'password' : 'text';
		}

		const label = FormUtils.labelRender(sc.label, sc.required);

		const wrapperClazz = this.getWrapperClass(sc);

		return	(
			<Input ref="input"
				label={label}
				type={ctype}
				onChange={this.onChange}
				value={this.props.value}
				help={errors}
				wrapperClassName={wrapperClazz}
				bsStyle={errors ? 'error' : null} >
				{options}
			</Input>
			);
	}

}

InputControl.options = {
	supportedTypes: ['string', 'number']
};


InputControl.propTypes = {
	value: React.PropTypes.any,
	schema: React.PropTypes.object,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	resources: React.PropTypes.any
};

export default fieldControlWrapper(InputControl);
