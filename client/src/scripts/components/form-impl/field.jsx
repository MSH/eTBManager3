
import React from 'react';
import { Input } from 'react-bootstrap';
import { getValue, setValue } from '../../commons/utils';
import { app } from '../../core/app';


export default class Field extends React.Component {

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
	}

	onChange() {
		const el = this.props.element;
		const model = this.props.doc;
		const comp = this.refs[el.property];

		const value = comp.props.type === 'checkbox' ? comp.getChecked() : comp.getValue();

		// change the value in the data model
		setValue(model, el.property, value);

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

		if (el.type === 'boolean') {
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
		if (!el.options) {
			return null;
		}

		const lst = typeof el.options === 'string' ? app.getState().app.lists[el.options] : el.options;

		if (!lst) {
			return null;
		}

		const opts = [];
		opts.push({ value: '-', text: '-' });
		Object.keys(lst).forEach(key => opts.push({ value: key, text: lst[key] }));

		return opts.map(opt => <option key={opt.value} value={opt.value}>{opt.text}</option>);
	}

	render() {
		const el = this.props.element;
		const data = this.props.doc;
		const errors = this.props.errors;

		if (!el.property || !data) {
			return null;
		}

		const value = getValue(data, el.property);

		const ctype = this.controlType(el);

		let label = el.label;
		let checked = null;
		if (ctype !== 'checkbox') {
			label += ':';
			if (el.required) {
				label = <span>{label}<i className="fa fa-exclamation-circle app-required"/></span>;
			}
		}
		else {
			checked = value;
		}

		const err = errors ? errors[el.property] : null;

		const options = this.createOptions(el);

		return	(
			<Input ref={el.property}
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

Field.propTypes = {
	element: React.PropTypes.object,
	doc: React.PropTypes.object,
	errors: React.PropTypes.object
};
