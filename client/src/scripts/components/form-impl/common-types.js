
import { setValue, format } from '../../commons/utils';
import msgs from './messages';


const emailPattern = /^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/;
const passwordPattern = /((?=.*\d)(?=.*[a-zA-Z]).{6,20})/;


export default [
	/**
	 * String validator
	 */
	{
		type: 'string',
		validate: function(elem, value, dm) {
			if (elem.min && value.length < elem.min) {
				return format(__('validation.client.min'), elem.min);
			}

			if (elem.max && value.length > elem.max) {
				return format(__('validation.client.max'), elem.max);
			}

			if (elem.trim) {
				setValue(dm, elem.property, value.trim());
			}
		}
	},
	/**
	 * email validator
	 */
	{
		type: 'email',
		validate: function(elem, value) {
			if (!emailPattern.test(value)) {
				return __('NotValidEmail');
			}
		}
	},
	/**
	 * Password validator
	 */
	{
		type: 'password',
		validate: function(elem, value) {
			if (!passwordPattern.test(value)) {
				return __('NotValidPassword');
			}
		}
	},
	/**
	 * Number validator
	 */
	{
		type: 'number',
		validate: function(elem, value, dm) {
			if (isNaN(value)) {
				return msgs.NotValid;
			}

			if (typeof value !== 'number') {
				const num = Number(value);
				setValue(dm, elem.property, num);
			}
		}
	},
	/**
	 * Boolean validator
	 */
	{
		type: 'bool',
		validate: function(schema, value) {
			if (typeof value !== 'boolean') {
				return msgs.NotValid;
			}
		}
	},
	/**
	 * Date and time validator
	 */
	{
		type: ['date', 'datetime', 'timestamp'],
		validate: function(schema, value) {
			if (!value instanceof Date) {
				return msgs.NotValid;
			}
		}
	},

	/**
	 * Item, carrying an id (or value) and a name
	 */
	{
		type: 'item',
		validate: function(schema, value) {
			if (typeof value !== 'object' || !value.id || !value.name) {
				return msgs.NotValid;
			}
		},
		valueToSave: function(value) {
			return value.id;
		}
	}
];

