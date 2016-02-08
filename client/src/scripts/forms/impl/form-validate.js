/**
 * Validate the content of the form based on the fields definition
 */

import { getValue, isEmpty } from '../../commons/utils';
import msgs from '../../commons/messages';


/**
 * Validate a data model against its validation rules in the form layout
 * @param  {array} elems    List of elements that contains the rules to be validated
 * @param  {object} dm       The data model to be validated
 * @return {array}           List of errors found
 */
export default function validateForm(form) {
	let errors = null;

	const snapshot = form.state.snapshot;
	const doc = form.props.doc;

	// browse all fields
	snapshot.filter(el => el.el === 'field')
	.forEach(elem => {
		const value = getValue(doc, elem.property);

		let msg;
		// no value informed ?
		if (isEmpty(value)) {
			// value is required ?
			if (elem.required) {
				// if required and empty, so show required message
				msg = msgs.NotNull;
			}
		}
		else {
			// validate value
			const comp = form.refs[elem.id];
			msg = comp.validate(elem, value, doc);
		}

		if (msg) {
			if (!errors) {
				errors = {};
			}
			errors[elem.property] = msg;
		}
	});

	return errors;
}
