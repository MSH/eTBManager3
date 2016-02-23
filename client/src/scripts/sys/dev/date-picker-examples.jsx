
import React from 'react';
import { Row, Col } from 'react-bootstrap';
import DayPicker, { DateUtils } from 'react-day-picker';
import { Card, Fa, Popup, DatePicker } from '../../components/index';


/**
 * Initial page that declare all routes of the module
 */
export default class DatePickerExamples extends React.Component {

	constructor(props) {
		super(props);
		this.onDayClick = this.onDayClick.bind(this);
		this.dayPickerSelect = this.dayPickerSelect.bind(this);

		this.state = { };
	}

	onDayClick(e, day) {
		this.setState({ day: day });
	}


	dayPickerSelect(e, day) {
		this.setState({ dayPicker: day.toString() });
	}


	render() {
		const selday = this.state.day;

		const modifiers = {
			selected: day => selday ? DateUtils.isSameDay(day, selday) : false
		};

		return (
			<div>
				<Card title="Date picker">
					<Row>
						<Col md={5}>
							<DatePicker label="Start Date:" />
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
