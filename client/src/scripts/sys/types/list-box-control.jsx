
import React from 'react';
import Form from '../../forms/form';
import FormUtils from '../../forms/form-utils';
import { ListBox } from '../../components/index';
import { isPromise } from '../../commons/utils';


/**
 * Control for yes-no selection
 */
export default class ListBoxControl extends React.Component {

	static typeName() {
		return 'listBox';
	}

	/**
	 * Return request to be sent to server, if necessary
	 * @param  {[type]} schema [description]
	 * @return {[type]}        [description]
	 */
	static serverRequest(schema, val, doc) {
		// TODOMS Ricardo: O que faz isso???
		return FormUtils.optionsRequest(schema, doc);
	}

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
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
	 * Called when user changes the value in the control
	 * @return {[type]} [description]
	 */
	onChange() {
		const sc = this.props.schema;
		const value = this.refs.sel.getValue();

		this.props.onChange({ schema: sc, value: value ? value.id : null });
	}

	render() {
		const sc = this.props.schema;

		if (sc.readOnly) {
			const val = this.props.value ? this.props.value.item : null;
			return FormUtils.readOnlyRender(val, sc.label);
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
			<ListBox ref="sel"
				options={options}
				optionDisplay="name"
				label={FormUtils.labelRender(sc.label, sc.required)}
				onChange={this.onChange}
				value={value}
				help={errors}
				wrapperClassName={wrapperClazz}
				bsStyle={errors ? 'error' : null}
				vertical={sc.vertical}
				textAlign={sc.textAlign} />
		);
	}
}

ListBoxControl.propTypes = {
	value: React.PropTypes.string,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	schema: React.PropTypes.object,
	resources: React.PropTypes.array,
	noForm: React.PropTypes.bool,
	vertical: React.PropTypes.bool,
	textAlign: React.PropTypes.oneOf(['right', 'left', 'center'])
};
