/**
 * Generate and maintain a form based on a given layout (in object structure) and a data model
 */
import React from 'react';
import { Row, Col, Alert } from 'react-bootstrap';
import Field from './form-impl/field';
import { validateForm } from './form-impl/validator';


/**
 * Create a form based on a given json layout and a data model
 */
export default class Form extends React.Component {

	static validate(layout, datamodel) {
		return validateForm(layout, datamodel);
	}

	findComp(elname) {
		// no name defined, so return the default render
		if (!elname) {
			return components.field;
		}

		return components[elname];
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
			const ReactComp = this.findComp(elem.el);
			// no render was found ?
			if (!ReactComp) {
				continue;
			}

			const compErrors = this.propertyErrors(elem.property, errors);

			// create the component
			const comp = <ReactComp element={elem} doc={doc} errors={compErrors} />;

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
		if (!size) {
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
const components = {
	field: Field
};
