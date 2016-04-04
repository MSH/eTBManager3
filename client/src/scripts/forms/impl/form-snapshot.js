
import FormUtils from '../form-utils';

/**
 * Create an snapshot of the form, based on the form schema and the document assigned to it.
 * An snapshot is the current status of the schema properties for the document. It is called
 * on the initialization of the form and whenever the document is changed during its editing,
 * so the new snapshot is compared with the previous one to update the fields in the form
 *
 * @param  {Object} schemas The form schema
 * @param  {Object} doc    The document in use
 * @param  {Object} parentSnapshot The parent snapshot in a component tree structure. Initially it is the own form
 * @return {Array}         List of form elements state to the given document
 */

export default function createSnaphost(form) {
	const creator = new SnapshotCreator(form);
	return creator.create(form);
}

/**
 * Responsible for creating a snapshot of a form
 */
class SnapshotCreator {

	/**
	 * Create the snapshot of a given for
	 * @param  {[type]} form [description]
	 * @return {[type]}      [description]
	 */
	create(form) {
		this.form = form;
		const schema = form.props.schema;
		const formSnapshot = {
			readOnly: form.props.readOnly
		};

		this.form = form;
		this.list = [];

		this._traverse(schema.layout, formSnapshot);

		return this.list;
	}

	_traverse(schemas, parent, pref) {
		const doc = this.form.props.doc;
		const self = this;

		schemas.forEach((schema, index) => {
			const comp = FormUtils.getComponent(schema);
			// create new snapshot, using the parent to overwrite common properties
			const snapshot = Object.assign({}, comp.snapshot(schema, doc), parent);

			// if not visible, don't include in the list
			if (!snapshot.visible) {
				return;
			}

			// the id of the element, composed with the parent name
			const id = (pref ? pref + ':' : '') +
				(schemas.property ? schemas.property + index : 'ctrl' + index);
			// list of children elements
			const children = comp.children(schema);

			// there is any children ?
			if (children) {
				self._traverse(children, snapshot, id);
			}
			else {
				const data = {
					id: id,
					// pointer to the original schema
					schema: schema,
					comp: comp,
					snapshot: Object.assign({}, snapshot, parent),
					prev: self._findPrev(id)
				};
				self.list.push(data);
			}

		});
	}

	/**
	 * Find previous snapshot by id the schema ID
	 * @param  {string} id The schema ID
	 * @return {Object}    The data containing the snapshot
	 */
	_findPrev(id) {
		// get the list of snapshots in the form state
		const lst = this.form.state.snapshots;
		if (!lst) {
			return null;
		}

		// search for previous snapshot in the list
		const res = lst.find(item => item.id === id);
		return res ? res.snapshot : null;
	}

}
