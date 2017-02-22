import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { LinkTooltip } from '../../../components';
import FilterFactory from './filter-factory';


/**
 * Display a box with a single filter to enter its value in there
 */
export default class FilterBox extends React.Component {

    constructor(props) {
        super(props);
        this._onChange = this._onChange.bind(this);
        this._onRemove = this._onRemove.bind(this);
    }

    _onChange(val) {
        if (this.props.onChange) {
            this.props.onChange(this.props.filter, val);
        }
    }

    _onRemove() {
        this.props.onRemove(this.props.filter);
    }

    render() {
        const filter = this.props.filter;

        const FilterComponent = FilterFactory.create(filter);

        if (__DEV__) {
            if (!FilterComponent) {
                throw new Error('Invalid filter type: ' + filter.type);
            }
        }

        return (
            <Row>
                <Col sm={4} className="filter">
                    <div className="form-group">
                        <label className="control-label">{filter.name + ':'}</label>
                    </div>
                    <LinkTooltip
                        toolTip={__('form.filters.remove')}
                        onClick={this._onRemove}
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
    onRemove: React.PropTypes.func.isRequired,
    onChange: React.PropTypes.func.isRequired
};
