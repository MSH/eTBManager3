/**
 * Validate the content of the form based on the fields definition
 */

import { getValue, setValue, format } from '../../commons/utils';

const emailPattern = /^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/;
const passwordPattern = /((?=.*\d)(?=.*[a-zA-Z]).{6,20})/;

const msgs = {
	NotValid: __('NotValid'),
	NotNull: __('NotNull')
};

/**
 * Validate a data model against its validation rules in the form layout
 * @param  {array} layout    List of elements that contains the rules to be validated
 * @param  {object} dm       The data model to be validated
 * @return {array}           List of errors found
 */
export function validateForm(layout, dm) {
	let errors = null;

	layout.forEach(elem => {
		if (elem.el === 'field' || !elem.el) {
			const msg = validateField(elem, dm, errors);
			if (msg) {
				if (!errors) {
					errors = {};
				}
				errors[elem.property] = msg;
			}
		}
	});

	return errors;
}

function validateField(elem, dm) {
	const val = getValue(dm, elem.property);

	// check required field
	if (isEmpty(val)) {
		if (elem.required && isEmpty(val)) {
			return msgs.NotNull;
		}
	}

	const validator = validators[elem.type];
	if (!validator) {
		return 'No validator found: ' + elem.property + ': ' + elem.type;
	}

	return validator(elem, val, dm);
}

function isEmpty(val) {
	return val === undefined || val === null || val === '';
}

/**
 * List of custom validators
 * @type {Object}
 */
const validators = {
	/**
	 * String validator
	 */
	string: function(elem, value, dm) {
		if (elem.min && value.length < elem.min) {
			return format(__('validation.client.min'), elem.min);
		}

		if (elem.max && value.length > elem.max) {
			return format(__('validation.client.max'), elem.max);
		}

		if (elem.trim) {
			setValue(dm, elem.property, value.trim());
		}
	},

	/**
	 * email validator
	 */
	email: function(elem, value) {
		if (!emailPattern.test(value)) {
			return __('NotValidEmail');
		}
	},

	/**
	 * Password validator
	 */
	password: function(elem, value) {
		if (!passwordPattern.test(value)) {
			return __('NotValidPassword');
		}
	},

	/**
	 * Number validator
	 */
	number: function(elem, value, dm) {
		if (isNaN(value)) {
			return msgs.NotValid;
		}

		if (typeof value !== 'number') {
			const num = Number(value);
			setValue(dm, elem.property, num);
		}
	},

	boolean: function(elem, value) {
		if (typeof value !== 'boolean') {
			return msgs.NotValid;
		}
	},

	date: function(elem, value) {
		if (!value instanceof Date) {
			return msgs.NotValid;
		}
	}
};
