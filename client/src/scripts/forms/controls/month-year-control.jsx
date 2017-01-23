import React from 'react';
import FormUtils from '../form-utils';
import MonthYearPicker from '../../components/month-year-picker';
import moment from 'moment';


/**
 * Form control to support the selection of a period of initial and final month/year
 */
export default class PeriodControl extends React.Component {

    static typeName() {
        return 'month-year';
    }

    constructor(props) {
        super(props);
        this._change = this._change.bind(this);
    }

    displayText(period) {
        if (!period || (!period.month && !period.year)) {
            return '';
        }

        const s = (period.month ? moment().month(period.month - 1).format('MMM') + '-' : '') + period.year;
        return s;
    }

    _change(value) {
        this.props.onChange({ schema: this.props.schema, value: value });
    }

    focus() {
        this.refs.picker.focus();
        return true;
    }

    render() {
        const sc = this.props.schema;

        if (sc.readOnly) {
            return FormUtils.readOnlyRender(this.displayText(this.props.value), sc.label);
        }

        const label = FormUtils.labelRender(sc.label, sc.required);

        return (
            <MonthYearPicker
                ref="picker"
                label={label}
                value={this.props.value}
                onChange={this._change}
                errors={this.props.errors}
                />
        );
    }
}


PeriodControl.propTypes = {
    value: React.PropTypes.any,
    schema: React.PropTypes.object,
    onChange: React.PropTypes.func,
    errors: React.PropTypes.any,
    resources: React.PropTypes.any
};
