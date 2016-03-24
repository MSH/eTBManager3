import React from 'react';

/**
 * Represent a row of the ReactTable
 */
export default class ReactRow extends React.Component {

	constructor(props) {
		super(props);

		this._rowClick = this._rowClick.bind(this);
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
			<div onClick={this._rowClick} className={this.props.className}>
			{
				this.props.onRender(this.props.value)
			}
			</div>
			);
	}
}

ReactRow.propTypes = {
	value: React.PropTypes.any,
	onRender: React.PropTypes.func,
	onClick: React.PropTypes.func,
	className: React.PropTypes.string
};
