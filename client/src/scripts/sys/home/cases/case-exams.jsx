import React from 'react';
import { Alert, DropdownButton, MenuItem, Row, Col } from 'react-bootstrap';
import { SelectionBox, MessageDlg, Fa, WaitIcon } from '../../../components';

import FollowupDisplay from './followup-display';
import FollowupModal from './followup-modal';
import { getFollowUpTypes, getFollowUpType } from './followup-utils';

import { isString } from '../../../commons/utils';
import { server } from '../../../commons/server';
import CRUD from '../../../commons/crud';
import moment from 'moment';

export default class CaseExams extends React.Component {

	constructor(props) {
		super(props);

		const options = getFollowUpTypes();

		this.onFilterChange = this.onFilterChange.bind(this);
		this.startOperation = this.startOperation.bind(this);
		this.endOperation = this.endOperation.bind(this);
		this.closeDel = this.closeDel.bind(this);

		this.state = { filter: options.slice(), operation: null, showDelMsg: false, showForm: false };
	}

	componentWillMount() {
		if (!this.props.tbcase.followups) {
			this.refreshFollowups();
		}
	}

	refreshFollowups() {
		const self = this;
		const id = this.props.tbcase.id;

		return server.get('/api/cases/case/followups/' + id)
				.then(res => {
					self.props.tbcase.followups = res.list;

					this.forceUpdate();
				});
	}

	/**
	 * Insert on state values that will control the operation in progress
	 * @param  {[type]} opTypeP       New(new), edit(edt) or delete(del)
	 * @param  {[type]} followUpTypeP object with the details of followUp type
	 * @param  {[type]} docP          Data of followup used on delete and edit operation
	 * @return {[type]}               function that create the operation object on the state of this component
	 */
	startOperation(opTypeP, followup) {
		const self = this;

		return () => {
			if (opTypeP === 'del') {
				self.setState({ showDelMsg: true });
			} else if (opTypeP === 'new' || opTypeP === 'edt') {
				self.setState({ showForm: true });
			}

			const op = {};
			op.opType = opTypeP;
			op.followUpType = getFollowUpType(followup.type);
			op.followUpId = followup ? followup.data.id : null;
			op.followUpDate = followup.data.date;
			op.tbcaseId = self.props.tbcase.id;
			op.crud = new CRUD(op.followUpType.crud);
			self.setState({ operation: op });
		};
	}

	/**
	 * Clear operation object to end any kind of operation
	 * @return {[type]} [description]
	 */
	endOperation(res) {
		const self = this;

		self.setState({ operation: null, showDelMsg: false, showForm: false });

		let sMsg = null;
		switch (res) {
			case 'successNew': sMsg = __('default.entity_created'); break;
			case 'successEdt': sMsg = __('default.entity_updated'); break;
			case 'successDel': sMsg = __('default.entity_deleted'); break;
			default: sMsg = null;
		}

		if (isString(res) && res.search('success') >= 0) {
			self.refreshFollowups()
			.then(() => {
				if (sMsg) {
					self.setState({ successMsg: sMsg });
					setTimeout(() => { self.setState({ successMsg: null }); }, 4000);
				}
			});
		}
	}

	closeDel(res) {
		const op = this.state.operation;

		if (res === 'yes') {
			return op.crud.delete(op.followUpId).then(() => this.endOperation('successDel'));
		}

		this.endOperation();
		return null;
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

		if (!op || op.opType !== 'del') {
			return null;
		}

		var delTitle = __('action.delete') + ' - ';
		delTitle = delTitle + op.followUpType.name + ' ';
		delTitle = delTitle + moment(op.followUpDate).format('ll');

		return delTitle;
	}

	/**
	 * Render the exams and medical consultations to be displayed
	 * @param  {[type]} issues [description]
	 * @return {[type]}        [description]
	 */
	contentRender() {
		const data = this.props.tbcase.followups;

		if (!data || data.length < 1) {
			return <Alert bsStyle="warning">{'No result found'}</Alert>;
		}

		return (
			<div>
			{
				data.map(item => (
					<div key={item.data.id}>
						{this.isSelected(item) && <FollowupDisplay followup={item} onEdit={this.startOperation('edt', item)} onDelete={this.startOperation('del', item)}/>}
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
									<MenuItem key={index} eventKey={index} onSelect={this.startOperation('new', { type: item.id, name: item.name, data: {} })} >{item.name}</MenuItem>
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

				{!this.props.tbcase.followups ? <WaitIcon type="card" /> : this.contentRender()}

				<MessageDlg show={this.state.showDelMsg}
					onClose={this.closeDel}
					title={this.renderDelTitle()}
					message={__('form.confirm_remove')}
					style="warning"
					type="YesNo" />

				{this.state.showForm === true &&
					<FollowupModal onClose={this.endOperation}
						operation={this.state.operation} />
				}

			</div>);
	}
}

CaseExams.propTypes = {
	tbcase: React.PropTypes.object.isRequired
};
