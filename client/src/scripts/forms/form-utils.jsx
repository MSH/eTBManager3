import React from 'react';
import { server } from '../commons/server';
import { isFunction } from '../commons/utils';


export default class FormUtils {

	/**
	 * Generate a new label component to be displayed as the label of an input control.
	 * Includes a red icon on the right side to indicate required fields
	 * @param  {[type]} schema [description]
	 * @return {[type]}        [description]
	 */
	static labelRender(label, required) {
		if (!label) {
			return null;
		}

		const txt = label + ':';

		return required ?
			<span>{txt}<i className="fa fa-exclamation-circle app-required"/></span> :
			txt;
	}


	/**
	 * Create the options for a select box control
	 * @param  {[type]} options [description]
	 * @return {[type]}         [description]
	 */
	static createOptions(options, resources) {
		if (!options && !resources) {
			return null;
		}

		const lst = typeof options === 'string' ? resources : options;

		if (!lst) {
			return null;
		}

		let opts = [];
		if (lst.constructor !== Array && typeof lst === 'object') {
			const keys = Object.keys(lst);
			if (keys.length === 2 && lst.from && lst.to) {
				for (var i = lst.from; i <= lst.to; i++) {
					opts.push({ id: i.toString(), name: i.toString() });
				}
			}
			else {
				keys.forEach(key => opts.push({ id: key, name: lst[key] }));
			}
		}
		else {
			opts = opts.concat(lst);
		}

		// create component for list of options
		return opts;
	}


	/**
	 * Request the server to initialize the given fields
	 * @param  {Array} req list of field objects with id, type and value
	 * @return {Promise}   Promise to be resolved when server answers back
	 */
	static initFields(req) {
		const data = Array.isArray(req) ? req : [req];
		return server.post('/api/form/request', data);
	}


}
