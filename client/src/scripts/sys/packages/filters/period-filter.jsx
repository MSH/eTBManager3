import React from 'react';
import { MonthYearPicker } from '../../../components';
import { objEqual, isEmpty } from '../../../commons/utils';


/**
 * A filter component for selection of a period
 */
export default class PeriodFilter extends React.Component {

    constructor(props) {
        super(props);
        this._change = this._change.bind(this);
    }

    _change(val) {
        const period = Object.assign({ }, val, { type: 'DATE' });

        if (this.props.onChange) {
            this.props.onChange(period);
        }
    }

    render() {
        return (
            <MonthYearPicker
                value={this.props.value}
                period
                onChange={this._change} />
        );
    }
}

PeriodFilter.propTypes = {
    filter: React.PropTypes.object,
    value: React.PropTypes.any,
    onChange: React.PropTypes.func
};
