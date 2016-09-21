
import React from 'react';
import { Row, Col } from 'react-bootstrap';
import DayPicker, { DateUtils } from 'react-day-picker';
import { Card, DatePicker } from '../../components/index';


/**
 * Initial page that declare all routes of the module
 */
export default class DatePickerExamples extends React.Component {

    constructor(props) {
        super(props);
        this.onDayClick = this.onDayClick.bind(this);
        this.datePickerChange = this.datePickerChange.bind(this);

        this.state = { day: new Date(), dayPicker: new Date() };
    }

    onDayClick(e, day) {
        this.setState({ day: day });
    }


    datePickerChange(e, day) {
        this.setState({ dayPicker: day });
    }


    render() {
        const selday = this.state.day;

        // display the selected day
        const modifiers = {
            selected: day => selday ? DateUtils.isSameDay(day, selday) : false
        };

        return (
            <div>
                <Card title="Date picker">
                    <Row>
                        <Col md={5}>
                            <DatePicker label="Start Date:" value={this.state.dayPicker}
                                onChange={this.datePickerChange} />
                        </Col>
                        <Col md={3}>
                            {this.state.dayPicker.toString()}
                        </Col>
                    </Row>
                </Card>
                <Card title="Calendar">
                    <Row>
                        <Col mdOffset={3} md={6}>
                            <DayPicker modifiers={modifiers}
                                onDayClick={this.onDayClick} />
                        </Col>
                    </Row>
                </Card>
            </div>
            );
    }
}
