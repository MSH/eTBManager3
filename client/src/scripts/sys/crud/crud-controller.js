
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
	// confirm delete
	confirmDelete: 'confirm-delete'
};

export default class CrudController {

	constructor(crud, options) {
		this.openForm = this.openForm.bind(this);
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
		this.gotoPage(this.getPage());
	}

	/**
	 * Generate events to open a new form
	 * @return {[type]} [description]
	 */
	openForm(item) {
		if (this.isFormOpen()) {
			return null;
		}

		// information about the document being edited in the form
		const formInfo = {
			doc: null,
			fetching: false,
			id: null,
			item: item
		};

		// store information in the controller about the doc being edited
		this.formInfo = formInfo;

		// is a new doc ?
		if (!item) {
			// create a new document to be saved
			formInfo.doc = {};
			// notify by event
			this._raise(Events.openForm);
			// return the document as a promise
			return Promise.resolve(formInfo);
		}

		// EXISTING DOC
		formInfo.fetching = true;
		// the default key is the ID of the item
		formInfo.id = item.id;

		const self = this;
		// get the data to edit from the crud object and return as a promise
		return this.crud.getEdit(formInfo.id)
		.then(res => {
			// set the id just to know if it is a new form
			res.id = formInfo.id;
			formInfo.fetching = false;
			formInfo.doc = res;
			// raise the event to notify the form to open
			self._raise(Events.openForm);
			// return the doc and id in the a promise
			return formInfo;
		});
	}

	/**
	 * Send message to close the current form
	 * @return {[type]} [description]
	 */
	closeForm() {
		this.formInfo.closing = true;
		this._raise(Events.closeForm);
		delete this.formInfo;
	}

	/**
	 * Save the document and close the form
	 * @param  {Obejct} doc The document to save
	 * @param  {string} id  The id of the document, null if it is a new document
	 * @return {Prmise}     The promise to be evaluated
	 */
	saveAndClose() {
		const fi = this.formInfo;
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

		const self = this;
		return prom.then(res => {
			self.closeForm();
			const msg = fi.id ? __('default.entity_updated') : __('default.entity_created');
			self._raise(Events.showMsg, msg);
			return res;
		});
	}

	/**
	 * Return true if there is a form open
	 * @return {Boolean} [description]
	 */
	isFormOpen() {
		return !!this.formInfo;
	}

	/**
	 * Return true if is a new document being edited
	 * @return {Boolean} [description]
	 */
	isNewForm() {
		return !(this.formInfo && this.formInfo.item);
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
			.then(() => self.refreshList());
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

		const qry = Object.assign(this.result.query, { pageSize: this.options.pageSize, page: page });

		return this._queryList(qry);
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
			const paging = !!this.options.pageSize;
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

			// return to the promise
			return result;
		})
		.catch(err => {
			self._raise(Events.error, err);
		});
	}
}
