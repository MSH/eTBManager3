import React from 'react';
import { Row, Col } from 'react-bootstrap';

export default class GridTable extends React.Component {

	gridRender() {
		const vals = this.props.values;

		const rows = [];
		const cellSize = this.props.cellSize;
		let size = new Size();

		const cellRender = this.props.cellRender;
		if (!cellRender) {
			return null;
		}

		// cell render
		let index = 1;
		let cells = [];
		vals.forEach(item => {
			// generate content
			const content = cellRender(item);
			// rend cell
			const cell = (
				<Col {...cellSize} key={index}>
					{content}
				</Col>
			);

			cells.push(cell);

			size.add(cellSize);

			// check if next cell fits in ermaining size or if it is the last item
			if (!size.fitInSize(cellSize) || index === vals.length) {
				// add cells to the row
				rows.push(<Row key={index}>{cells}</Row>);
				// empty the left size
				size = new Size();
				// empty cells
				cells = [];
			}
			index++;
		});

		return rows;
	}

	render() {
		if (!this.props.values) {
			return null;
		}

		return <div>{this.gridRender()}</div>;
	}
}

GridTable.propTypes = {
	cellRender: React.PropTypes.func,
	cellSize: React.PropTypes.object,
	values: React.PropTypes.array
};

GridTable.defaultProps = {
	cellSize: { sm: 6 }
};

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