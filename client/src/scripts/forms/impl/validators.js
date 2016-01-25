
import msgs from '../../commons/messages';

const emailPattern = /^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/;
const passwordPattern = /((?=.*\d)(?=.*[a-zA-Z]).{6,20})/;


/**
 * Standard validator for string types
 * @param  {[type]} schema [description]
 * @param  {[type]} value  [description]
 * @return {[type]}        [description]
 */
export function stringValidator(schema, value) {
	if (schema.min && value.length < schema.min) {
		return msgs.minValue(schema.min);
	}

	if (schema.max && value.length > schema.max) {
		return msgs.maxValue(schema.max);
	}

	if (schema.email && !emailPattern.test(value)) {
		return msgs.NotValidEmail;
	}

	if (schema.password && !passwordPattern.test(value)) {
		return msgs.NotValidPassword;
	}

	return null;
}

/**
 * Standard validator for number types
 * @param  {[type]} schema [description]
 * @param  {[type]} value  [description]
 * @return {[type]}        [description]
 */
export function numberValidator(schema, value) {
	if (isNaN(value)) {
		return msgs.NotValid;
	}

	if (schema.min && value.length < schema.min) {
		return msgs.minValue(schema.min);
	}

	if (schema.max && value > schema.max) {
		return msgs.maxValue(schema.max);
	}
	return null;
}
