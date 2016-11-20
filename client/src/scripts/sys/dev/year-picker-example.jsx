
import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { Card, YearPicker, MonthYearPicker } from '../../components/index';

/**
 * Example of how to use the year picker and month-year picker control
 */
export default class ReacttableExample extends React.Component {

    constructor(props) {
        super(props);

        this.yearChange = this.yearChange.bind(this);
        this.yearChange2 = this.yearChange2.bind(this);
        this.monthYearChange = this.monthYearChange.bind(this);

        this.state = { };
    }

    yearChange(val) {
        this.setState({ year: val });
    }

    yearChange2(val) {
        this.setState({ endYear: val });
    }

    monthYearChange(val) {
        this.setSate({ monthYear: val });
    }

    render() {

        return (
            <div>
                <Card title="Year picker">
                    <Row>
                        <Col md={6}>
                            <YearPicker value={this.state.year}
                                label="Initial year:"
                                onChange={this.yearChange} />
                        </Col>
                        <Col md={6}>
                            <YearPicker value={this.state.endYear}
                                label="Final year:"
                                bsStyle="error"
                                help="Enter the final year"
                                onChange={this.yearChange2} />
                        </Col>
                    </Row>
                    <Row>
                        <Col md={4} mdOffset={4}>
                            <YearPicker value={this.state.year}
                                noPopup
                                onChange={this.yearChange} />
                        </Col>
                    </Row>
                </Card>
                <Card title="Month year picker">
                    <Row>
                        <Col md={12}>
                            <MonthYearPicker value={this.state.monthYear}
                                label="Please select the month and year:"
                                onChange={this.monthYearChange} />
                        </Col>
                    </Row>
                </Card>
            </div>
            );
    }
}
