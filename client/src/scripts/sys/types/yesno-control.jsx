
import React from 'react';
import { Input } from 'react-bootstrap';
import Form from '../../forms/form';
import FormUtils from '../../forms/form-utils';
import { Fa, SelectionBox } from '../../components/index';


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
			const val = this.refs.input.getValue();
			this.props.onChange({ schema: this.props.schema, value: val });
		}
	}


	optionRender(val) {
		return val ?
			<div><Fa icon="check-circle" className="text-primary"/>{__('global.yes')}</div> :
			<div><Fa icon="times-circle" className="text-danger"/>{__('global.no')}</div>;
	}


	editRender(schema) {
		const err = this.props.errors;
		const options = [true, false];

		return (
			<SelectionBox label={FormUtils.labelRender(schema.label, schema.required)}
				help={err} ref="input"
				onChange={this.onChange}
				options={options}
				optionDisplay={this.optionRender}
				wrapperClassName="size-3"
				bsStyle={err ? 'error' : null} value={this.props.value} />
			);
	}

	render() {
		// if there is no form, just display an icon
		if (this.props.noForm) {
			return this.props.value ? <Fa icon="check" className="text-primary" /> : <Fa icon="times-circle" className="text-danger" />;
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
