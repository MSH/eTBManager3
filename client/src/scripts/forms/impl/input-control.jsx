
import React from 'react';
import ReactDOM from 'react-dom';
import { Input } from 'react-bootstrap';


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
		// // change the value in the data model
		// el.setValue(value);

		// // force component refreshing
		// this.forceUpdate();
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

	render() {
		const sc = this.props.schema;
		const errors = this.props.errors;

		const ctype = this.controlType(sc);

		let checked = null;
		const label = sc.label;
		if (ctype === 'checkbox') {
			checked = this.props.value;
		}

		const err = errors ? errors[sc.property] : null;

		const options = this.createOptions(sc);

		return	(
			<Input ref={sc.property}
				label={label}
				type={ctype}
				onChange={this.onChange}
				value={this.props.value}
				help={err}
				checked={checked}
				bsStyle={err ? 'error' : null} >
				{options}
			</Input>
			);
	}
}

InputControl.propTypes = {
	schema: React.PropTypes.object,
	value: React.PropTypes.any,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.object
};
