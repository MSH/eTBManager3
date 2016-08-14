import React from 'react';
import { Card, Fa } from '../../../../components';
import { format } from '../../../../commons/utils';
import FollowupCalendar from './followup-calendar';
import CalendEditor from './calend-editor';

import './treat-followup.less';

/**
 * Display the treatment followup card
 */
export default class TreatFollowup extends React.Component {

	constructor(props) {
		super(props);
		this._calendarClick = this._calendarClick.bind(this);
		this.closeEditor = this.closeEditor.bind(this);

		this.state = { selBtn: 'DOTS' };
	}

	_buttonClick(key) {
		return () => {
			this.setState({ selBtn: key });
		};
	}

	_calendarClick(data) {
		// calendar is being edited already
		if (this.state.editor) {
			return;
		}

		// create a copy of the data
		this.setState({ editor: data });
	}

	closeEditor(data) {
		if (data) {
			this.saveData(data);
			const lst = this.props.treatment.followup;
			const index = lst.indexOf(this.state.editor);
			lst[index] = data;
		}

		this.setState({ editor: null });
	}

	saveData(data) {
		console.log('save = ', data);
		// THIS FUNCTION MUST POST THE DATA TO THE SERVER
	}

	renderLegend() {
		return (
				<div className="treat-legend">
					<Fa icon="circle" className="treat-DOTS"/>{__('TreatmentDayOption.DOTS')}
					<Fa icon="circle" className="treat-SELF_ADMIN"/>{__('TreatmentDayOption.SELF_ADMIN')}
					<Fa icon="circle" className="treat-NOT_TAKEN"/>{__('TreatmentDayOption.NOT_TAKEN')}
				</div>
			);
	}


	renderCalendars() {
		const lst = this.props.treatment.followup;
		if (!lst) {
			return null;
		}

		const editor = this.state.editor;

		const res = [];
		lst.forEach(item => {
			const key = (item.year * 100) + item.month;

			// count the number of days
			let daysCount = 0;
			if (item.days) {
				item.days.forEach(day => {
					if (day.status !== 'NOT_TAKEN') {
						daysCount++;
					}
				});
			}

			const txt = __('cases.treat.disp') + ': ' + format(__('cases.treat.days'), daysCount, item.plannedDays);

			res.push(
				<div key={key} className="pull-left">
					<FollowupCalendar key={key}
						className="treat-cal"
						data={item}
						onClick={this._calendarClick} >
						<div className="treat-info">{txt}</div>
					</FollowupCalendar>
					{
						editor === item &&
						<CalendEditor data={editor} onClose={this.closeEditor} />
					}
				</div>
				);
		});

		const clazz = 'treat-table' + (this.state.editor ? ' readonly' : '');

		return <div className={clazz}>{res}</div>;
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
