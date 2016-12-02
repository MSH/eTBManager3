import React from 'react';
import moment from 'moment';
import YearPicker from './year-picker';
import SelectionBox from './selection-box';

/**
 * Display a bootstrap form control for selection of a month and year
 */
export default class MonthYearPicker extends React.Component {

    constructor(props) {
        super(props);

        this.change = this.change.bind(this);
    }

    change(prop) {
        return val => {
            if (this.props.onChange) {
                const data = Object.assign({}, this.props.value);
                data[prop] = val;
                this.props.onChange(data);
            }
        };
    }

    focus() {
        const refs = this.refs;
        const ctrl = this.props.period ? refs.iniMonth : refs.month;
        ctrl.getDOMNode().focus();
    }

    /**
     * Create the month selector with the list of month names
     */
    monthSelector(props) {
        // create the options
        const options = [];
        let count = 0;
        while (count < 12) {
            options.push({
                id: count + 1,
                name: moment().month(count++).format('MMM')
            });
        }

        const selval = props.value ?
            options.find(it => it.id === props.value) :
            null;

        const monthChange = val => {
            if (props.onChange) {
                props.onChange(val !== null ? val.id : null);
            }
        };

        const compProps = Object.assign({}, props, { onChange: monthChange, value: selval });

        return (
            <SelectionBox ref={props.ref} options={options}
                optionDisplay="name"
                noSelectionLabel="-"
                {...compProps}
            />
        );
    }

    monthYearCtrl(opts) {
        const val = this.props.value ? this.props.value : {};

        return (
            <div>
                {opts.label ? <label className="pull-left label-left-ctrl">{opts.label}</label> : null}
                <div className="pull-left" style={{ minWidth: '100px' }}>
                {
                    this.monthSelector({
                        ref: opts.pmonth,
                        placeHolder: __('datetime.month'),
                        value: val[opts.pmonth],
                        onChange: this.change(opts.pmonth)
                    })
                }
                </div>
                <div className="pull-left" style={{ minWidth: '90px' }}>
                    <YearPicker onChange={this.change(opts.pyear)}
                        value={val[opts.pyear]} />
                </div>
            </div>
        );
    }

    render() {
        const props = this.props;

        const label = props.label ? <label className="control-label">{props.label}</label> : null;

        const period = props.period;

        return (
            <div className="form-group">
                {label}
                {!period && this.monthYearCtrl({
                    pmonth: 'month',
                    pyear: 'year'
                })}
                {period && this.monthYearCtrl({
                    pmonth: 'iniMonth',
                    pyear: 'iniYear'
                })}
                {period && this.monthYearCtrl({
                    label: __('period.to') + ':',
                    pmonth: 'endMonth',
                    pyear: 'endYear'
                })}
            </div>
        );
    }
}

MonthYearPicker.propTypes = {
    value: React.PropTypes.object,
    onChange: React.PropTypes.func,
    label: React.PropTypes.node,
    bsStyle: React.PropTypes.oneOf(['success', 'warning', 'error']),
    help: React.PropTypes.string,
    wrapperClassName: React.PropTypes.string,
    // if true, an initial and final month/year controls will be displayed to set a period
    period: React.PropTypes.bool
};
