/**
 * Generate and maintain a form based on a given layout (in object structure) and a data model
 */
import React from 'react';
import { Grid } from 'react-bootstrap';
import validateForm from './impl/form-validate';
import { setValue } from '../commons/utils';
import formRender from './impl/form-render';
import createSnapshot from './impl/form-snapshot';
import formControl from './controls/form-control';
import { initDefaultValues } from './impl/form-init';
import { requestServer } from './impl/form-request';
import FormUtils from './form-utils';


/**
 * Create a form based on a given json layout and a data model
 */
export default class Form extends React.Component {

	/**
	 * Register a new type to be supported by the forms
	 * @param  {[type]} Comp [description]
	 */
	static registerType(Comp) {
		if (Comp.constructor.name === 'Array') {
			Comp.forEach(item => Form.registerType(item));
			return;
		}

		const name = Comp.typeName();

		if (__DEV__) {
			if (!name) {
				console.log('No name defined for ' + (Comp.typeName ? Comp.typeName() : Comp) + '.typeName()');
			}
		}

		if (name.constructor.name === 'Array') {
			name.forEach(k => { Form.types[k] = Comp; });
		}

		Form.types[name] = Comp;
	}

	constructor(props) {
		super(props);
		this._onChange = this._onChange.bind(this);
		this.state = {};

		// this code block is just available in development mode
		if (__DEV__) {
			if (!this.props.doc) {
				throw new Error('Form document is required (property doc)');
			}

			const schema = this.props.schema;
			if (!schema) {
				throw new Error('Form schema is required (property schema)');
			}

			// validate the schema
			if (schema.layout) {
				schema.layout
				.forEach(elem => {
					if (!elem.type) {
						throw new Error('Element type not defined for ' + elem.property);
					}

					const comp = FormUtils.getComponent(elem);
					if (!comp) {
						throw new Error('Component type not found: ' + elem.type);
					}
				});
			}
		}
	}

	componentWillMount() {
		initDefaultValues(this);

		const snapshots = this.updateSnapshot();

		// check if there is any element to set focus on
		if (!snapshots.find(item => item.autoFocus)) {
			// find first field element to set autofocus
			const el = snapshots.find(item => !!item.property);
			if (el) {
				el.autoFocus = true;
			}
		}


		const self = this;

		// request the server
		requestServer(snapshots)
		.then(res => self.setState({ resources: res }));
	}

	/**
	 * Validate the form and return validation messages, if any erro is found
	 * @return {[type]}           [description]
	 */
	validate() {
		return validateForm(this);
	}


	/**
	 * Update element state list. Element state is just update if it has changed
	 * @return {Array} List of element states
	 */
	updateSnapshot() {
		const lst = createSnapshot(this);
		this.setState({ snapshots: lst });
		return lst;
	}


	/**
	 * Called every time a component value is changed
	 */
	_onChange(evt) {
		// update value in the data model
		const schema = evt.schema;
		const value = evt.value;
		setValue(this.props.doc, schema.property, value, true);

		// check if schema is mapping changes
		if (schema.onChange) {
			schema.onChange.call(this.props.doc, this.props.doc);
		}

		// notify parent about changes
		if (this.props.onChange) {
			this.props.onChange(this.props.doc, schema, value);
		}

		const snapshots = this.updateSnapshot();

		if (schema.refreshOnChange) {
			this._refreshElems(schema.refreshOnChange, snapshots);
		}

		const self = this;
		// request the server
		requestServer(snapshots)
		.then(res => self.setState({ resources: res }));
	}


	/**
	 * Rend form
	 * @return {[type]} [description]
	 */
	render() {
		const form = formRender(this);
		return <Grid fluid className={this.props.className}>{form}</Grid>;
	}
}

Form.propTypes = {
	schema: React.PropTypes.object,
	doc: React.PropTypes.object,
	errors: React.PropTypes.object,
	resources: React.PropTypes.object,
	onChange: React.PropTypes.func,
	readOnly: React.PropTypes.bool,
	className: React.PropTypes.string
};


/**
 * List of supported types
 * @type {Object}
 */
Form.types = {};

Form.control = formControl;

/**
 * Automatically register common controls
 */

import InputControl from './controls/input-control';
import BoolControl from './controls/bool-control';
import TextControl from './controls/text-control';
import SelectControl from './controls/select-control';
import DateControl from './controls/date-control';
import SubtitleControl from './controls/subtitle-control';
import GroupControl from './controls/group-control';

Form.registerType([
	InputControl,
	BoolControl,
	TextControl,
	SelectControl,
	DateControl,
	SubtitleControl,
	GroupControl
]);
