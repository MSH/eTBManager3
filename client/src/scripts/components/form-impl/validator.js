/**
 * Validate the content of the form based on the fields definition
 */

import Element from './element';


/**
 * Validate a data model against its validation rules in the form layout
 * @param  {array} layout    List of elements that contains the rules to be validated
 * @param  {object} dm       The data model to be validated
 * @return {array}           List of errors found
 */
export function validateForm(layout, dm) {
	let errors = null;

	layout.forEach(elem => {
		if (elem.el === 'field' || !elem.el) {
			const elhandler = new Element(elem, dm);
			// validate the element
			const msg = elhandler.validate();

			if (msg) {
				if (!errors) {
					errors = {};
				}
				errors[elem.property] = msg;
			}
		}
	});

	return errors;
}
