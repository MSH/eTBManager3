import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import ReactRow from './react-row';
import Expandable from './expandable';
import { Size } from '../commons/grid-utils';


export default class ReactGrid extends React.Component {

	constructor(props) {
		super(props);
		this.cellRender = this.cellRender.bind(this);
	}

	gridRender() {
		const vals = this.props.values;

		const rows = [];
		const onCellSize = this.props.onCellSize;

		let size = new Size();

		// cell render
		let cells = [];
		vals.forEach((item, index) => {
			// get the cell size (width)
			const cellSize = onCellSize ? onCellSize(item) : this.props.cellSize;

			// rend cell
			const cell = (
				<Col {...cellSize} key={index}>
					<ReactRow value={item} onRender={this.cellRender} index={index} />
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
		});

		return rows;
	}

	cellRender(item, row) {
		// check if there is a row render defined
		if (this.props.onCellRender) {
			const content = this.props.onCellRender(item, row);
			// render returned any content ?
			if (content) {
				return content;
			}
		}

		const collapsed = this.props.onCollapseRender ? this.props.onCollapseRender(item, row) : null;

		const clickable = !!this.props.onCollapseRender;

		return (
			<div className="card card-small tbl-row">
			{
				clickable ?
				<Expandable ref="exp" onExpandRender={this.props.onExpandRender} value={item} className="card-content">
					{collapsed}
				</Expandable> :
				collapsed
			}
			</div>
			);
	}

	render() {
		const className = this.props.onCollapseRender ? 'tbl-hover' : null;

		return (
			<Grid fluid className={className}>
				{this.gridRender()}
			</Grid>
			);
	}
}


ReactGrid.propTypes = {
	onCellRender: React.PropTypes.func,
	onCollapseRender: React.PropTypes.func,
	onExpandRender: React.PropTypes.func,
	cellSize: React.PropTypes.object,
	values: React.PropTypes.array,
	/**
	 * Function to return custom cell size in format func(item)
	 * @type {object} Object containing the size in several bootstrap sizes
	 */
	onCellSize: React.PropTypes.func
};

ReactGrid.defaultProps = {
	cellSize: { sm: 6 }
};
