
import React from 'react';
import { Input } from 'react-bootstrap';
import Types from '../../forms/types';
import Form from '../../forms/form';
import Fa from '../../components/fa';

/**
 * Type handle for administrative unit series
 */
export default class YesNoType extends Types.Class.BoolType {

	displayText(value) {
		return value ? __('global.yes') : __('global.no');
	}

	render(value) {
		return value ? <Fa icon="check" className="text-primary" /> : '-';
	}

	formComponent() {
		return YesNoControl;
	}
}


/**
 * Default control to be used in forms
 */
class YesNoControl extends React.Component {

	readOnlyRender(schema) {
		return <Input disabled value={this.props.value} label={schema.label} type="text" />;
	}

	onChange() {
		if (this.props.onChange) {
			const s = this.refs.input.getValue();
			const value = s === '-' ? null : Boolean(s);
			this.props.onChange({ element: this.props.schema, value: value });
		}
	}

	editRender(schema) {
		const err = schema.errors;

		return (
			<Input label={Form.labelRender(schema.label, schema.required)} help={err} ref="input"
				onChange={this.onChange} wrapperClassName="size-3"
				style={err ? 'error' : null} type="select" value={this.props.value}>
				<option value="-">{'-'}</option>
				<option value="true">{__('global.yes')}</option>
				<option value="false">{__('global.no')}</option>
			</Input>
			);
	}

	render() {
		const schema = this.props.schema || {};

		return schema.readOnly ? this.readOnlyRender(schema) : this.editRender(schema);
	}
}

YesNoControl.propTypes = {
	value: React.PropTypes.bool,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	schema: React.PropTypes.object
};
