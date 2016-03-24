
const Events = {
	// List is changed
	list: 'list',
	// An error occur
	error: 'error',
	// Page is changed
	page: 'page',
	// fetching the list
	fetchingList: 'fetching-list',
	// open the new form
	newForm: 'new-form',
	closeNewForm: 'close-new-form',
	// display a message on the screen
	showMsg: 'show-msg'
};

export default class CrudController {

	constructor(crud, options) {
		this.openNewForm = this.openNewForm.bind(this);
		this.cancelNewForm = this.cancelNewForm.bind(this);

		this.listeners = [];
		this.crud = crud;
		this.options = options || {};
		this.state = null;
	}

	/**
	 * Register a listener to the controller
	 * @param  {[type]} listener [description]
	 * @return {[type]}          [description]
	 */
	on(listener) {
		this.listeners.push(listener);
	}

	/**
	 * Open the list and display that to the screen
	 * @param  {[type]} qry [description]
	 * @return {[type]}     [description]
	 */
	initList(qry) {
		// check if query is paged
		const q = this.options.pageSize ?
			Object.assign(qry || {}, { pageSize: this.options.pageSize, page: 0 }) :
			qry;

		return this._queryList(q);
	}

	/**
	 * Refresh the list
	 * @return {[type]} [description]
	 */
	refreshList() {
		this.gotoPage(this.getPage());
	}

	openNewForm() {
		this.newFormOpen = true;
		this._raise(Events.newForm);
	}

	cancelNewForm() {
		this._closeNewForm();
	}

	isNewFormOpen() {
		return this.newFormOpen;
	}

	saveNewForm(doc) {
		const self = this;
		return this.crud.create(doc)
		.then(res => {
			self._raise(Events.showMsg, __('default.entity_created'));
			self._closeNewForm();
			return res.id;
		});
	}

	_closeNewForm() {
		this.newFormOpen = false;
		this._raise(Events.closeNewForm);
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
			// wrap itens inside a controller object
			const list = res.list.map(item => ({ data: item, state: 'ok' }));
			// generate new result
			const result = {
				count: res.count,
				list: list,
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
