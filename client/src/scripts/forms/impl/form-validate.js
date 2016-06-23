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

	const snapshots = form.state.snapshots;
	const doc = form.props.doc;

	// browse all fields
	snapshots
		.filter(it => !!it.snapshot.property)
		.forEach(elem => {
		const snapshot = elem.snapshot;
		const value = getValue(doc, snapshot.property);

		let msg;
		// no value informed ?
		if (isEmpty(value)) {
			// value is required ?
			if (snapshot.required) {
				// if required and empty, so show required message
				msg = msgs.NotNull;
			}
		}
		else {
			// validate value
			const comp = form.refs[snapshot.id];
			msg = comp.validate(snapshot, value, doc);
		}

		if (msg) {
			if (!errors) {
				errors = {};
			}
			errors[snapshot.property] = msg;
		}
	});

	return errors;
}
