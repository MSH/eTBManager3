
import React from 'react';
import FormUtils from '../form-utils';
import { format } from '../../commons/utils';
import MonthYearPicker from '../../components/month-year-picker';
import moment from 'moment';


/**
 * Form control to support the selection of a period of initial and final month/year
 */
export default class PeriodControl extends React.Component {

    static typeName() {
        return 'period';
    }

    constructor(props) {
        super(props);
        this._change = this._change.bind(this);
    }

    displayText(period) {
        const ini = moment(period.ini).format('L');
        const end = moment(period.end).format('L');
        return format(__('period.display'), ini, end);
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
                period />
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
