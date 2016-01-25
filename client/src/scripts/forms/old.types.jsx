/**
 * Handle type validation and convertion in the app
 */

import TypeHandler from './impl/type-handler';
import registerCommonTypes from './impl/common-types';


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

	_types.Class[Type.name] = Type;

	if (name.constructor === Array) {
		name.forEach(k => _types.list[k] = instance);
	}
	else {
		_types.list[name] = instance;
	}
}


/**
 * All registered types
 * @type {Object}
 */
var _types = {
	register: registerType,
	BaseClass: TypeHandler,
	list: {},
	Class: {}
};


/**
 * Values to export
 */
export default _types;

/**
 * Register the common types
 */
registerCommonTypes(_types.register);
