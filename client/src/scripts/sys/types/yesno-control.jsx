
import React from 'react';
import { Input } from 'react-bootstrap';
import Form from '../../forms/form';
import FormUtils from '../../forms/form-utils';
import Fa from '../../components/fa';


/**
 * Control for yes-no selection
 */
class YesNoControl extends React.Component {

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
	}

	static displayText(value) {
		return value ? __('global.yes') : __('global.no');
	}

	readOnlyRender(schema) {
		return <Input disabled value={this.props.value} label={schema.label} type="text" />;
	}

	onChange() {
		if (this.props.onChange) {
			const s = this.refs.input.getValue();
			const value = s === '-' ? null : s === 'true';
			this.props.onChange({ schema: this.props.schema, value: value });
		}
	}

	editRender(schema) {
		const err = schema.errors;

		return (
			<Input label={FormUtils.labelRender(schema.label, schema.required)} help={err} ref="input"
				onChange={this.onChange} wrapperClassName="size-3"
				style={err ? 'error' : null} type="select" value={this.props.value}>
				<option value="-">{'-'}</option>
				<option value="true">{__('global.yes')}</option>
				<option value="false">{__('global.no')}</option>
			</Input>
			);
	}

	render() {
		// if there is no form, just display an icon
		if (this.props.noForm) {
			return this.props.value ? <Fa icon="check" className="text-primary" /> : <span>{'-'}</span>;
		}

		const schema = this.props.schema || {};

		return schema.readOnly ? this.readOnlyRender(schema) : this.editRender(schema);
	}
}

YesNoControl.propTypes = {
	value: React.PropTypes.bool,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	schema: React.PropTypes.object,
	noForm: React.PropTypes.bool
};

YesNoControl.options = {
	supportedTypes: 'yesNo'
};

export default Form.typeWrapper(YesNoControl);
