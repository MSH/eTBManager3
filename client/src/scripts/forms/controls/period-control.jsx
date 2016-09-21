
import React from 'react';
import FormUtils from '../form-utils';
import { format } from '../../commons/utils';
import moment from 'moment';


/**
 * Control to support date types
 */
export default class PeriodControl extends React.Component {

    static typeName() {
        return 'period';
    }

    displayText(period) {
        const ini = moment(period.ini).format('L');
        const end = moment(period.end).format('L');
        return format(__('period.display'), ini, end);
    }

    render() {
        const sc = this.props.schema;

        if (sc.readOnly) {
            return FormUtils.readOnlyRender(this.displayText(this.props.value), sc.label);
        }

        if (__DEV__) {
            throw new Error('PeriodControl: Not implemented for editing');
        }

        return null;
    }
}


PeriodControl.propTypes = {
    value: React.PropTypes.any,
    schema: React.PropTypes.object,
    onChange: React.PropTypes.func,
    errors: React.PropTypes.any,
    resources: React.PropTypes.any
};
