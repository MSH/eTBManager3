
import React from 'react';
import DatePicker from '../../components/date-picker';
import fieldControlWrapper from './field-control';
import FormUtils from '../form-utils';

/**
 * Control to support date types
 */
class DateControl extends React.Component {

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
	}

	onChange(evt, value) {
		this.props.onChange({ schema: this.props.schema, value: value });
	}

	render() {
		const sc = this.props.schema;

		const label = FormUtils.labelRender(sc.label, sc.required);

		console.log(sc);

		return	(
			<DatePicker ref="input"
				label={label}
				onChange={this.onChange}
				help={sc.errors}
				value={this.props.value}
				bsStyle={sc.errors ? 'error' : null} />
			);
	}
}


DateControl.options = {
	supportedTypes: 'date'
};


DateControl.propTypes = {
	value: React.PropTypes.instanceOf(Date),
	schema: React.PropTypes.object,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	resources: React.PropTypes.any
};

export default fieldControlWrapper(DateControl);
