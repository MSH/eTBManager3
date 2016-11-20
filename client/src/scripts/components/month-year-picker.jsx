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

        this.monthChange = this.monthChange.bind(this);
        this.yearChange = this.yearChange.bind(this);

        this.state = { };
    }

    monthChange(val) {
        this.setState({ month: val });
    }

    yearChange(val) {
        this.setState({ year: val });
    }

    render() {
        const props = this.props;

        const label = props.label ? <label className="control-label">{props.label}</label> : null;

        const options = [];
        let count = 0;
        while (count < 12) {
            options.push(moment().month(count++).format('MMM'));
        }

        return (
            <div className="form-group">
                {label}
                <div>
                <div className="pull-left" style={{ minWidth: '100px' }}>
                    <SelectionBox options={options}
                        placeHolder={__('datetime.month')}
                        noSelectionLabel="-"
                        onChange={this.monthChange}
                        value={this.state.month} />
                </div>
                <div className="pull-left" style={{ minWidth: '90px' }}>
                    <YearPicker onChange={this.yearChange}
                        value={this.state.year} />
                </div>
                </div>
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
    wrapperClassName: React.PropTypes.string
};
