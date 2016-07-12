import React from 'react';
import { Alert, DropdownButton, MenuItem, Row, Col } from 'react-bootstrap';
import { SelectionBox, MessageDlg, Fa, WaitIcon } from '../../../components';

import FollowupDisplay from './followup-display';
import FollowupModal from './followup-modal';
import { server } from '../../../commons/server';

import moment from 'moment';
import { getFollowUpTypes, getFollowUpType } from './followup-utils';

export default class CaseExams extends React.Component {

	constructor(props) {
		super(props);

		const options = getFollowUpTypes();

		this.onFilterChange = this.onFilterChange.bind(this);
		this.startOperation = this.startOperation.bind(this);
		this.endOperation = this.endOperation.bind(this);

		this.state = { filter: options.slice(), operation: null };
	}

	componentWillMount() {
		this.refreshFollowups();
	}

	refreshFollowups() {
		const self = this;
		const id = this.props.tbcase.id;

		server.get('/api/cases/case/followups/' + id)
			.then(res => self.setState({ data: res }));
	}

	/**
	 * Insert on state values that will control the operation in progress
	 * @param  {[type]} opTypeP       New(new), edit(edt) or delete(del)
	 * @param  {[type]} followUpTypeP object with the details of followUp type
	 * @param  {[type]} docP          Data of followup used on delete and edit operation
	 * @return {[type]}               function that create the operation object on the state of this component
	 */
	startOperation(opTypeP, followUpTypeP, docP) {
		const self = this;
		docP.tbcaseId = this.props.tbcase.id;

		return () => {
			const op = {};
			op.opType = opTypeP;
			op.followUpType = followUpTypeP;
			op.doc = docP;
			self.setState({ operation: op });
		};
	}

	/**
	 * Clear operation object to end any kind of operation
	 * @return {[type]} [description]
	 */
	endOperation(res) {
		const self = this;
		const sMsg = res === 'success' ? __('default.entity_created') : null;

		const op = {
			opType: null,
			followUpType: null,
			doc: null
		};

		self.setState({ operation: op, successMsg: sMsg });
		setTimeout(() => { self.setState({ successMsg: null }); }, 4000);

		if (res === 'success') {
			self.refreshFollowups();
		}
	}

	onFilterChange() {
		const self = this;
		return (val) => {
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
			if (this.state.filter[i].id === item.type) {
				return true;
			}
		}

		return false;
	}

	renderDelTitle() {
		const op = this.state.operation;

		if (!op.doc) {
			return null;
		}

		var delTitle = __('action.delete') + ' - ';
		delTitle = delTitle + op.followUpName + ' ';
		delTitle = delTitle + moment(op.doc[op.followUpType.dateField]).format('ll');

		return delTitle;
	}

	/**
	 * Render the exams and medical consultations to be displayed
	 * @param  {[type]} issues [description]
	 * @return {[type]}        [description]
	 */
	contentRender() {
		const data = this.state.data;

		if (!data || !data.list || data.list.length < 1) {
			return <Alert bsStyle="warning">{'No result found'}</Alert>;
		}

		return (
			<div>
			{
				data.list.map((item) => (
					<div key={item.data.id}>
						{this.isSelected(item) && <FollowupDisplay followup={item} onEdit={this.startOperation('edt', getFollowUpType(item.type), item.data)} onDelete={this.startOperation('del', getFollowUpType(item.type), item.data)}/>}
					</div>
				))
			}
			</div>
			);
	}

	render() {
		const options = getFollowUpTypes();

		return (
			<div>
				<Row>
					<Col sm={12}>
						<DropdownButton id="newFollowUp" bsStyle="default" title={<span><Fa icon="plus-circle"/>{__('action.add')}</span>}>
							{
								options.map((item, index) => (
									<MenuItem key={index} eventKey={index} onSelect={this.startOperation('new', getFollowUpType(item.id), {})} >{item.name}</MenuItem>
								))
							}
						</DropdownButton>
					</Col>
				</Row>
				<Row className="mtop">
					<Col sm={12}>
						<SelectionBox ref="filter"
							value={this.state.filter}
							mode="multiple"
							optionDisplay={'name'}
							onChange={this.onFilterChange()}
							options={options}/>
					</Col>
				</Row>

				{
					!!this.state.successMsg &&
					<Alert bsStyle="success">{this.state.successMsg}</Alert>
				}

				{!this.state || !this.state.data ? <WaitIcon type="card" /> : this.contentRender()}

				{!!this.state.operation &&
				<MessageDlg show={this.state.operation.opType === 'del'}
					onClose={this.endOperation}
					title={this.renderDelTitle()}
					message={__('form.confirm_remove')}
					style="warning"
					type="YesNo" />}

				{!!this.state.operation &&
				<FollowupModal show={this.state.operation.opType === 'new' || this.state.operation.opType === 'edt'}
					onClose={this.endOperation}
					opType={this.state.operation.opType}
					followUpType={this.state.operation.followUpType}
					tbcase={this.props.tbcase} />}

			</div>);
	}
}

CaseExams.propTypes = {
	tbcase: React.PropTypes.object.isRequired
};
