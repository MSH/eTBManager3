
import React from 'react';
import { Input } from 'react-bootstrap';
import Form from '../form';


export default class InputControl extends React.Component {

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
	}

	componentDidMount() {
		const sc = this.props.schema;
		if (sc.autoFocus) {
			// set the focus
			// it seems that, because form is displayed through an animation, the focus must
			// be set after a while
			setTimeout(() => this.refs[sc.property].getInputDOMNode().focus(), 1);
		}
	}

	/**
	 * Called when user changes the value in the control
	 * @return {[type]} [description]
	 */
	onChange(evt) {
		const el = this.props.schema;
		const comp = this.refs[el.property];

		const value = comp.props.type === 'checkbox' ? comp.getChecked() : comp.getValue();

		this.props.onChange({ event: evt, element: el, value: value });
	}

	/**
	 * return the control type according to the element description
	 * @param  {[type]} el [description]
	 * @return {[type]}    [description]
	 */
	controlType(sc) {
		if (sc.options) {
			return 'select';
		}

		const ctype = sc.type;

		if (ctype === 'bool') {
			return 'checkbox';
		}

		return 'text';
	}

	/**
	 * Create the list of options based on element structure
	 * @param  {[type]} el [description]
	 * @return {[type]}    [description]
	 */
	createOptions(el) {
		const opts = el.options;

		return opts ?
			opts.map(opt => <option key={opt.id} value={opt.id}>{opt.name}</option>) :
			null;
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

		const ctype = this.controlType(sc);

		let checked = null;
		let label;
		if (ctype === 'checkbox') {
			checked = this.props.value;
			label = sc.label;
		}
		else {
			label = Form.labelRender(sc.label, sc.required);
		}

		const options = this.createOptions(sc);

		const wrapperClazz = this.getWrapperClass(sc);

		return	(
			<Input ref={sc.property}
				label={label}
				type={ctype}
				onChange={this.onChange}
				value={this.props.value}
				help={errors}
				checked={checked}
				wrapperClassName={wrapperClazz}
				bsStyle={errors ? 'error' : null} >
				{options}
			</Input>
			);
	}
}

InputControl.propTypes = {
	schema: React.PropTypes.object,
	value: React.PropTypes.any,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any
};
