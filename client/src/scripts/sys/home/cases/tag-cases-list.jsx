import React from 'react';
import { Badge, Alert } from 'react-bootstrap';
import { Card, Profile, WaitIcon, ReactTable } from '../../../components';

import moment from 'moment';
import SessionUtils from '../../session-utils';

import CrudPagination from '../../crud/crud-pagination';
import CrudController from '../../crud/crud-controller';
import FakeCRUD from '../../../commons/fake-crud';

const mockCases = [
		{
			patient: { name: 'Mauricio Santos', gender: 'MALE' },
			registrationDate: new Date(),
			caseCode: '23847',
			classification: 'TB',
			diagnosisType: 'SUSPECT',
			age: 41,
			state: 'WAITING_TREATMENT'
		},
		{
			patient: { name: 'Ricardo Memoria', gender: 'MALE' },
			registrationDate: new Date(),
			caseCode: '03492-3',
			classification: 'DRTB',
			diagnosisType: 'CONFIRMED',
			age: 2,
			state: 'ONTREATMENT'
		},
		{
			patient: { name: 'Juliana', gender: 'FEMALE' },
			registrationDate: new Date(),
			caseCode: '2389478',
			classification: 'NMT',
			diagnosisType: 'SUSPECT',
			age: 5,
			state: 'CLOSED'
		}
	];

export default class TagCasesList extends React.Component {

	constructor(props) {
		super(props);
		this.onClose = this.onClose.bind(this);

		this.state = { res: null, lastTagLoaded: null };
	}

	componentWillMount() {
		const self = this;
		const res = { count: 0, list: mockCases };

		setTimeout(() => { self.setState({ res: res }); }, 2000);
	}

	onClose() {
		this.props.onClose();
		this.setState({ res: null });
	}

	/**
	 * Called when user clicks on a case
	 * @param  {[type]} id [description]
	 * @return {[type]}    [description]
	 */
	caseClick(item) {
		window.location.hash = SessionUtils.caseHash(item.id);
	}

	render() {
		const tag = this.props.tag;

		if (!tag || !tag.id || !this.props.view || (this.props.view && !this.props.unitId)) {
			return null;
		}

		const type = 'prof-tag-' + tag.type.toLowerCase();
		const header = (
			<Profile
				imgClass={type}
				fa="tag" title={tag.name}
				size="small"
				subtitle={tag.count < 1 ? __('form.norecordfound') : tag.count + ' ' + __('form.resultlist')}/>);

		return (
			<Card header={header} closeBtn onClose={this.onClose}>
				{
					// loading
					!this.state.res ?
						<WaitIcon type="card" /> : null
				}
				{
					// no results found
					this.state.res && (!this.state.res.list || this.state.res.list.length < 1) ?
						<Alert bsStyle="warning">{__('form.norecordfound')}</Alert> : null
				}
				{
					// show case list
					this.state.res && this.state.res.list && this.state.res.list.length > 0 ?
						<ReactTable className="mtop-2x"
							columns={[
								{
									title: 'Patient',
									size: { sm: 4 },
									content: item =>
										<Profile type={item.patient.gender.toLowerCase()} size="small"
											title={item.patient.name} subtitle={item.recordNumber} />
								},
								{
									title: 'Registration date',
									size: { sm: 2 },
									content: item => <div>{moment(item.registrationDate).format('ll')}<br/>
											<div className="sub-text">{moment(item.registrationDate).format('LT')}</div></div>
								}
							]} values={this.state.res.list} onClick={this.caseClick}/> : null
				}
			</Card>
			);
	}
}

TagCasesList.propTypes = {
	tag: React.PropTypes.object,
	view: React.PropTypes.oneOf(['workspace', 'unit']),
	onClose: React.PropTypes.func,
	unitId: React.PropTypes.string
};
