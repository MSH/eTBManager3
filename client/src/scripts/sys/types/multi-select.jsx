
import React from 'react';
import { SelectionBox } from '../../components/index';
import Form from '../../forms/form';
import FormUtils from '../../forms/form-utils';
import { isString } from '../../commons/utils';


/**
 * Field component to handle an array as value to select multiple options
 */
class MultiSelect extends React.Component {

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
	}

	componentWillMount() {
		this.setState({ resources: this.props.resources ? this.props.resources : null });
	}

	static getServerRequest(sc) {
		return isString(sc.options) ?
			{ cmd: sc.options } :
			null;
	}

	static getInitParams(sc) {
		return {
			options: sc.options
		};
	}

	/**
	 * Called when user selects an item in the drop down box
	 * @return {[type]} [description]
	 */
	onChange() {
		const vals = this.refs.selbox
			.getValue()
			.map(item => item.id);

		this.props.onChange({ schema: this.props.schema, value: vals });
	}

	/**
	 * Adjust the values to the corresponding options
	 * @return {[type]} [description]
	 */
	adjustValues() {
		const vals = this.props.value;
		const options = this.state.resources;
		if (!vals || !options) {
			return null;
		}

		return options.filter(item => vals.indexOf(item.id) >= 0);
	}

	render() {
		// get the schema passed by the parent
		const sc = this.props.schema || {};

		// get the label to be displayed
		const label = FormUtils.labelRender(sc.label, sc.required);

		// get the list of values adjusted to the options
		const values = this.adjustValues();

		return (
			<SelectionBox ref="selbox"
				value={values}
				errors={this.props.errors}
				label={label}
				optionDisplay="name"
				mode="multiple"
				options={this.state.resources}
				onChange={this.onChange} />
			);
	}
}

MultiSelect.propTypes = {
	value: React.PropTypes.array,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	schema: React.PropTypes.object,
	resources: React.PropTypes.array,
	noForm: React.PropTypes.bool
};

MultiSelect.options = {
	supportedTypes: 'multi-select'
};

export default Form.typeWrapper(MultiSelect);
