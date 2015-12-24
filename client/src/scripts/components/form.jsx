/**
 * Generate and maintain a form based on a given layout (in object structure) and a data model
 */
import React from 'react';
import { Row, Col, Alert } from 'react-bootstrap';
import FieldControl from './form-impl/field-control';
import { validateForm } from './form-impl/validator';
import { setValue } from '../commons/utils';
import commonTypes from './form-impl/common-types';
import Element from './form-impl/element';


/**
 * Create a form based on a given json layout and a data model
 */
export default class Form extends React.Component {

	/**
	 * Register a control
	 * @param  {[type]} control [description]
	 */
	static registerControl(control, defaultCtrl) {
		const name = control.controlName();
		controls[name] = control;

		if (defaultCtrl) {
			controls.$ = control;
		}
	}

	/**
	 * Register a new type to be handled by the form
	 * @param  {[type]} type [description]
	 */
	static registerType(type) {
		if (type.constructor === Array) {
			type.forEach(item => Form.registerType(item));
			return;
		}

		const tname = type.type;

		if (tname.constructor === Array) {
			tname.forEach(item => typeHandlers[item] = type);
		}
		else {
			typeHandlers[tname] = type;
		}
	}

	static getType(name) {
		return typeHandlers[name];
	}

	/**
	 * Validate the form and return validation messages, if any erro is found
	 * @param  {[type]} layout    [description]
	 * @param  {[type]} datamodel [description]
	 * @return {[type]}           [description]
	 */
	static validate(layout, datamodel) {
		return validateForm(layout, datamodel);
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
			if (elem.property && elem.defaultValue) {
				setValue(doc, elem.property, elem.defaultValue);
			}
		});
		return doc;
	}

	/**
	 * Create the form content
	 * @return {Component} Return the react component to be displayed inside the grid
	 */
	createForm() {
		const layout = this.props.layout;
		const doc = this.props.doc;
		let errors = this.props.errors;

		// check if there is any global error message
		let globalMsg = errors instanceof Error ? errors.message : null;

		// is not a list of error messages ?
		if (errors instanceof Error) {
			errors = null;
		}

		// errors handled by the fields
		this.handledErrors = [];

		const lst = this.createFields(layout, doc, errors);

		if (!globalMsg) {
			globalMsg = this.createGlobalMsgs(globalMsg);
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
	 * Create a list of global messages based on unhandled messages from the fields
	 * @return {[type]} [description]
	 */
	createGlobalMsgs(msg) {
		const errors = this.props.errors;

		if (!errors) {
			return null;
		}

		const keys = Object.keys(errors);
		const lst = [];

		if (msg) {
			lst.push(msg);
		}

		keys.forEach(key => {
			if (this.handledErrors.indexOf(key) < 0) {
				const err = errors[key];
				lst.push(<li key={key}>{key + ': ' + (err.msg ? err.msg : err)}</li>);
			}
		});

		return lst.length > 0 ? <ul>{lst}</ul> : null;
	}

	/**
	 * Return a list of errors of a specific field
	 * @param  {[type]} propname [description]
	 * @param  {[type]} errors   [description]
	 * @return {[type]}          [description]
	 */
	propertyErrors(propname, errors) {
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
				this.handledErrors.push(key);
			}
		});
		return res;
	}

	/**
	 * Find control by the element
	 * @param  {[type]} elname [description]
	 * @return {[type]}        [description]
	 */
	findControl(elname, typeHandler) {
		// no name defined, so return the default render
		if (!elname) {
			if (typeHandler.control) {
				return controls[typeHandler.control];
			}

			// return the default control
			return controls.$;
		}

		return controls[elname];
	}

	/**
	 * Create the fields of the form
	 * @param  {[type]} layout [description]
	 * @param  {[type]} doc    [description]
	 * @param  {[type]} errors [description]
	 * @return {[type]}        [description]
	 */
	createFields(layout, doc, errors) {
		if (!layout || !doc) {
			return null;
		}

		// the column size being rendered
		let row = null;

		const lst = [];
		let key = 1;
		for (let i = 0; i < layout.length; i++) {
			const elem = layout[i];
			const type = typeHandlers[elem.type];
			const ReactComp = this.findControl(elem.el, type);
			// no render was found ?
			if (!ReactComp) {
				continue;
			}

			const compErrors = this.propertyErrors(elem.property, errors);

			// create the component
			const comp = <ReactComp element={new Element(elem, doc)} doc={doc} errors={compErrors} />;

			// has information about size ?
			if (elem.size) {
				// there is NO row being processed?
				if (!row) {
					// create a new row
					row = new RowInfo('row' + key);
				}

				if (!row.fitSize(elem.size)) {
					lst.push(row.render());
					row = new RowInfo('row' + key);
				}
				row.addCol(comp, elem.size);
			}
			else {
				if (row) {
					lst.push(row.render());
					row = null;
				}
				lst.push(<Row key={key}><Col xs={12}>{comp}</Col></Row>);
			}

			key++;
		}

		if (row) {
			lst.push(row.render());
		}

		return lst;
	}

	/**
	 * Rend form
	 * @return {[type]} [description]
	 */
	render() {
		const form = this.createForm();
		return <div>{form}</div>;
	}
}

Form.propTypes = {
	layout: React.PropTypes.array,
	doc: React.PropTypes.object,
	errors: React.PropTypes.object
};


/**
 * Store temporary information about a row to be rendered, like
 * total size of the columns and the columns to be rendered
 */
class RowInfo {
	constructor(key) {
		this.key = key;
		this.size = { xs: 0, sm: 0, md: 0, lg: 0 };
		this.cols = [];
	}

	/**
	 * Test if element fits in the current column
	 * @param  {object} col  The size of the column being rendered
	 * @param  {numer|object} size The size of the element
	 * @return {boolean}      true if component does fit, otherwise return false
	 */
	fitSize(size) {
		const col = this.size;
		if (!size || size.newLine) {
			return false;
		}

		if (typeof size === 'number') {
			return col.xs + size <= 12 && col.sm + size <= 12 && col.md + size <= 12 && col.lg + size <= 12;
		}

		return (col.xs + (size.xs ? size.xs : 0) <= 12) &&
				(col.sm + (size.sm ? size.sm : 0) <= 12) &&
				(col.md + (size.md ? size.md : 0) <= 12) &&
				(col.lg + (size.lg ? size.lg : 0) <= 12);
	}

	addCol(comp, size) {
		// increase the size of the row
		const s = this.size;
		s.xs += size.xs ? size.xs : 0;
		s.sm += size.sm ? size.sm : 0;
		s.md += size.md ? size.md : 0;
		s.lg += size.lg ? size.lg : 0;

		const props = Object.assign({ key: this.key + (this.cols.length + 1) }, size);

		this.cols.push(<Col {...props}>{comp}</Col>);
	}

	render() {
		return <Row key={this.key}>{this.cols}</Row>;
	}
}


/**
 * List of components by its element name
 */
const controls = {
};

/**
 * List of field types by name
 */
const typeHandlers = {};

/**
 * Register types and controls
 */
Form.registerType(commonTypes);
Form.registerControl(FieldControl, true);
