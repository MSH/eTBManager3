
import React from 'react';
import { Row, Col } from 'react-bootstrap';
import DayPicker, { DateUtils } from 'react-day-picker';
import { Card } from '../../components/index';


/**
 * Initial page that declare all routes of the module
 */
export default class DatePickerExamples extends React.Component {

	constructor(props) {
		super(props);
		this.onDayClick = this.onDayClick.bind(this);

		this.state = { };
	}

	onDayClick(e, day) {
		this.setState({ day: day });
	}

	render() {

		const selday = this.state.day;

		const modifiers = {
			selected: day => selday ? DateUtils.isSameDay(day, selday) : false
		};

		return (
			<Card title="Calendar">
				<Row>
					<Col mdOffset={3} md={6}>
						<DayPicker modifiers={modifiers} onDayClick={this.onDayClick} />
					</Col>
				</Row>
			</Card>
			);
	}
}
