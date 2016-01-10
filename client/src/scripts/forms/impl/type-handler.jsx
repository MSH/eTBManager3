
/**
 * Base class for type declaration
 */
export default class TypeHandler {

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

	/**
	 * The form component to be used in a form editor
	 * @return {[type]} [description]
	 */
	formComponent() {
		return null;
	}

	toServer(value) {
		return value;
	}
}
