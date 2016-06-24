
const Events = {
	// List is changed
	list: 'list',
	// An error occur
	error: 'error',
	// Page is changed
	page: 'page',
	// fetching the list
	fetchingList: 'fetching-list',
	// display a message on the screen
	showMsg: 'show-msg',
	// Open a form
	openForm: 'open-form',
	// Close the opened form
	closeForm: 'close-form',
	// after a editing, the item is being reloaded from server
	fetchingItem: 'fetching-item',
	// when item data is updated after a fetching
	itemUpdated: 'item-updated',
	// confirm delete
	confirmDelete: 'confirm-delete'
};

export default class CrudController {

	constructor(crud, options) {
		if (__DEV__) {
			if (!options || !options.editorSchema) {
				throw new Error('editorSchema must be specified in CrudController options');
			}
		}
		this.openNewForm = this.openNewForm.bind(this);
		this.openEditForm = this.openEditForm.bind(this);
		this.closeForm = this.closeForm.bind(this);
		this.saveAndClose = this.saveAndClose.bind(this);

		this.listeners = [];
		this.crud = crud;
		this.options = options || {};
		this.state = null;
		this.formOpen = false;
	}

	/**
	 * Register a listener to the controller
	 * @param  {[type]} listener [description]
	 * @return {[type]}          [description]
	 */
	on(listener) {
		this.listeners.push(listener);
		return listener;
	}

	removeListener(listener) {
		const index = this.listeners.indexOf(listener);
		this.listeners.splice(index, 1);
	}

	/**
	 * Open the list and display that to the screen
	 * @param  {[type]} qry [description]
	 * @return {[type]}     [description]
	 */
	initList(qry) {
		// check if query is paged
		const q = this.options.pageSize ?
			Object.assign({}, qry, { pageSize: this.options.pageSize, page: 0 }) :
			qry || {};

		return this._queryList(q);
	}

	/**
	 * Given an item from the list, return its id
	 * @param  {object} item Item from the list returned from the CRUD object
	 * @return {string}      The ID of the item
	 */
	resolveId(item) {
		// temporary
		return item.id;
	}

	/**
	 * Search for an item from the list by its ID
	 * @param  {string} id The ID of an item in the list
	 * @return {object}    The item that matches the ID, or undefined if item with ID is not found
	 */
	itemById(id) {
		const self = this;
		return this.result.list.find(item => id === self.resolveId(item));
	}

	/**
	 * Return true if the form is read-only
	 * @return {Boolean} [description]
	 */
	isReadOnly() {
		return !!this.options.readOnly;
	}

	/**
	 * Refresh the list
	 * @return {[type]} [description]
	 */
	refreshList() {
		return this.gotoPage(this.getPage());
	}

	/**
	 * Return the available schema editor keys and its title. Used to create a popup menu
	 * @return {[type]} [description]
	 */
	getEditors() {
		const schema = this.options.editorSchema;
		if (!schema.editors) {
			return null;
		}

		return Object.keys(schema.editors).map(key => ({ key: key, label: schema.editors[key].label }));
	}

	/**
	 * Generate an event to create a new form and optionally passes the editor schema key, if it
	 * is a mulit schema
	 * @param  {[type]} key [description]
	 * @return {[type]}     [description]
	 */
	openNewForm(key) {
		return this._openForm(null, key);
	}

	/**
	 * Generate an event to open an edit form based on its document to edit
	 * @param  {[type]} item [description]
	 * @return {[type]}      [description]
	 */
	openEditForm(item) {
		return this._openForm(item);
	}

	/**
	 * Generate events to open a new form
	 * @return {[type]} [description]
	 */
	_openForm(item, key) {
		if (this.isFormOpen()) {
			return null;
		}

		// select the schema to be used in the form
		const se = this.options.editorSchema;
		var schema;
		// is mulit schema ?
		if (se.editors) {
			if (__DEV__) {
				if (!item && !key) {
					throw new Error('CrudController: In a multi schema, either the item or the schema key must be informed');
				}
			}
			// select the schema by its item or by the key
			schema = se.editors[key ? key : se.select(item)];
		}
		else {
			schema = se;
		}

		// information about the document being edited in the form
		const formInfo = {
			doc: null,
			id: null,
			fetching: true,
			item: item,
			schema: schema
		};

		// store information in the controller about the doc being edited
		this.frm = formInfo;

		// is a new doc ?
		if (!item) {
			// create a new document to be saved
			formInfo.doc = {};
			// notify by event
			this._raise(Events.openForm, formInfo);
			// return the document as a promise
			return Promise.resolve(formInfo);
		}

		// EXISTING DOC
		formInfo.fetching = true;
		// the default key is the ID of the item
		formInfo.id = this.resolveId(item);

		const self = this;
		// get the data to edit from the crud object and return as a promise
		return this.crud.getEdit(formInfo.id)
		.then(res => {
			// set the id just to know if it is a new form
			res.id = formInfo.id;
			formInfo.fetching = false;
			formInfo.doc = res;

			// raise the event to notify the form to open
			self._raise(Events.openForm, formInfo);

			self.hideMessage();
			// return the doc and id in the a promise
			return formInfo;
		});
	}

	/**
	 * Send message to close the current form
	 * @return {[type]} [description]
	 */
	closeForm() {
		this.frm.closing = true;
		this._raise(Events.closeForm, this.frm);
		this.hideMessage();
		delete this.frm;
	}

	/**
	 * Save the document and close the form
	 * @param  {Obejct} doc The document to save
	 * @param  {string} id  The id of the document, null if it is a new document
	 * @return {Prmise}     The promise to be evaluated
	 */
	saveAndClose() {
		const fi = this.frm;
		if (__DEV__) {
			if (!fi) {
				throw new Error('There is no open form to be saved');
			}
		}

		// remove the id of the document passed before
		if (fi.id) {
			delete fi.doc.id;
		}

		const prom = fi.id ? this.crud.update(fi.id, fi.doc) : this.crud.create(fi.doc);

		const isNew = !fi.id;

		const self = this;
		return prom.then(res => {
			self.closeForm();

			const id = isNew ? res : fi.id;

			// remove information about open form
			delete self.frm;

			if (this.options.refreshAll) {
				return this.refreshList();
			}

			return this._updateItem(id, isNew);
		});
	}

	/**
	 * Called by function saveAndClose to update the item that was edited or inserted
	 * @param  {[type]}  id    [description]
	 * @param  {Boolean} isNew [description]
	 * @return {[type]}        [description]
	 */
	_updateItem(id, isNew) {
		// informing that the item is being loaded
		if (id) {
			this._raise(Events.fetchingItem, id);
		}

		const self = this;
		// reload the item in order to be displayed again
		return this.crud.query({ id: id })
		.then(res => {
			// the new item that will replace the current item
			const newitem = res.list[0];

			// create a copy of the list to be updated
			const lst = this.result.list.slice(0);
			this.result.list = lst;

			// is a new item ?
			if (isNew) {
				// add a new item in the list
				lst.unshift(newitem);
				// if list size is bigger than the page size, remove the last item
				if (this.options.pageSize && lst.length > this.options.pageSize) {
					lst.pop();
				}
				// update the new list
				this.result.list = lst;
				self._raise(Events.list, this.result);
			}
			else {
				// search for the position of the item in the list
				const index = lst.findIndex(item => id === self.resolveId(item));
				if (__DEV__) {
					// if item was not found, raise an error
					if (index === -1) {
						throw new Error('Item not found in the list');
					}
				}
				// replace item
				lst[index] = newitem;

				// generate event to update the item being displayed
				self._raise(Events.itemUpdated, { id: id, item: newitem });
			}

			const msg = id ? __('default.entity_updated') : __('default.entity_created');
			self.showMessage(msg);
		});
	}

	/**
	 * Raise an event to display a message
	 * @param  {String} msg The message to be displayed
	 */
	showMessage(msg, type) {
		this._raise(Events.showMsg, { msg: msg, type: type });
	}

	/**
	 * Raise an event to hide any message displayed
	 */
	hideMessage() {
		this.showMessage(null);
	}

	/**
	 * Return true if there is a form open
	 * @return {Boolean} [description]
	 */
	isFormOpen() {
		return !!this.frm;
	}

	/**
	 * Return true if is a new document being edited
	 * @return {Boolean} [description]
	 */
	isNewForm() {
		return !(this.frm && this.frm.item);
	}

	/**
	 * Get the ID of the item being edited
	 * @return {String} The ID of the document being edited
	 */
	getFormItemId() {
		return this.frm ? this.frm.id : null;
	}

	getFormSchema() {
		return this.frm ? this.frm.schema : null;
	}

	/**
	 * Init the deleting of the document represented by the item in teh list
	 * @param  {[type]} item [description]
	 * @return {[type]}      [description]
	 */
	initDelete(item) {
		this.item = item;
		const data = {
			item: item,
			title: __('action.delete'),
			msg: __('form.confirm_remove')
		};

		this.hideMessage();
		this._raise(Events.confirmDelete, data);
	}

	confirmDelete() {
		if (__DEV__) {
			if (!this.item) {
				throw new Error('No item to be deleted');
			}
		}

		const self = this;
		return this.crud.delete(this.item.id)
			.then(res => {
				if (!res.errors) {
					return self.refreshList()
					.then(() => self.showMessage(__('default.entity_deleted')));
				}

				return self.showMessage(res.errors, 'error');
			});
	}

	/**
	 * Go to the give page reading the list from the server
	 * @param  {[type]} page [description]
	 * @return {[type]}      [description]
	 */
	gotoPage(page) {
		if (__DEV__) {
			if (!this.result) {
				throw new Error('List must be initialized first');
			}
		}

		// check if there is any open form
		if (this.frm) {
			this.closeForm();
		}

		const qry = Object.assign(this.result.query, { pageSize: this.options.pageSize, page: page });

		return this._queryList(qry);
	}

	/**
	 * Check if paging is enabled
	 * @return {Boolean} Return true if paging is enabled
	 */
	isPaging() {
		return !!this.options.pageSize;
	}

	/**
	 * Get the current page
	 * @return {[type]} [description]
	 */
	getPage() {
		return this.result && this.result.page;
	}

	getPageIni() {
		return this.result.page * this.options.pageSize;
	}

	getPageEnd() {
		return this.getPageIni() + this.options.pageSize - 1;
	}

	getPageCount() {
		return this.result && this.result.pageCount;
	}

	getCount() {
		return this.result && this.result.count;
	}

	/**
	 * Return the list stored by the controller
	 * @return {Array} List of items
	 */
	getList() {
		return this.result && this.result.list;
	}

	isFetching() {
		return this.state === Events.fetchingList;
	}

	getState() {
		return this.state;
	}

	/**
	 * raise an event notifying it to the listeners
	 * @param  {[type]} event [description]
	 * @param  {[type]} data  [description]
	 * @return {[type]}       [description]
	 */
	_raise(event, data) {
		this.state = event;
		this.listeners.forEach(listener => listener(event, data));
	}

	/**
	 * Query the list
	 * @param  {[type]} qry [description]
	 * @return {[type]}     [description]
	 */
	_queryList(qry) {
		this._raise(Events.fetchingList);

		const self = this;

		return this.crud.query(qry)
		.then(res => {
			const paging = !!self.options.pageSize;
			// generate new result
			const result = {
				count: res.count,
				list: res.list,
				pageCount: paging ? Math.ceil(res.count / this.options.pageSize) : null,
				page: qry.page,
				query: qry
			};

			// save current list
			self.result = result;

			// notify about the loaded list
			if (paging) {
				self._raise(Events.page, result);
			}
			self._raise(Events.list, result);
			self.hideMessage();

			// return to the promise
			return result;
		})
		.catch(err => {
			self._raise(Events.error, err);
		});
	}
}
