
import React from 'react';
import { Alert } from 'react-bootstrap';
import WaitIcon from '../../components/wait-icon';
import { getValue } from '../../commons/utils';
import { arrangeGrid } from '../../commons/grid-utils';
import Form from '../form';


/**
 * Create the form content for the given form component
 * @param  {Form} form The form component
 * @return {React.Component} The rendered form content
 */
export default function createForm(form) {
	if (!form.state.resources) {
		return <WaitIcon type="card" />;
	}

	let errors = form.props.errors;

	const handledErrors = [];

	// check if there is any global error message
	let globalMsg = errors instanceof Error ? errors.message : null;

	// is not a list of error messages ?
	if (errors instanceof Error) {
		errors = null;
	}

	const sslist = [];
	// create list of components to render
	getCompList(form.state.snapshot, sslist);

	const items = sslist.map(elem => {
		const compErrors = elem.property ? propertyErrors(elem.property, errors, handledErrors) : null;
		const value = elem.el === 'field' ? getValue(form.props.doc, elem.property) : null;

		const comp = createElement(form, elem, value, compErrors);

		const size = elem.size ? elem.size : { sm: 12 };
		return { size: size, content: comp };
	});

	const lst = arrangeGrid(items);

	// called after the elements loop to search for unhandled messages
	if (!globalMsg) {
		globalMsg = createGlobalMsgs(errors, globalMsg, handledErrors);
	}

	// is there a global message
	if (globalMsg) {
		return (
			<div>
				<Alert bsStyle="danger">{globalMsg}</Alert>
				{lst}
			</div>
			);
	}

	return lst;
}

/**
 * Create an array with all elements including recursivelly all that are inside a group
 * @param  {[type]} snapshot [description]
 * @param  {[type]} lst      [description]
 * @return {[type]}          [description]
 */
function getCompList(snapshot, lst) {
	snapshot.forEach(elem => {
		if (elem.el === 'group') {
			if ('visible' in elem && elem.visible) {
				getCompList(elem.layout, lst);
			}
		}
		else {
			lst.push(elem);
		}
	});
}

/**
 * Create the component of a given schema
 * @param  {[type]} schema [description]
 * @param  {[type]} value  [description]
 * @param  {[type]} errors [description]
 * @return {[type]}        [description]
 */
function createElement(form, schema, value, errors) {
	if (schema.el === 'subtitle') {
		return <div className="subtitle">{schema.label}</div>;
	}

	if (__DEV__) {
		// check if property was defined
		if (!schema.property) {
			throw new Error('Property not defined in schema');
		}

		// check if type was defined
		if (!schema.type) {
			throw new Error('Type not defined. Property ' + schema.property);
		}
	}

	// get any resource that came from the object
	const res = form.state.resources[schema.id];

	// simplify error handling, sending just a string if there is
	// just one single error for the property
	let err;
	if (errors && Object.keys(errors).length === 1 && schema && errors[schema.property]) {
		err = errors[schema.property];
	}
	else {
		err = errors;
	}

	const Comp = Form.types[schema.type];

	if (__DEV__) {
		if (!Comp) {
			throw new Error('Invalid type: ' + schema.type);
		}
	}

	return (
		<Comp ref={schema.id} schema={schema} value={value} resources={res}
			onChange={form._onChange} errors={err} />
		);
}

/**
 * Return a list of errors of a specific field
 * @param  {[type]} propname [description]
 * @param  {[type]} errors   [description]
 * @return {[type]}          [description]
 */
function propertyErrors(propname, errors, handledErrors) {
	if (!errors) {
		return null;
	}

	const keys = Object.keys(errors);
	const res = {};
	keys.forEach(key => {
		if (key.startsWith(propname)) {
			const error = errors[key];
			res[key] = error.msg ? error.msg : error;
			// add error messages that are handled
			handledErrors.push(key);
		}
	});
	return Object.keys(res).length === 0 ? null : res;
}


/**
 * Create a list of global messages based on unhandled messages by the fields
 * @return {[type]} [description]
 */
function createGlobalMsgs(errors, msg, handledErrors) {
	if (!errors) {
		return null;
	}

	const keys = Object.keys(errors);
	const lst = [];

	if (msg) {
		lst.push(msg);
	}

	keys.forEach(key => {
		if (handledErrors.indexOf(key) < 0) {
			const err = errors[key];
			lst.push(<li key={key}>{key + ': ' + (err.msg ? err.msg : err)}</li>);
		}
	});

	return lst.length > 0 ? <ul>{lst}</ul> : null;
}
