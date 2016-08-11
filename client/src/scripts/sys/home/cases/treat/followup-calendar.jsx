import React from 'react';
import DayPicker, { DateUtils } from 'react-day-picker';


/**
 * Display the treatment followup card
 */
export default class FollowupCalendar extends React.Component {

	constructor(props) {
		super(props);
		this._handleDayClick = this._handleDayClick.bind(this);
	}

	shouldComponentUpdate(nextProps) {
		return this.props.data !== nextProps.data;
	}

	_handleDayClick(e, day) {
		this.props.onClick(day, this.props.data);
	}

	_dayStatus(dt) {
		const days = this.props.data.days;
		if (!days) {
			return null;
		}

		const day = dt.getDate();

		const item = days.find(it => it.day === day);
		return item ? item.status : null;
	}

	_renderDay(dt) {
		const day = dt.getDate();

		return <div className="day">{day}</div>;
	}

	render() {
		const data = this.props.data;
		const year = data.year;
		const month = data.month;
		const day = data.iniDay ? data.iniDay : 1;
		const dt = new Date(year, month, day, 0, 0);

		const modifiers = {
			dots: date => this._dayStatus(date) === 'DOTS',
			selfadmin: date => this._dayStatus(date) === 'SELF_ADMIN',
			nottaken: date => this._dayStatus(date) === 'NOT_TAKEN'
		};

		return (
			<div className="treat-cal">
				<DayPicker canChangeMonth={false}
					initialMonth={dt}
					modifiers={modifiers}
					renderDay={this._renderDay}
					onDayClick={this._handleDayClick} />
			</div>
			);
	}
}

FollowupCalendar.propTypes = {
	data: React.PropTypes.object.isRequired,
	onClick: React.PropTypes.func.isRequired
};
