import React from 'react';
import { Row } from 'react-bootstrap';
import ReactGridCell from './react-grid-cell';
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
				<ReactGridCell key={index} initialSize={cellSize}
					value={item}
					onRender={this.cellRender}
					index={index} />
			);

			cells.push(cell);

			size.add(cellSize);

			// check if next cell fits in ermaining size or if it is the last item
			if (!size.fitInSize(cellSize) || index === vals.length - 1) {
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

	cellRender(item, cell) {
		// check if there is a row render defined
		if (this.props.onCellRender) {
			const content = this.props.onCellRender(item, cell);
			// render returned any content ?
			if (content) {
				return content;
			}
		}

		const collapsed = this.props.onCollapseRender ? this.props.onCollapseRender(item, cell) : null;

		const clickable = !!this.props.onExpandRender;

		const self = this;
		const expandRender = it => {
			if (self.props.onExpandRender) {
				return self.props.onExpandRender(it, cell);
			}
			return null;
		};

		return (
			<div className="card card-small tbl-row">
			{
				clickable ?
				<Expandable ref="exp" onExpandRender={expandRender} value={item} className="card-content">
					{collapsed}
				</Expandable> :
				collapsed
			}
			</div>
			);
	}

	render() {
		const className = (this.props.onCollapseRender ? 'tbl-hover' : null);

		return (
			<div className={className} style={{ clear: 'both' }}>
				{this.gridRender()}
			</div>
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
