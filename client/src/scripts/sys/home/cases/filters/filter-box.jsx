import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { LinkTooltip } from '../../../../components';
import SelectFilter from './select-filter';


export default class FilterBox extends React.Component {

	filterComponent(type) {
		switch (type) {
			case 'select': return SelectFilter;
			default: return null;
		}
	}

	render() {
		const filter = this.props.filter;

		const FilterComponent = this.filterComponent(filter.type);

		if (__DEV__) {
			if (!FilterComponent) {
				throw new Error('Invalid filter type ' + filter.type);
			}
		}

		return (
			<Row>
				<Col sm={4} className="filter">
					<div className="form-group">
						<label className="control-label">{filter.label + ':'}</label>
					</div>
					<LinkTooltip
						toolTip="Remove filter"
						onClick={this.props.onRemove}
						icon="minus"
						className="remove-link" />
				</Col>
				<Col sm={8}>
					<FilterComponent filter={filter} value={this.props.value} />
				</Col>
			</Row>
		);
	}
}

FilterBox.propTypes = {
	filter: React.PropTypes.object.isRequired,
	value: React.PropTypes.any,
	onRemove: React.PropTypes.func,
	onChange: React.PropTypes.func
};
