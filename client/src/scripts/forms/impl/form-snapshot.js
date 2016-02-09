
import { isFunction } from '../../commons/utils';

/**
 * properties to evaluate during spanshot creation
 */
const evalProps = [
	'readOnly', 'visible', 'label', 'required', 'disabled'
];

/**
 * Create an snapshot of the form, based on the form schema and the document assigned to it.
 * An snapshot is the current status of the schema properties for the document. It is called
 * on the initialization of the form and whenever the document is changed during its editing,
 * so the new snapshot is compared with the previous one to update the fields in the form
 *
 * @param  {Object} schema The form schema
 * @param  {Object} doc    The document in use
 * @return {Array}         List of form elements state to the given document
 */
export default function createSnapshot(schema, doc) {
	const lst = schema.layout.map((elem, index) => {
		// create element state
		const state = Object.assign({ el: 'field' },
			{ id: elem.property ? elem.property + index : 'elem' + index },
			elem);

		// replace functions by properties values
		evalProps.forEach(prop => {
			const val = state[prop];
			if (isFunction(val)) {
				const res = val.call(doc, doc);
				state[prop] = res;
			}
		});

		// element contains another set of elements ?
		if (state.layout) {
			state.layout = createSnapshot(elem, doc);
		}
		return state;
	});

	return lst.length > 0 ? lst : null;
}
