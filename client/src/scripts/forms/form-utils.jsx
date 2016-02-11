import React from 'react';
import { Tooltip, OverlayTrigger } from 'react-bootstrap';
import { server } from '../commons/server';
import { isFunction } from '../commons/utils';

const requiredTooltip = (
	<Tooltip id="required">{__('NotNull')}</Tooltip>
	);

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
			<OverlayTrigger placement="top" overlay={requiredTooltip}>
				<span>{txt}<i className="fa fa-exclamation-circle app-required"/></span>
			</OverlayTrigger> :
			txt;
	}


	/**
	 * Return the server request of the options, if available
	 * @param  {[type]} schema The element schema
	 * @param  {[type]} doc    The document of the form
	 * @return {[type]}        [description]
	 */
	static optionsRequest(schema, doc) {
		const options = schema.options;
		if (!options) {
			return null;
		}
		const req = isFunction(options) ? options(doc) : options;

		if (typeof req === 'string') {
			return { cmd: req };
		}

		return typeof req === 'object' && Object.keys(req).length <= 2 && 'cmd' in req ?
			req : null;
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

		// if options is a string, so it was (probably) resolved before,
		// so return the resources instead
		const lst = typeof options === 'string' ? resources : null;

		if (!lst) {
			return null;
		}

		// options is an array ?
		if (Array.isArray(lst)) {
			return lst;
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
	static serverRequest(req) {
		// multiple requests ?
		const mult = Array.isArray(req);
		// create the data request to be posted
		const data = mult ?
			req :
			[{
				cmd: req.cmd,
				id: 'v',
				params: req.params
			}];

		return server.post('/api/form/request', data)
			.then(res => mult ? res : res.v);
	}

}
