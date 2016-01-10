import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { Size } from '../commons/grid-utils';

export default class GridTable extends React.Component {

	/**
	 * Render the grid in multiple rows mode
	 * @return {array} List of React components
	 */
	gridRender() {
		const vals = this.props.values;

		const rows = [];
		const onCellSize = this.props.onCellSize;

		let size = new Size();

		const cellRender = this.props.onCellRender;
		if (!cellRender) {
			return null;
		}

		// cell render
		let index = 1;
		let cells = [];
		vals.forEach(item => {
			// generate content
			const content = cellRender(item, index - 1);

			// get the cell size (width)
			const cellSize = onCellSize ? onCellSize(item) : this.props.cellSize;

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


	/**
	 * Render the grid in sigle column mode
	 * @return {array} List of React components
	 */
	singleColRender() {
		const vals = this.props.values;

		const rows = [];

		const cellRender = this.props.onCellRender;
		if (!cellRender) {
			return null;
		}

		// cell render
		let index = 1;
		vals.forEach(item => {
			// rend cell
			const row = (
				<Row key={index}>
					{cellRender(item)}
				</Row>
			);

			rows.push(row);

			index++;
		});

		return rows;
	}

	render() {
		if (!this.props.values) {
			return null;
		}

		return (
			<div className="grid-table">
				{this.props.singleColumn ? this.singleColRender() : this.gridRender()}
			</div>
			);
	}
}

GridTable.propTypes = {
	onCellRender: React.PropTypes.func,
	cellSize: React.PropTypes.object,
	values: React.PropTypes.array,
	singleColumn: React.PropTypes.bool,
	/**
	 * Function to return custom cell size in format func(item)
	 * @type {object} Object containing the size in several bootstrap sizes
	 */
	onCellSize: React.PropTypes.func
};

GridTable.defaultProps = {
	cellSize: { sm: 6 },
	singleColumn: false
};

