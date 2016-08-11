import React from 'react';
import { Button, ButtonGroup } from 'react-bootstrap';
import { Card, Fa } from '../../../../components';
import FollowupCalendar from './followup-calendar';

import './treat-followup.less';

/**
 * Display the treatment followup card
 */
export default class TreatFollowup extends React.Component {

	constructor(props) {
		super(props);
		this._calendarClick = this._calendarClick.bind(this);
		this.state = { selBtn: 'DOTS' };
	}

	_buttonClick(key) {
		return () => {
			this.setState({ selBtn: key });
		};
	}

	renderCalendars() {
		const lst = this.props.treatment.followup;
		if (!lst) {
			return null;
		}

		const res = [];
		lst.forEach(item => {
			const key = (item.year * 100) + item.month;
			res.push(
				<FollowupCalendar key={key} data={item} onClick={this._calendarClick}/>
				);
		});

		return <div className="treat-table">{res}</div>;
	}

	_calendarClick(date, data) {
		// get the selected day
		const day = date.getDate();
		const lst = data.days ? data.days : [];

		// update day status
		let item = lst.find(it => it.day === day);
		const status = this.state.selBtn;
		if (item) {
			if (item.status === status) {
				const index = lst.indexOf(item);
				lst.splice(index, 1);
			} else {
				item.status = status;
			}
		} else {
			item = { day: day, status: status };
			lst.push(item);
		}

		// create new treatment monitoring obj for the month/year
		const newdata = Object.assign({}, data, { days: lst });

		const followup = this.props.treatment.followup;

		const i = followup.indexOf(data);
		// replace item in the list
		followup[i] = newdata;

		this.forceUpdate();
	}

	renderLegend() {
		const active = this.state.selBtn;

		return (
			<div className="treat-legend">
				<ButtonGroup bsSize="small">
					<Button active={active === 'DOTS'} onClick={this._buttonClick('DOTS')}>
						<Fa icon="circle" className="text-success"/>{'DOT'}
					</Button>
					<Button active={active === 'SELF_ADMIN'} onClick={this._buttonClick('SELF_ADMIN')}>
						<Fa icon="circle" className="text-primary"/>{'Self administered'}
					</Button>
					<Button active={active === 'NOT_TAKEN'} onClick={this._buttonClick('NOT_TAKEN')}>
						<Fa icon="circle" className="text-muted"/>{'Not taken'}
					</Button>
				</ButtonGroup>
			</div>
			);
		// return (
		// 		<div className="treat-legend">
		// 			<Fa icon="circle" className="text-success"/>{'DOT  '}
		// 			<Fa icon="circle" className="text-primary"/>{'Self administered  '}
		// 			<Fa icon="circle" className="text-muted"/>{'Not taken'}
		// 		</div>
		// 	);
	}

	render() {
		return (
			<Card title={__('cases.details.treatment.medintake')} >
			<div className="treat-followup">
			{
				this.renderLegend()
			}
			{
				this.renderCalendars()
			}
			</div>
			</Card>
			);
	}
}

TreatFollowup.propTypes = {
	treatment: React.PropTypes.object
};
