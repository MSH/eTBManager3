import React from 'react';
import { Alert, DropdownButton, MenuItem, Row, Col } from 'react-bootstrap';
import { SelectionBox } from '../../../components';

import FollowupDisplay from './followup-display';

const options = [
	{ id: 'MEDEXAM', name: __('FollowUpType.MEDEXAM') },
	{ id: 'MICROSCOPY', name: __('FollowUpType.MICROSCOPY') },
	{ id: 'CULTURE', name: __('FollowUpType.CULTURE') },
	{ id: 'XPERT', name: __('FollowUpType.XPERT') },
	{ id: 'DST', name: __('FollowUpType.DST') },
	{ id: 'XRAY', name: __('FollowUpType.XRAY') },
	{ id: 'HIV', name: __('FollowUpType.HIV') }
];

export default class CaseExams extends React.Component {

	constructor(props) {
		super(props);

		this.onFilterChange = this.onFilterChange.bind(this);
		this.state = { filter: options.slice() };

		this.renderEditDlg = this.renderEditDlg.bind(this);
		this.confirmDelMessage = this.confirmDelMessage.bind(this);
	}

	renderEditDlg() {
		window.alert('edit');
	}

	confirmDelMessage() {
		window.alert('delete');
	}

	onFilterChange() {
		const self = this;
		return (evt, val) => {
			const obj = {};
			obj.filter = val;
			self.setState(obj);
		};
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
						{this.isSelected(item) && <FollowupDisplay followup={item} onEdit={this.renderEditDlg} onDelete={this.confirmDelMessage}/>}
					</div>
				))
			}
			</div>
			);
	}

	render() {
		return (
			<div>
				<Row>
					<Col sm={1}>
						<DropdownButton id="newFollowUp" bsStyle="primary" title={'New'}>
							{
								options.map((item, index) => (
									<MenuItem key={index} eventKey={index} >{item.name}</MenuItem>
								))
							}
						</DropdownButton>
					</Col>
					<Col sm={11}>
						<SelectionBox ref="filter"
							value={this.state.filter}
							mode="multiple"
							optionDisplay={'name'}
							onChange={this.onFilterChange()}
							options={options}/>
					</Col>
				</Row>
				{this.contentRender(this.props.tbcase.followUp)}
			</div>);
	}
}

CaseExams.propTypes = {
	tbcase: React.PropTypes.object.isRequired
};
