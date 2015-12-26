/**
 * Application state storage
 */
export default class Storage {

	/**
	 * Create a state storage with an initial state
	 * @param  {[type]} inistate [description]
	 * @return {[type]}          [description]
	 */
	constructor(inistate) {
		this.state = inistate || {};
		this.listeners = [];
	}

	/**
	 * Add a listener to the state storage
	 * @param  {[type]} listener function listener to receive notification about storage changes
	 * @return {[type]}          [description]
	 */
	addListener(listener) {
		if (this.listeners.indexOf(listener) === -1) {
			this.listeners.push(listener);
		}
	}

	/**
	 * Remove a listener from the storage
	 * @param  {[type]} listener Listener function previously registered
	 * @return {[type]}          [description]
	 */
	removeListener(listener) {
		var index = this.listeners.indexOf(listener);
		if (index >= 0) {
			this.listeners.splice(index, 1);
		}
	}

	/**
	 * Set application state, mixing the current state with the new given one
	 * @param {String} action The action that originated the state change
	 * @param {[type]} obj    THe state to be incorporated to the current state
	 */
	dispatch(action, obj) {
		// call listeners
		const states = [];
		const lst = this.listeners;
		lst.forEach(listener => {
			const res = listener(action, obj);
			if (res) {
				states.push(res);
			}
		});

		// update state, if any
		states.forEach(st => {
			for (var p in st) {
				const val = st[p];
				if (val === undefined || val === null) {
					delete this.state[p];
				}
				else {
					this.state[p] = obj[p];
				}
			}


		});
	}

	getState() {
		return this.state;
	}
}
