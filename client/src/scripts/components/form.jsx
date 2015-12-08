/**
 * Generate and maintain a form based on a given layout (in object structure) and a data model
 */
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
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
		const errors = this.props.errors;

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

			// create the component
			const comp = <ReactComp element={elem} doc={doc} errors={errors} />;

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


	render() {
		const form = this.createForm();
		return (
			<Grid fluid>
				{form}
			</Grid>
			);
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
