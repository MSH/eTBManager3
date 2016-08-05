
import React from 'react';
import DatePicker from '../../components/date-picker';
import FormUtils from '../form-utils';
import { isString } from '../../commons/utils';
import moment from 'moment';


/**
 * Control to support date types
 */
export default class DateControl extends React.Component {

	static typeName() {
		return 'date';
	}

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
		this.focus = this.focus.bind(this);
	}

	onChange(evt, value) {
		this.props.onChange({ schema: this.props.schema, value: value });
	}

	/**
	 * Set the component focus
	 * @return {[type]} [description]
	 */
	focus() {
		this.refs.input.focus();
		return true;
	}

	displayText(val) {
		const dt = isString(val) ? Date.parse(val) : val;
		return moment(dt).format('LL');
	}

	render() {
		const sc = this.props.schema;

		if (sc.readOnly) {
			return FormUtils.readOnlyRender(this.displayText(this.props.value), sc.label);
		}

		const label = FormUtils.labelRender(sc.label, sc.required);

		let value = this.props.value;
		if (isString(value)) {
			value = moment(this.props.value)._d;
		}

		return	(
			<DatePicker ref="input"
				label={label}
				onChange={this.onChange}
				help={this.props.errors}
				value={value}
				bsStyle={this.props.errors ? 'error' : null} />
			);
	}
}


DateControl.propTypes = {
	value: React.PropTypes.any,
	schema: React.PropTypes.object,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	resources: React.PropTypes.any
};
