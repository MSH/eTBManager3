
import React from 'react';
import { Input } from 'react-bootstrap';
import { getValue, setValue } from '../../commons/utils';

export default class Field extends React.Component {

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
	}

	onChange() {
		const el = this.props.element;
		const model = this.props.doc;
		const value = this.refs[el.property].getValue();

		// change the value in the data model
		setValue(model, el.property, value);

		// force component refreshing
		this.forceUpdate();
	}

	render() {
		const el = this.props.element;
		const data = this.props.doc;
		const errors = this.props.errors;

		if (!el.property || !data) {
			return null;
		}

		const value = getValue(data, el.property);

		let label = el.label + ':';
		if (el.required) {
			label = <span>{el.label + ':'}<i className="fa fa-exclamation-circle app-required"/></span>;
		}

		const err = errors ? errors[el.property] : null;

		return	(
			<Input ref={el.property}
				label={label}
				type="text"
				onChange={this.onChange}
				value={value}
				help={err}
				bsStyle={err ? 'error' : null} />
			);
	}
}

Field.propTypes = {
	element: React.PropTypes.object,
	doc: React.PropTypes.object,
	errors: React.PropTypes.object
};
