
import React from 'react';
import { getValue, setValue } from '../../commons/utils';
import { app } from '../../core/app';
import msgs from './messages';
import Form from '../form';

/**
 * Represents a field element of the form, with a lot of utility functions
 */
export default class Element {
	constructor(data, doc) {
		this.data = data;
		this.doc = doc;
	}

	/**
	 * Get the value being handled by the control
	 * @return {[type]} [description]
	 */
	getValue() {
		return getValue(this.doc, this.data.property);
	}

	/**
	 * Set the value handled by the control
	 * @param {[type]} val [description]
	 */
	setValue(val) {
		setValue(this.doc, this.data.property, val);
	}

	/**
	 * Check if the property element or the data are valid
	 * @return {Boolean} true if the property and value are defined, otherwise false
	 */
	isValid() {
		return this.data.property && this.doc;
	}

	/**
	 * Check if the element is read-only
	 * @return {Boolean} [description]
	 */
	isReadOnly() {
		return evalBool(this.data.readOnly, this.doc);
	}

	/**
	 * Check if the element is visible
	 * @return {Boolean} [description]
	 */
	isVisible() {
		return evalBool(this.data.visible, this.doc);
	}

	isRequired() {
		return !this.isReadOnly() && evalBool(this.data.required, this.doc);
	}

	label() {
		const label = this.data.label + ':';

		return this.isRequired() ?
			<span>{label}<i className="fa fa-exclamation-circle app-required"/></span> :
			label;
	}

	/**
	 * Search for an option by its id
	 * @param  {[type]} id [description]
	 * @return {[type]}    [description]
	 */
	optionById(id) {
		const opts = this.options();
		if (!opts) {
			return null;
		}

		return opts.find(opt => opt.id === id);
	}

	/**
	 * Return the list of options
	 * @return {[type]} [description]
	 */
	options() {
		if (this._options) {
			return this._options;
		}

		const options = this.data.options;

		if (!options) {
			return null;
		}

		const lst = typeof options === 'string' ? app.getState().app.lists[options] : options;

		if (!lst) {
			return null;
		}

		let opts = [];
		opts.push({ id: '-', name: '-' });
		if (lst.constructor !== Array && typeof lst === 'object') {
			Object.keys(lst).forEach(key => opts.push({ id: key, name: lst[key] }));
		}
		else {
			opts = opts.concat(lst);
		}

		this._options = opts;
		return opts;
	}

	isEmpty() {
		const val = this.getValue();
		return val === undefined || val === null || val === '';
	}

	typeHandler() {
		if (this._typeHandler) {
			return this._typeHandler;
		}

		this._typeHandler = Form.getType(this.data.type);
		return this._typeHandler;
	}

	/**
	 * Validate the value
	 * @return {[type]} [description]
	 */
	validate() {
		if (this.isReadOnly()) {
			return null;
		}

		// check required field
		if (this.isEmpty()) {
			if (this.isRequired()) {
				return msgs.NotNull;
			}
			return null;
		}

		const th = this.typeHandler();

		const elem = this.data;

		if (!th) {
			return 'No validator found: ' + elem.property + ': ' + elem.type;
		}

		return th.validate ? th.validate(elem, this.getValue(), this.doc) : null;
	}
}

/**
 * Evaluate a boolean value. If it is a function, the function is evaludated
 * and the value returned
 * @param  {[type]} val     [description]
 * @param  {[type]} context [description]
 * @return {[type]}         [description]
 */
function evalBool(val, context) {
	if (typeof val === 'function') {
		return context.val() === true;
	}
	return val === true;
}
