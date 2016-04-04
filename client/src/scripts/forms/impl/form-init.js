
import FormUtils from '../form-utils';
import { setValue, isFunction } from '../../commons/utils';


/**
 * Initialize default properties in the document
 * @param  {[type]} form [description]
 * @return {[type]}      [description]
 */
export function initDefaultValues(form) {
	const schema = form.props.schema;
	const doc = form.props.doc;

	// set default properties of the document
	const defprops = schema.defaultProperties;
	if (defprops) {
		Object.keys(defprops).forEach(prop => {
			let val = defprops[prop];
			if (isFunction(val)) {
				val = val.call(doc, doc);
			}
			doc[prop] = val;
		});
	}

	// set the default properties of the controls
	schema.layout
		.filter(elem => !!elem.property)
		.forEach(elem => {
			const type = FormUtils.getComponent(elem);

			let defaultValue;

			if ('defaultValue' in elem) {
				defaultValue = elem.defaultValue;
			} else {
				defaultValue = type.defaultValue();
			}

			if (defaultValue !== undefined) {
				setValue(doc, elem.property, defaultValue, true);
			}
		});
}
