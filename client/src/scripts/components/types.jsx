/**
 * Handle type validation and convertion in the app
 */

import { setValue } from '../commons/utils';
import msgs from '../commons/messages';

const emailPattern = /^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/;
const passwordPattern = /((?=.*\d)(?=.*[a-zA-Z]).{6,20})/;


/**
 * Base class for type declaration
 */
class TypeHandler {

	/**
	 * default name of the type handler
	 * @return {[type]} [description]
	 */
	static typeName() {
		let name = this.name;
		if (name.endsWith('Type')) {
			name = name.slice(0, -4);
		}
		name = name[0].toLowerCase() + name.slice(1);
		return name;
	}

	/**
	 * Convert a value to a string representation
	 * @param  {[type]} value [description]
	 * @return {[type]}       [description]
	 */
	toString(value) {
		return value ? value.toString() : 'null';
	}

	/**
	 * Convert a value from a string representation
	 * @param  {[type]} value [description]
	 * @return {[type]}       [description]
	 */
	fromString(value) {
		return value;
	}

	/**
	 * Validate the value
	 * @param  {[type]} schema [description]
	 * @param  {[type]} value  [description]
	 * @return {[type]}        [description]
	 */
	validate() {
		return null;
	}

	displayText(value) {
		return value;
	}

	render(value) {
		return this.displayText(value);
	}

	toServer(value) {
		return value;
	}
}


/**
 * Register a new type handler by its class name
 * @param  {[type]} Type [description]
 * @return {[type]}      [description]
 */
function registerType(Type) {
	if (Type.constructor === Array) {
		Type.forEach(item => registerType(item));
		return;
	}

	const name = Type.typeName();
	const instance = new Type();
	if (name.constructor === Array) {
		name.forEach(k => _types[k] = instance);
	}
	else {
		_types[name] = instance;
	}
}

/**
 * All registered types
 * @type {Object}
 */
var _types = {
	$: {
		register: registerType,
		Handler: TypeHandler
	}
};


/**
 * Values to export
 */
export default _types;


/**
 * Common types
 */

/**
 * String type handler
 */
class StringType extends TypeHandler {

	validate(elem, value, dm) {
		if (elem.min && value.length < elem.min) {
			return msgs.minValue(elem.min);
		}

		if (elem.max && value.length > elem.max) {
			return msgs.maxValue(elem.max);
		}

		if (elem.trim) {
			setValue(dm, elem.property, value.trim());
		}
	}
}

class EmailType extends TypeHandler {

	validate(elem, value) {
		if (!emailPattern.test(value)) {
			return msgs.NotValidEmail;
		}
	}

}

class PasswordType extends TypeHandler {

	validate(elem, value) {
		if (!passwordPattern.test(value)) {
			return msgs.NotValidPassword;
		}
	}
}


class NumberType extends TypeHandler {

	static typeName() {
		return ['number', 'int', 'float'];
	}

	validate(elem, value, dm) {
		if (isNaN(value)) {
			return msgs.NotValid;
		}

		if (typeof value !== 'number') {
			const num = Number(value);
			setValue(dm, elem.property, num);
		}
	}
}


class BoolType extends TypeHandler {

	static typeName() {
		return 'bool';
	}

	validate(schema, value) {
		if (typeof value !== 'boolean') {
			return msgs.NotValid;
		}
	}
}

class DateType extends TypeHandler {

	static typeName() {
		return ['date', 'datetime', 'time'];
	}

	validate(schema, value) {
		if (!value instanceof Date) {
			return msgs.NotValid;
		}
	}
}

class ItemType extends TypeHandler {

	static typeName() {
		return 'item';
	}

	validate(schema, value) {
		if (typeof value !== 'object' || !value.id || !value.name) {
			return msgs.NotValid;
		}
	}

	toServer(value) {
		return value.id;
	}
}

/**
 * Register the common types
 */
registerType([
	StringType,
	EmailType,
	PasswordType,
	NumberType,
	BoolType,
	DateType,
	ItemType
	]);
