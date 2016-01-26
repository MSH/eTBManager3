/**
 * Generate and maintain a form based on a given layout (in object structure) and a data model
 */
import React from 'react';
import validateForm from './impl/form-validate';
import { setValue, objEqual } from '../commons/utils';
import createForm from './impl/form-create';
import createSnapshot from './impl/form-snapshot';
import fieldControlWrapper from './impl/field-control';
import initForm from './impl/form-init';


/**
 * Create a form based on a given json layout and a data model
 */
export default class Form extends React.Component {

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
				.filter(elem => elem.el === 'field' || !elem.el)
				.forEach(elem => {
					if (!elem.property) {
						throw new Error('Property must be informed in field schema');
					}
					if (!elem.type) {
						throw new Error('Element type not defined for ' + elem.property);
					}

					const comp = Form.types[elem.type];
					if (!comp) {
						throw new Error('Component type not found: ' + elem.type);
					}
				});
			}
		}
	}

	componentWillMount() {
		const snapshot = this.updateSnapshot();

		// check if there is any element to set focus on
		if (!snapshot.find(item => item.autoFocus)) {
			// find first field element to set autofocus
			const el = snapshot.find(item => item.el === 'field');
			if (el) {
				el.autoFocus = true;
			}
		}


		const self = this;

		initForm(this, snapshot)
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
		const lst = createSnapshot(this.props.schema, this.props.doc);

		const oldlst = this.state ? this.state.snapshot : null;

		let updated = oldlst === null;

		// there is a previous snapshot ?
		if (oldlst) {
			// compare the items of previous snapshot with the new one
			for (var i = 0; i < lst.length; i++) {
				const old = oldlst[i];
				const cur = lst[i];
				if (objEqual(old, cur)) {
					lst[i] = old;
					updated = true;
				}
			}
		}

		// check if snapshot should be updated
		if (updated || !this.state.snapshot) {
			this.setState({ snapshot: lst });
		}

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

		this.updateSnapshot();
		// force update, in case snapshot remains the same
		this.forceUpdate();
	}

	/**
	 * Called when form initialization is done and field resources are available
	 * @param  {object} resources List of field resources
	 * @return {[type]}           [description]
	 */
	notifyReady(resources) {
		this.setState({ resources: resources });
		if (this.props.onReady) {
			this.props.onReady();
		}
	}

	/**
	 * Rend form
	 * @return {[type]} [description]
	 */
	render() {
		const form = createForm(this);
		return <div>{form}</div>;
	}
}

Form.propTypes = {
	schema: React.PropTypes.object,
	doc: React.PropTypes.object,
	errors: React.PropTypes.object,
	resources: React.PropTypes.object,
	onReady: React.PropTypes.func,
	crud: React.PropTypes.object
};

/**
 * Register a new type to be supported by the forms
 * @param  {[type]} Comp [description]
 * @return {[type]}      [description]
 */
Form.registerType = function(Comp) {
	if (Comp.constructor.name === 'Array') {
		return Comp.forEach(item => Form.registerType(item));
	}

	const name = Comp.supportedTypes();

	if (name.constructor.name === 'Array') {
		name.forEach(k => Form.types[k] = Comp);
	}

	Form.types[name] = Comp;
};

/**
 * List of supported types
 * @type {Object}
 */
Form.types = {};

Form.typeWrapper = fieldControlWrapper;

/**
 * Automatically register common controls
 */

import InputControl from './impl/input-control';
import BoolControl from './impl/bool-control';

Form.registerType(InputControl);
Form.registerType(BoolControl);
