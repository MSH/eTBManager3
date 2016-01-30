
import React from 'react';

export default class SelectionBox extends React.Component {

	labelRender() {
		const label = this.props.label;

		return label ? <div className="control-label">{label}</div> : null;
	}

	render() {
		return (
			<div className="form-group">
				{this.labelRender()}
				<div className="form-control">
					{'This is a simple test'}
				</div>
			</div>
			);
	}
}

SelectionBox.propTypes = {
	label: React.PropTypes.string
};
