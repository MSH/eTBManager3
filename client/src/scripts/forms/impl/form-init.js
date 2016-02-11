
import FormUtils from '../form-utils';
import Form from '../form';
import { setValue, getValue } from '../../commons/utils';


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
			if (typeof val === 'function') {
				val = val.call(doc, doc);
			}
			doc[prop] = val;
		});
	}

	// set the default properties of the controls
	schema.layout
			.filter(elem => elem.el === 'field' || !elem.el)
			.forEach(elem => {
				const type = Form.types[elem.type];

				const defaultValue = elem.defaultValue || type.defaultValue();
				if (defaultValue !== undefined) {
					setValue(doc, elem.property, defaultValue, true);
				}
			});
}

/**
 * Initialize form. Called once when form is included in the DOM to check
 * if any field requires initialization from server side.
 * If so, sends a request to server to get field resources
 * @return {Promise} Null if no initialization is required, otherwise returns a promise that will be
 * resolved when resources are returned from server
 */
export function initForm(form, snapshot) {

	return new Promise(resolve => {
		const resources = form.props.resources;

		// resources are already available ?
		if (resources) {
			return resolve(resources);
		}

		// get the request to be made to the server
		const req = createInitRequest(snapshot, form.props.doc);

		// if there is no need for a request, return null
		if (!req) {
			return resolve({});
		}

		// is there a custom initialization function ?
		return resolve(FormUtils.serverRequest(req));
	});
}


/**
 * Create form initialization request to be sent to the server in order
 * to initialize the fields in the form
 * @param  {[type]} state [description]
 * @return {[type]}       [description]
 */
function createInitRequest(snapshot, doc) {
	const lst = [];

	getFieldsRequest(snapshot, doc, lst);

	return lst.length === 0 ? null : lst;
}


/**
 * Collect all field elements that requires initialization from the server
 * and include them in the given list. This function is recursive. If an element
 * contains a 'layout' property, it will be evaluated as well
 * @param  {Array} snapshot List of elements
 * @param  {Object} doc     The document model in use
 * @param  {Array} lst      List to include elements
 */
function getFieldsRequest(snapshot, doc, lst) {
	snapshot.forEach(sc => {
		if (sc.layout) {
			getFieldsRequest(sc.layout, doc, lst);
		}

		const req = schemaRequest(sc, doc);

		if (req) {
			lst.push(req);
		}
	});
}

/**
 * Generate a request object of a given element schema
 * @param  {object} sc  The schema snapshot of the element
 * @param  {object} doc The document being edited in the form
 * @return {object}     The request to be sent to the server, or null if no request is necessary
 */
export function schemaRequest(sc, doc) {
	if (sc.el !== 'field') {
		return null;
	}

	// doesn't require server init ?
	const Comp = Form.types[sc.type];

	const val = getValue(doc, sc.property);

	// get server request, if any
	const req = Comp.getServerRequest ? Comp.getServerRequest(sc, val, doc) : null;

	return req ? {
		id: sc.id,
		cmd: req.cmd,
		params: req.params
	} : null;
}
