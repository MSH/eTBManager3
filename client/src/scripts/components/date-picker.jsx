import React from 'react';
import DayPicker, { DateUtils } from 'react-day-picker';
import Popup from './popup';
import Fa from './fa';


/**
 * React component that displays an input box for a date picker component
 */
export default class DatePicker extends React.Component {

	constructor(props) {
		super(props);
		this.dayClick = this.dayClick.bind(this);
		this.buttonClick = this.buttonClick.bind(this);
		this.preventClose = this.preventClose.bind(this);
		this.valueChange = this.valueChange.bind(this);

		this.state = { };
	}

	/**
	 * Called when user clicks on the drop down calendar
	 * @param  {[type]} evt [description]
	 * @param  {[type]} day [description]
	 * @return {[type]}     [description]
	 */
	dayClick(evt, day) {
		if (this.props.onChange) {
			this.props.onChange(evt, day);
		}

		this.refs.textfield.value = this.dateToStr(day);
	}

	/**
	 * Convert a date to a string for displaying
	 * @param  {[type]} day [description]
	 * @return {[type]}     [description]
	 */
	dateToStr(day) {
		var date = day.getDate().toString();
		if (date.length === 1) {
			date = '0' + date;
		}

		var month = (day.getMonth() + 1).toString();
		if (month.length === 1) {
			month = '0' + month;
		}

		return date + '/' + month + '/' + day.getFullYear();
	}

	/**
	 * Convert a string to a date object
	 * @param  {[type]} s [description]
	 * @return {[type]}   [description]
	 */
	strToDate(s) {
		const vals = s.split('/');
		if (vals.length !== 3) {
			return null;
		}

		const year = Number(vals[2]);
		if (year < 1000 || year > 3000) {
			return null;
		}

		const dt = new Date(year, Number(vals[1]) - 1, Number(vals[0]));
		if (isNaN(dt.getTime())) {
			return null;
		}
		return !dt ? null : dt;
	}

	/**
	 * Called when user clicks on the calendar button to display the drop down calendar
	 * @return {[type]} [description]
	 */
	buttonClick() {
		this.refs.popup.show();
	}

	/**
	 * Called to prevent that the calendar is closed when changing months
	 * @return {[type]} [description]
	 */
	preventClose() {
		this.refs.popup.preventHide();
	}

	/**
	 * Called when user changes the text in the input box
	 * @return {[type]} [description]
	 */
	valueChange() {
		const s = this.refs.textfield.value;
		if (!s) {
			this.dayClick(null, dt);
			return;
		}

		const dt = this.strToDate(s);
		if (dt) {
			this.dayClick(null, dt);
		}
	}

	render() {

		// label render
		const label = this.props.label ?
			<label className="control-label">{this.props.label}</label> :
			null;

		const dt = this.props.value;
		const value = dt ? this.dateToStr(dt) : '';

		// display the selected day
		const modifiers = {
			selected: day => dt ? DateUtils.isSameDay(day, dt) : false
		};

		const help = this.props.help;

		const compClass = 'form-group' + (this.props.bsStyle ? ' has-' + this.props.bsStyle : '');

		return (
			<div className={compClass}>
				<div>
					{label}
					<div className="input-btn-group">
						<input type="text" ref="textfield"
							className="form-control"
							placeholder="dd/mm/yyyy"
							defaultValue={value} onChange={this.valueChange}/>
						<a className="btn-group" onClick={this.buttonClick} >
							<Fa icon="calendar"/>
						</a>
					</div>
					<Popup ref="popup">
						<DayPicker onDayClick={this.dayClick}
							modifiers={modifiers}
							onMonthChange={this.preventClose} />
					</Popup>
				</div>
				{
					help ? <div className="help-block">{help}</div> : null
				}
			</div>
			);
	}
}


DatePicker.propTypes = {
	label: React.PropTypes.node,
	onChange: React.PropTypes.func,
	value: React.PropTypes.any,
	bsStyle: React.PropTypes.oneOf(['success', 'warning', 'error']),
	help: React.PropTypes.string,
	wrapperClassName: React.PropTypes.string
};

DatePicker.defaultProps = {
	mode: 'single'
};
