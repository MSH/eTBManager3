
import FormUtils from '../form-utils';
import { getValue, setValue, isFunction } from '../../commons/utils';


/**
 * Initialize default properties in the document
 * @param  {[type]} form [description]
 * @return {[type]}      [description]
 */
export function initDefaultValues(form) {
	const schema = form.props.schema;
	const doc = form.props.doc;

	initFromSchema(schema, doc);
	initFromControls(schema, doc);
}

/**
 * Initialize default values from the form schema
 * @param  {Object} schema The form schema
 * @param  {Object} doc    The document being edited in the form
 */
function initFromSchema(schema, doc) {
	// set default properties of the document
	const defprops = schema.defaultProperties;
	if (!defprops) {
		return;
	}

	// browse the default properties
	Object.keys(defprops).forEach(prop => {
		// check if there is already a property value
		if (getValue(doc, prop) !== undefined) {
			return;
		}

		// get default value
		let val = defprops[prop];
		if (isFunction(val)) {
			val = val.call(doc, doc);
		}
		// set default value
		setValue(doc, prop, val);
	});
}

/**
 * Initialize the default values from the controls,
 * in case the control assumes a default value
 * @param  {Object} schema The form schema
 * @param  {Object} doc    The document being edited in the form
 */
function initFromControls(schema, doc) {
	// set the default properties of the controls
	schema.layout
		.filter(elem => !!elem.property && getValue(doc, elem.property) === undefined)
		.forEach(elem => {
			const control = FormUtils.getComponent(elem);
			const val = control.defaultValue();

			if (val) {
				setValue(doc, elem.property, val, true);
			}
		});
}
