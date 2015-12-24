
import React from 'react';
import { Input } from 'react-bootstrap';


export default class FieldControl extends React.Component {

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
	}

	static controlName() {
		return 'field';
	}

	/**
	 * Called when user changes the value in the control
	 * @return {[type]} [description]
	 */
	onChange() {
		const el = this.props.element;
		const comp = this.refs[el.data.property];

		const value = comp.props.type === 'checkbox' ? comp.getChecked() : comp.getValue();

		// change the value in the data model
		el.setValue(value);

		// force component refreshing
		this.forceUpdate();
	}

	/**
	 * return the control type according to the element description
	 * @param  {[type]} el [description]
	 * @return {[type]}    [description]
	 */
	controlType(el) {
		const ctype = el.control;

		if (ctype) {
			return ctype;
		}

		if (el.type === 'bool') {
			return 'checkbox';
		}

		return el.options ? 'select' : 'text';
	}

	/**
	 * Create the list of options based on element structure
	 * @param  {[type]} el [description]
	 * @return {[type]}    [description]
	 */
	createOptions(el) {
		const opts = el.options();

		return opts ?
			opts.map(opt => <option key={opt.id} value={opt.id}>{opt.name}</option>) :
			null;
	}

	render() {
		const el = this.props.element;
		const errors = this.props.errors;

		if (!el.isValid()) {
			return null;
		}

		const value = el.getValue();

		const ctype = this.controlType(el.data);

		let checked = null;
		let label;
		if (ctype !== 'checkbox') {
			label = el.label();
		}
		else {
			checked = value;
			label = el.data.label;
		}

		const err = errors ? errors[el.data.property] : null;

		const options = this.createOptions(el);

		return	(
			<Input ref={el.data.property}
				label={label}
				type={ctype}
				onChange={this.onChange}
				value={value}
				help={err}
				checked={checked}
				bsStyle={err ? 'error' : null} >
				{options}
			</Input>
			);
	}
}

FieldControl.propTypes = {
	element: React.PropTypes.object,
	doc: React.PropTypes.object,
	errors: React.PropTypes.object
};
