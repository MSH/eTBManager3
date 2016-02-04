import React from 'react';
import { server } from '../commons/server';
import { app } from '../core/app';


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
		opts.push({ id: '-', name: '-' });
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
//		return opts.map(opt => <option key={opt.id} value={opt.id}>{opt.name}</option>);
	}


	/**
	 * Request the server to initialize the given fields
	 * @param  {Array} req list of field objects with id, type and value
	 * @return {Promise}   Promise to be resolved when server answers back
	 */
	static initFields(req) {
		return server.post('/api/form/initfields', req);
	}

	/**
	 * Initialize a form making a request to the server
	 * @param  {[type]} params [description]
	 * @return {[type]}        [description]
	 */
	static initForm(params) {
		const formSchema = params.formSchema;
		const crud = params.crud;

		const fields = FormUtils.generateFormState(formSchema);

		if (__DEV__) {
			if (params.id && !crud) {
				throw new Error('No crud defined');
			}
		}

		// crud was informed ?
		if (crud) {
			const req = {
				id: params.id,
				fields: fields
			};
			return crud.initForm(req);
		}

		// No field to initialize ?
		if (!fields) {
			return null;
		}

		// call the server to initialize fields
		return FormUtils.initFields(fields);
	}


}
