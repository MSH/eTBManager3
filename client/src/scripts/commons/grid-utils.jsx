
import React from 'react';
import { Row, Col } from 'react-bootstrap';

/**
 * Arrange the components in a bootstrap grid component,
 * declaring rows and columns
 * @param  {[type]} lst [description]
 * @return {[type]}     [description]
 */
function arrangeGrid(lst) {
	// the initial row size
	let rowsize = new Size();

	// the list of rows
	const rows = [];

	// grid render
	let index = 1;
	let cols = [];
	lst.forEach(item => {
		// generate content
		const content = item.content;

		// get the cell size (width)
		const colsize = item.size ? item.size : { sm: 12 };

		// rend cell
		const col = (
			<Col {...colsize} key={index}>
				{content}
			</Col>
		);

		cols.push(col);

		rowsize.add(colsize);

		// is next item a new line ?
		const nextItem = index < lst.length ? lst[index] : null;
		const nextSize = nextItem && nextItem.size ? nextItem.size : { sm: 12 };
		const newRow = (nextItem && nextItem.size ? nextItem.newRow : false) || item.spanRow;

		// check if next cell fits in ermaining size or if it is the last item
		if (newRow || !rowsize.fitInSize(nextSize) || index === lst.length) {
			// add cells to the row
			rows.push(
				<Row key={index}>
					{cols}
				</Row>
			);

			// reset the row size
			rowsize = new Size();
			// empty columns
			cols = [];
		}
		index++;
	});

	return rows;
}

/**
 * Simple size class to handle size operation
 */
class Size {
	constructor() {
		this.xs = 0;
		this.sm = 0;
		this.md = 0;
		this.lg = 0;
	}

	add(size) {
		this.xs += size.xs ? size.xs : 0;
		this.sm += size.sm ? size.sm : 0;
		this.md += size.md ? size.md : 0;
		this.lg += size.md ? size.md : 0;
	}

	isOverflow() {
		return this.xs > 12 || this.sm > 12 || this.md > 12 || this.lg > 12;
	}

	fitInSize(size) {
		return (this.xs + (size.xs ? size.xs : 0) <= 12) &&
				(this.sm + (size.sm ? size.sm : 0) <= 12) &&
				(this.md + (size.md ? size.md : 0) <= 12) &&
				(this.lg + (size.lg ? size.lg : 0) <= 12);
	}
}


/**
 * Exported resources of the module
 */
export { Size, arrangeGrid };
