
/**
 * Common types
 */

import { setValue } from '../../commons/utils';
import msgs from '../../commons/messages';
import TypeHandler from './type-handler';
import InputControl from './input-control';

const emailPattern = /^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/;
const passwordPattern = /((?=.*\d)(?=.*[a-zA-Z]).{6,20})/;


class BaseInputType extends TypeHandler {
	formComponent() {
		return InputControl;
	}
}

/**
 * String type handler
 */
class StringType extends BaseInputType {

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

class EmailType extends BaseInputType {

	validate(elem, value) {
		if (!emailPattern.test(value)) {
			return msgs.NotValidEmail;
		}
	}

}

class PasswordType extends BaseInputType {

	validate(elem, value) {
		if (!passwordPattern.test(value)) {
			return msgs.NotValidPassword;
		}
	}
}


class NumberType extends BaseInputType {

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


class BoolType extends BaseInputType {

	static typeName() {
		return 'bool';
	}

	validate(schema, value) {
		if (typeof value !== 'boolean') {
			return msgs.NotValid;
		}
	}
}


class ItemType extends BaseInputType {

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

export default function(registerType) {
	registerType([
		StringType,
		EmailType,
		PasswordType,
		NumberType,
		BoolType,
		ItemType
		]);
}
