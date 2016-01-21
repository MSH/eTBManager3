import React from 'react';
import { server } from '../commons/server';
import { setValue } from '../commons/utils';


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
	 * Create a new instance of the document to be edited in the form based on default values
	 * of the form layout
	 * @param  {[type]} layout [description]
	 * @return {[type]}        [description]
	 */
	static newInstance(layout) {
		const doc = {};
		layout.forEach(elem => {
			if (elem.property) {
				setValue(doc, elem.property, elem.defaultValue);
			}
		});
		return doc;
	}

	/**
	 * Request the server to initialize the given fields
	 * @param  {Array} req list of field objects with id, type and value
	 * @return {Promise}   Promise to be resolved when server answers back
	 */
	static initFields(req) {
		return server.post('/api/form/initfields', req);
	}

}
