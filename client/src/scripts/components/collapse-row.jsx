
import React from 'react';
import { Collapse, Row } from 'react-bootstrap';

/**
 * A simple row component with support for additional content that is
 * expanded or collapsed on users click
 */
export default class CollapseRow extends React.Component {

	constructor(props) {
		super(props);
		this.onClick = this.onClick.bind(this);
		this.state = { collapse: this.props.collapse };
	}

	/**
	 * Called when user clicks on the row
	 * @param  {[type]} evt [description]
	 * @return {[type]}     [description]
	 */
	onClick(evt) {
		if (!evt.isPropagationStopped()) {
			this.setState({ collapse: !this.state.collapse });
		}
	}

	render() {
		const props = Object.assign({}, this.props);
		delete props.collapsable;

		return (
			<Row {...props} onClick={this.onClick} className={'collapse-row ' + props.className}>
					{this.props.children}
					<Collapse in={!this.state.collapse}>
						{this.props.collapsable}
					</Collapse>
			</Row>
			);
	}
}


CollapseRow.propTypes = {
    className: React.PropTypes.string,
    children: React.PropTypes.any,
    style: React.PropTypes.object,
    onClick: React.PropTypes.func,
    collapsable: React.PropTypes.any,
    collapse: React.PropTypes.bool
};

CollapseRow.defaultProps = {
	collapse: true
};
