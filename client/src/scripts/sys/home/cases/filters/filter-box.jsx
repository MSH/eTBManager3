import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { LinkTooltip } from '../../../../components';
import SelectFilter from './select-filter';


export default class FilterBox extends React.Component {

	constructor(props) {
		super(props);
		this._onChange = this._onChange.bind(this);
	}


	filterComponent(type) {
		switch (type) {
			case 'select':
			case 'multi-select': return SelectFilter;
			default: return null;
		}
	}

	_onChange(val) {
		if (this.props.onChange) {
			this.props.onChange(this.props.filter, val);
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
					<FilterComponent filter={filter}
						value={this.props.value}
						onChange={this._onChange} />
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
