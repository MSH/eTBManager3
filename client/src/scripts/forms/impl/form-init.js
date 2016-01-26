
import FormUtils from '../form-utils';
import Form from '../form';
import { setValue, getValue } from '../../commons/utils';


/**
 * Initialize form. Called once when form is included in the DOM to check
 * if any field requires initialization from server side.
 * If so, sends a request to server to get field resources
 * @return {Promise} Null if no initialization is required, otherwise returns a promise that will be
 * resolved when resources are returned from server
 */
export default function initForm(form, snapshot) {

	initDefaultValues(form);

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
		if (!form.onInit) {
			return resolve(FormUtils.initFields(req));
		}

		resolve(form.onInit(req));
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

	snapshot
	.filter(sc => sc.el === 'field')
	.forEach(sc => {
		// doesn't require server init ?
		const Comp = Form.types[sc.type];

		if (!Comp.isServerInitRequired(sc)) {
			return;
		}

		const req = {
			id: sc.id,
			type: sc.type,
			value: getValue(doc, sc.property)
		};

		lst.push(req);
	});

	return lst.length === 0 ? null : lst;
}


/**
 * Initialize default properties in the document
 * @param  {[type]} form [description]
 * @return {[type]}      [description]
 */
function initDefaultValues(form) {
	const schema = form.props.schema;
	const doc = form.props.doc;

	// set default properties of the document
	const defprops = schema.defaultProperties;
	if (defprops) {
		Object.keys(defprops).forEach(prop => {
			doc[prop] = defprops[prop];
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
