import React from 'react';

/**
 * Represent a row of the ReactTable
 */
export default class ReactRow extends React.Component {

	constructor(props) {
		super(props);

		this._rowClick = this._rowClick.bind(this);
		this.state = {};
	}

	/**
	 * Called when user clicks on the row of the table
	 * @param  {[type]} evt [description]
	 * @return {[type]}     [description]
	 */
	_rowClick(evt) {
		if (evt.isDefaultPrevented()) {
			return;
		}

		if (this.props.onClick) {
			this.props.onClick(this.props.value, this);
		}
	}

	render() {
		return (
			<div onClick={this._rowClick}>
			{
				this.props.onRender(this.props.value, this)
			}
			</div>
		);
	}
}

ReactRow.propTypes = {
	value: React.PropTypes.any.isRequired,
	onRender: React.PropTypes.func.isRequired,
	onClick: React.PropTypes.func,
	index: React.PropTypes.number.isRequired
};
