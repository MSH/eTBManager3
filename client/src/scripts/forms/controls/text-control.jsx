
import React from 'react';
import { FormGroup, FormControl, ControlLabel, HelpBlock } from 'react-bootstrap';
import FormUtils from '../form-utils';

export default class TextControl extends React.Component {

	static typeName() {
		return 'text';
	}

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
	}

	focus() {
		this.refs.input.focus();
		return true;
	}

	onChange(evt) {
//		const value = this.refs.input.getValue();
		const value = evt.target.value;

		this.props.onChange({ schema: this.props.schema, value: value });
	}

	render() {
		const sc = this.props.schema;

		const label = FormUtils.labelRender(sc.label, sc.required);

		const errors = this.props.errors;

		return (
			<FormGroup validationState={errors ? 'error' : null}>
				{
					label &&
					<ControlLabel>{label}</ControlLabel>
				}
				<FormControl ref="input"
					componentClass="textarea"
					value={this.props.value}
					onChange={this.onChange}
					/>
				{
					errors && <HelpBlock>{errors}</HelpBlock>
				}
			</FormGroup>
			);
	}
}


TextControl.propTypes = {
	value: React.PropTypes.string,
	schema: React.PropTypes.object,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any
};
