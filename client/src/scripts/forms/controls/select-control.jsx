
import React from 'react';
import ReactDOM from 'react-dom';
import FormUtils from '../form-utils';
import { SelectionBox } from '../../components/index';
import { isPromise } from '../../commons/utils';

/**
 * Used in the Form library. Provide input data of string and number types
 */
export default class SelectControl extends React.Component {

	static typeName() {
		return 'select';
	}

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
		this.focus = this.focus.bind(this);

		this.state = {};
	}

	componentWillMount() {
		if (!this.props.resources) {
			const options = FormUtils.createOptions(this.props.schema.options);
			if (options && !isPromise(options)) {
				this.setState({ options: options });
			}
		}
	}

	/**
	 * Prepare request data to be sent to the server, if necessary
	 * @param  {Object} snapshot The current snapshot
	 * @param  {Object} prev     The previous snapshot, if available
	 * @return {Object}          The request, or null if no request must be sent
	 */
	serverRequest(nextSchema, nextValue, nextResources) {
		return FormUtils.optionsRequest(this.props, nextSchema, nextValue, nextResources);
	}


	/**
	 * Set the component focus
	 * @return {[type]} [description]
	 */
	focus() {
		const comp = this.refs.sel;
		if (comp) {
			comp.getDOMNode().focus();
		}
	}

	/**
	 * Called when user changes the value in the control
	 * @return {[type]} [description]
	 */
	onChange(value) {
		const sc = this.props.schema;
		this.props.onChange({ schema: sc, value: value ? value.id : null });
	}

	/**
	 * Return the option by its ID
	 * @param  {[type]} id [description]
	 * @return {[type]}    [description]
	 */
	optionById(id) {
		const options = this.props.resources || this.state.options;
		return options ? options.find(item => item.id === id) : null;
	}

	render() {
		const sc = this.props.schema;

		if (sc.readOnly) {
			const val = this.optionById(this.props.value);
			return FormUtils.readOnlyRender(val ? val.name : null, sc.label);
		}

		const errors = this.props.errors;

		const wrapperClazz = sc.controlSize ? 'size-' + sc.controlSize : null;

		const options = this.props.resources || this.state.options;
		if (!options) {
			return null;
		}

		let value = this.props.value ? this.props.value.toString() : null;

		// get the value according to the option
		value = options.find(item => item.id.toString() === value);

		// rend the selection box
		return (
			<SelectionBox ref="sel"
				options={options}
				optionDisplay="name"
				label={FormUtils.labelRender(sc.label, sc.required)}
				onChange={this.onChange}
				value={value}
				help={errors}
				noSelectionLabel={sc.required ? null : '-'}
				wrapperClass={wrapperClazz}
				bsStyle={errors ? 'error' : null} />
		);
	}

}


SelectControl.propTypes = {
	value: React.PropTypes.any,
	schema: React.PropTypes.object,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	resources: React.PropTypes.any
};
