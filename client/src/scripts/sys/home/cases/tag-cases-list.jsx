import React from 'react';
import { Alert } from 'react-bootstrap';
import { Card, Profile, WaitIcon, ReactTable } from '../../../components';
import { app } from '../../../core/app';

import SessionUtils from '../../session-utils';

import CrudPagination from '../../crud/crud-pagination';
import CrudCounter from '../../crud/crud-counter';
import CrudController from '../../crud/crud-controller';
import FakeCRUD from '../../../commons/fake-crud';

export default class TagCasesList extends React.Component {

	constructor(props) {
		super(props);
		this.loadList = this.loadList.bind(this);
		this.eventHandler = this.eventHandler.bind(this);

		// create fake-crud controller
		const casesfcrud = new FakeCRUD('/api/cases/tag/query');
		const opts = {
			pageSize: 50,
			readOnly: true,
			editorSchema: {},
			refreshAll: false
		};

		const controller = new CrudController(casesfcrud, opts);
		this.state = { controller: controller };
	}

	componentWillMount() {
		const self = this;
		const handler = this.state.controller.on((evt, data) => {
			self.eventHandler(evt, data);
		});
		this.setState({ handler: handler });

		this.loadList(this.props.tag.id);
	}

	componentWillUpdate(nextProps) {
		if (this.props.tag.id !== nextProps.tag.id) {
			this.loadList(nextProps.tag.id);
		}
	}

	componentWillUnmount() {
		// remove registration
		this.state.controller.removeListener(this.state.handler);
	}

	eventHandler(evt) {
		if (evt === 'list' || evt === 'fetching-list') {
			this.forceUpdate();
		}
	}

	/**
	 * Called when user clicks on a case
	 * @param  {[type]} id [description]
	 * @return {[type]}    [description]
	 */
	caseClick(item) {
		window.location.hash = SessionUtils.caseHash(item.id);
	}

	loadList(tagId) {
		const self = this;
		const qry = { unitId: this.props.unitId, adminUnitId: this.props.adminUnitId, tagId: tagId };
		this.state.controller.initList(qry).then(() => self.forceUpdate());
	}

	getClassificatonLabel(classification, diagnosistype) {
		const lists = app.getState().app.lists;
		const list = lists['CaseClassification' + classification.id];
		return list[diagnosistype.id];
	}

	render() {
		const tag = this.props.tag;

		if (!tag || !tag.id) {
			return null;
		}

		const controller = this.state.controller;

		const type = 'prof-tag-' + tag.type.toLowerCase();
		const header = (
			<Profile
				imgClass={type}
				fa="tag" title={tag.name}
				size="small"
				subtitle={tag.count < 1 ? __('form.norecordfound') : tag.count + ' ' + __('form.resultlist')}/>);

		return (
			<Card header={header} closeBtn onClose={this.props.onClose}>
				{
					// loading
					controller.isFetching() === true ?
						<WaitIcon type="card" /> : null
				}
				{
					// no results found
					controller.isFetching() !== true && (!controller.getList() || controller.getList().length < 1) ?
						<Alert bsStyle="warning">{__('form.norecordfound')}</Alert> : null
				}
				{
					// show case list
					controller.isFetching() !== true && controller.getList() && controller.getList().length > 0 ?
					<span>
						<ReactTable className="mtop-2x"
							columns={[
								{
									title: 'Patient',
									size: { sm: 4 },
									content: item =>
										<Profile type={item.gender.toLowerCase()} size="small"
											title={item.name} subtitle={item.caseCode} />
								},
								{
									title: 'Case Info',
									size: { sm: 2 },
									content: item => <div>{this.getClassificatonLabel(item.classification, item.diagnosisType)}
														<br/><div className="sub-text">{item.state.name}</div></div>
								},
								{
									title: 'Unit',
									size: { sm: 6 },
									content: item => <div>{item.ownerUnit.name}<br/>
											<div className="sub-text">{SessionUtils.adminUnitSpanDisplay(item.ownerUnit.adminUnit, false)}</div></div>
								}
							]} values={controller.getList()} onClick={this.caseClick}/>

						<CrudCounter controller={controller} className="mtop"/>
						<CrudPagination controller={this.state.controller} showCounter className="mtop" />
					</span> : null
				}
			</Card>
			);
	}
}

TagCasesList.propTypes = {
	tag: React.PropTypes.object,
	onClose: React.PropTypes.func,
	unitId: React.PropTypes.string,
	adminUnitId: React.PropTypes.string
};
