
import React from 'react';
import { FormGroup, FormControl, ControlLabel, HelpBlock } from 'react-bootstrap';
import FormUtils from '../form-utils';
import { textToComp } from '../../commons/utils';

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
		const value = evt.target.value;

		this.props.onChange({ schema: this.props.schema, value: value });
	}

	/**
	 * Render a read-only text
	 * @param  {[type]} sc [description]
	 * @return {[type]}    [description]
	 */
	readOnlyRender(sc) {
		const value = this.props.value;
		const txt = value ? textToComp(value) : '';

		return FormUtils.readOnlyRender(txt, sc.label);
	}

	render() {
		const sc = this.props.schema;
		if (sc.readOnly) {
			return this.readOnlyRender(sc);
		}

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
