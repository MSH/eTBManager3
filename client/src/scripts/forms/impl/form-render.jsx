
import React from 'react';
import { Alert } from 'react-bootstrap';
import WaitIcon from '../../components/wait-icon';
import { getValue } from '../../commons/utils';
import { arrangeGrid } from '../../commons/grid-utils';


/**
 * Create the form content for the given form component
 * @param  {Form} form The form component
 * @return {React.Component} The rendered form content
 */
export default function formRender(form) {
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

	const snapshots = form.state.snapshots;

	const items = snapshots.map(item => {
		const snapshot = item.snapshot;
		const compErrors = snapshot.property ? propertyErrors(snapshot.property, errors, handledErrors) : null;
		const value = snapshot.property ? getValue(form.props.doc, snapshot.property) : null;

		const comp = createElement(form, item, value, compErrors);

		const size = snapshot.size ? snapshot.size : { sm: 12 };
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
 * Create the component of a given schema
 * @param  {[type]} schema [description]
 * @param  {[type]} value  [description]
 * @param  {[type]} errors [description]
 * @return {[type]}        [description]
 */
function createElement(form, item, value, errors) {
	const snapshot = item.snapshot;
	// get any resource that came from the object
	const res = form.state.resources[snapshot.id];

	// simplify error handling, sending just a string if there is
	// just one single error for the property
	let err;
	if (errors && Object.keys(errors).length === 1 && snapshot && errors[snapshot.property]) {
		err = errors[snapshot.property];
	}
	else {
		err = errors;
	}

	const Comp = item.comp;

	if (__DEV__) {
		if (!Comp) {
			throw new Error('Invalid type: ' + snapshot.type);
		}
	}

	return (
		<Comp ref={snapshot.id}
			schema={snapshot}
			value={value}
			resources={res}
			onChange={form._onChange}
			errors={err}
			onRequest={form._onRequest} />
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
