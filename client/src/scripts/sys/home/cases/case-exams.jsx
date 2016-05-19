import React from 'react';
import { Alert, DropdownButton, MenuItem, Row, Col } from 'react-bootstrap';
import { ListBox } from '../../../components';

import FollowupDisplay from './followup-display';

const options = [
	{ id: 'MEDEXAM', name: 'Medical Consultation' },
	{ id: 'MICROSCOPY', name: 'Exam Microscopy' },
	{ id: 'CULTURE', name: 'Exam Culture' },
	{ id: 'XPERT', name: 'Exam Xpert' },
	{ id: 'DST', name: 'Exam DST' },
	{ id: 'XRAY', name: 'Exam Xray' },
	{ id: 'HIV', name: 'Exam HIV' }
];

export default class CaseExams extends React.Component {

	constructor(props) {
		super(props);

		this.onFilterChange = this.onFilterChange.bind(this);
	}

	onFilterChange() {
		const self = this;
		return (evt, val) => {
			const obj = {};
			obj.filter = val;
			self.setState(obj);
		};
	}

	/**
	 * Render the exams and medical consultations to be displayed
	 * @param  {[type]} issues [description]
	 * @return {[type]}        [description]
	 */
	contentRender(followup) {
		if (!followup || followup.length === 0) {
			return <Alert bsStyle="warning">{'No result found'}</Alert>;
		}

		return (
			<div>
			{
				followup.map((item) => (
					<div key={item.data.id}>
						{this.isSelected(item) && <FollowupDisplay followup={item}/>}
					</div>
				))
			}
			</div>
			);
	}

	isSelected(item) {
		if (!this.state || !this.state.filter || this.state.filter.length === 0) {
			return true;
		}

		for (var i = 0; i < this.state.filter.length; i++) {
			if (this.state.filter[i].id === item.type.id) {
				return true;
			}
		}

		return false;
	}

	render() {
		return (
			<div>
				<DropdownButton bsStyle="primary" title={'New Follow-up'} id="newfollowup" className="def-margin-bottom">
					<MenuItem eventKey={1} >{'Medical Consultation'}</MenuItem>
					<MenuItem eventKey={1} >{'Exam Microscopy'}</MenuItem>
					<MenuItem eventKey={1} >{'Exam Culture'}</MenuItem>
					<MenuItem eventKey={1} >{'Exam Xpert'}</MenuItem>
					<MenuItem eventKey={1} >{'Exam DST'}</MenuItem>
					<MenuItem eventKey={1} >{'Exam HIV'}</MenuItem>
					<MenuItem eventKey={1} >{'Exam Xray'}</MenuItem>
				</DropdownButton>

				<ListBox ref="filter"
					mode="multiple"
					onChange={this.onFilterChange()}
					options={options}
					optionDisplay="name" />

				{this.contentRender(this.props.tbcase.followUp)}
			</div>);
	}
}

CaseExams.propTypes = {
	tbcase: React.PropTypes.object.isRequired
};
