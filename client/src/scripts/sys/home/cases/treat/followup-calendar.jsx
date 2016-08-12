import React from 'react';
import DayPicker from 'react-day-picker';


/**
 * Display the treatment followup card
 */
export default class FollowupCalendar extends React.Component {

	constructor(props) {
		super(props);
		this._calendarClick = this._calendarClick.bind(this);

		this.state = { selBtn: 'DOTS' };
	}

	shouldComponentUpdate(nextProps) {
		return this.props.data !== nextProps.data;
	}

	_calendarClick() {
		if (this.props.onClick) {
			this.props.onClick(this.props.data);
		}
	}

	/**
	 * Return the status of the given by according to the data being edited
	 * @param  {[type]} data [description]
	 * @param  {[type]} dt   [description]
	 * @return {[type]}      [description]
	 */
	_dayStatus(dt) {
		const days = this.props.data.days;
		if (!days) {
			return null;
		}

		const day = dt.getDate();

		const item = days.find(it => it.day === day);

		return item ? item.status : null;
	}

	/**
	 * Render the day in the calendar to easy styling
	 * @param  {[type]} dt [description]
	 * @return {[type]}    [description]
	 */
	_renderDay(dt) {
		const day = dt.getDate();

		return <div className="day">{day}</div>;
	}


	render() {
		const data = this.props.data;
		const year = data.year;
		const month = data.month;
		const day = data.iniDay ? data.iniDay : 1;
		const inidt = new Date(year, month, day, 0, 0);

		const modifiers = {
				dots: dt => this._dayStatus(dt) === 'DOTS',
				selfadmin: dt => this._dayStatus(dt) === 'SELF_ADMIN',
				nottaken: dt => this._dayStatus(dt) === 'NOT_TAKEN'
		};

		return (
			<div className={this.props.className} onClick={this._calendarClick}>
				<DayPicker canChangeMonth={false}
					initialMonth={inidt}
					modifiers={modifiers}
					renderDay={this._renderDay}
					onDayClick={this.props.onDayClick}
				/>
				{this.props.children}
			</div>
			);
	}
}

FollowupCalendar.propTypes = {
	data: React.PropTypes.object.isRequired,
	onClick: React.PropTypes.func,
	onDayClick: React.PropTypes.func,
	className: React.PropTypes.string,
	children: React.PropTypes.any
};
