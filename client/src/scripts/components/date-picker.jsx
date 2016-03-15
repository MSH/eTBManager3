import React from 'react';
import DayPicker from 'react-day-picker';
import Popup from './popup';
import Fa from './fa';

export default class DatePicker extends React.Component {

	constructor(props) {
		super(props);
		this.dayClick = this.dayClick.bind(this);
		this.buttonClick = this.buttonClick.bind(this);
		this.preventClose = this.preventClose.bind(this);

		this.state = { };
	}

	dayClick(evt, day) {
		if (this.props.onChange) {
			this.props.onChange(evt, day);
		}

		this.refs.textfield.value = this.formatDay(day);
	}

	formatDay(day) {
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


	buttonClick() {
		this.refs.popup.show();
	}

	preventClose() {
		this.refs.popup.preventHide();
	}


	render() {

		// label render
		const label = this.props.label ?
			<label className="control-label">{this.props.label}</label> :
			null;

		return (
			<div>
				{label}
				<input type="text" ref="textfield" className="form-control input-right-button" placeholder="dd/mm/yyyy" />
				<a className="form-control-action" onClick={this.buttonClick} >
					<Fa icon="calendar"/>
				</a>
				<Popup ref="popup">
					<DayPicker onDayClick={this.dayClick}
						onMonthChange={this.preventClose} />
				</Popup>
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
