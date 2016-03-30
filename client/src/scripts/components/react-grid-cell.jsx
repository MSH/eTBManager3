import React from 'react';
import { Col } from 'react-bootstrap';

/**
 * Represent a row of the ReactTable
 */
export default class ReactGridCell extends React.Component {

	constructor(props) {
		super(props);

		this._cellClick = this._cellClick.bind(this);
		this.state = {};
	}

	/**
	 * Called when user clicks on the row of the table
	 * @param  {[type]} evt [description]
	 * @return {[type]}     [description]
	 */
	_cellClick(evt) {
		if (evt.isDefaultPrevented()) {
			return;
		}

		if (this.props.onClick) {
			this.props.onClick(this.props.value, this);
		}
	}

	setSize(size) {
		this.setState({ size: size });
	}

	getSize() {
		return this.state.size;
	}

	render() {
		const size = this.state.size ? this.state.size : this.props.initialSize;

		return (
			<Col {...size} onClick={this._cellClick} className="tx-width">
			{
				this.props.onRender(this.props.value, this)
			}
			</Col>
		);
	}
}

ReactGridCell.propTypes = {
	value: React.PropTypes.any.isRequired,
	initialSize: React.PropTypes.object.isRequired,
	onRender: React.PropTypes.func.isRequired,
	onClick: React.PropTypes.func,
	index: React.PropTypes.number.isRequired
};
