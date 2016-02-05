
import React from 'react';
import fieldControlWrapper from './field-control';
import { Input } from 'react-bootstrap';
import FormUtils from '../form-utils';
import { stringValidator, numberValidator } from './validators';
import { SelectionBox } from '../../components/index';

/**
 * Used in the Form library. Provide input data of string and number types
 */
class InputControl extends React.Component {


	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
		this.focus = this.focus.bind(this);
	}

	static isServerInitRequired(schema) {
		return typeof schema.options === 'string';
	}

	static getInitParams(schema) {
		return {
			options: schema.options
		};
	}

	validate() {
		const schema = this.props.schema;
		const value = this.getValue();

		return schema.type === 'string' ?
			stringValidator(schema, value) :
			numberValidator(schema, value);
	}


	/**
	 * Get the selected value in the control
	 * @return {Any} The value in the control
	 */
	getValue() {
		if (this.refs.input) {
			return this.refs.input.getValue();
		}

		const val = this.refs.sel.getValue();

		return val ? val.id : null;
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
		let value = this.getValue();

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

		const label = FormUtils.labelRender(sc.label, sc.required);

		const wrapperClazz = this.getWrapperClass(sc);

		const options = FormUtils.createOptions(sc.options, this.props.resources);

		let value = this.props.value;

		// is selection box ?
		if (options) {
			// get the value according to the option
			value = options.find(item => item.id === value);

			// rend the selection box
			return (
				<SelectionBox ref="sel"
					options={options}
					optionDisplay="name"
					label={label}
					onChange={this.onChange}
					value={value}
					help={errors}
					noSelectionLabel={sc.required ? null : '-'}
					bsStyle={errors ? 'error' : null} />
			);
		}

		const ctype = sc.password ? 'password' : 'text';

		return	(
			<Input ref="input"
				label={label}
				type={ctype}
				onChange={this.onChange}
				value={value}
				help={errors}
				wrapperClassName={wrapperClazz}
				bsStyle={errors ? 'error' : null} >
				{options}
			</Input>
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
