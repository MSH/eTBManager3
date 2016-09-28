import React from 'react';
import { Card, AsyncButton } from '../../../../components';
import { isPromise } from '../../../../commons/utils';
import FiltersSelector from './filters-selector';

import './filters.less';

/**
 * Display a card for filter selection
 */
export default class FilterCard extends React.Component {

    constructor(props) {
        super(props);
        this._submitFilters = this._submitFilters.bind(this);
        this._filterValuesChanged = this._filterValuesChanged.bind(this);
        this.state = { };
    }


    _filterValuesChanged(filterValues) {
        this.setState({ filterValues: filterValues });
    }


    /**
     * Called when user clicks on the submit button to execute the filters
     */
    _submitFilters() {
        const res = this.props.onSubmit(this.state.filterValues);
        // check if it is a promise
        if (!isPromise(res)) {
            return;
        }

        this.setState({ fetching: true });

        const self = this;
        res.then(() => self.setState({ fetching: false }))
            .catch(() => self.setState({ fetching: false }));
    }


    render() {
        const onClose = this.props.onClose;

        const submitBtn = (
            <AsyncButton className="pull-right"
                onClick={this._submitFilters}
                bsStyle="success"
                fetching={this.state.fetching}>
                {this.props.btnLabel}
            </AsyncButton>
        );

        return (
            <Card title={this.props.title} closeBtn={!!onClose} onClose={onClose}>
                <FiltersSelector
                    footer={submitBtn}
                    filters={this.props.filters}
                    filterValues={this.state.filterValues}
                    onChange={this._filterValuesChanged}
                    showPopup
                    />
            </Card>
        );
    }
}

FilterCard.propTypes = {
    title: React.PropTypes.string.isRequired,
    filters: React.PropTypes.array,
    btnLabel: React.PropTypes.node,
    onClose: React.PropTypes.func,
    onSubmit: React.PropTypes.func
};

