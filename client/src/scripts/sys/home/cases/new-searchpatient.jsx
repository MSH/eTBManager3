import React from 'react';
import { Grid, Row, Col, Button, ButtonToolbar } from 'react-bootstrap';
import { Profile, Card, AsyncButton, ReactTable, WaitIcon, Fa, CaseState } from '../../../components/index';
import Form from '../../../forms/form';

import { app } from '../../../core/app';
import { format } from '../../../commons/utils';
import SessionUtils from '../../session-utils';

import FakeCRUD from '../../../commons/fake-crud';
import CrudPagination from '../../crud/crud-pagination';
import CrudCounter from '../../crud/crud-counter';
import CrudController from '../../crud/crud-controller';

import moment from 'moment';

const fschema = {
			controls: [
				{
					property: 'name',
					type: 'personName',
					label: __('Patient.name'),
					required: true,
					size: { md: 6 }
				},
				{
					property: 'birthDate',
					required: false,
					type: 'date',
					label: __('Patient.birthDate'),
					size: { md: 3 }
				}
			]
		};

/**
 * Component that displays and handle notification form
 */
export default class SearchPatient extends React.Component {

	constructor(props) {
		super(props);
		this.searchClick = this.searchClick.bind(this);
		this.eventHandler = this.eventHandler.bind(this);
		this.selectPatient = this.selectPatient.bind(this);

		// create fake-crud controller
		const casesfcrud = new FakeCRUD('/api/tbl/patient/search');
		const opts = {
			pageSize: 20,
			readOnly: true,
			editorSchema: {},
			refreshAll: false
		};

		const controller = new CrudController(casesfcrud, opts);
		this.state = { controller: controller, doc: {} };
	}

	componentWillMount() {
		const self = this;
		const handler = this.state.controller.on((evt, data) => {
			self.eventHandler(evt, data);
		});
		this.setState({ handler: handler });
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

	loadPatients() {
		this.setState({ fetching: true });
		const self = this;

		const qry = this.state.doc;

		this.state.controller.initList(qry).then(() => {
			self.setState({ fetching: false });
			self.forceUpdate();
		});
	}

	searchClick() {
		const errors = this.refs.form.validate();
		this.setState({ errors: errors });
		if (errors) {
			return;
		}

		this.loadPatients();
	}

	selectPatient(item) {
		let patientInfo;

		if (item && item.patient) {
			patientInfo = item.patient;
		} else {
			patientInfo = this.state.doc;
		}

		this.props.onSelect(patientInfo);
	}

	latestCaseRender(item) {
		// get label according to classification and diag type
		const typeLabel = app.getState().app.lists['CaseClassification' + item.classification.id][item.diagnosisType.id];

		let temporalInfo;
		if (item.diagnosisType.id === 'SUSPECT') {
			temporalInfo = format(__('cases.sit.SUSP.date'), moment(item.registrationDate).format('ll'));
		} else {
			switch (item.state.id) {
				case 'NOT_ONTREATMENT':
					temporalInfo = format(__('cases.sit.CONF.date'), moment(item.diagnosisDate).format('ll')); break;
				case 'ONTREATMENT':
					temporalInfo = format(__('cases.sit.ONTREAT.date'), moment(item.iniTreatmentDate).format('ll')); break;
				case 'CLOSED':
					temporalInfo = format(__('cases.sit.OUTCOME.date'), moment(item.outcomeDate).format('ll')); break;
				default:
					temporalInfo = null;
			}
		}

		return (
			<div>
				<div style={{ fontSize: '0.9em' }} className="pull-right">
					<CaseState state={item.state} />
				</div>
				<div className="bold">
					{typeLabel}
				</div>
				<div>
					<Fa icon="hospital-o" />
					{item.notificationUnit.name}
				</div>
				<div className="sub-text">
					{SessionUtils.adminUnitDisplay(item.notificationUnit.adminUnit, false, true)}
				</div>
				<div style={{ color: '#b5912f' }}>
					{temporalInfo}
				</div>
				{item.state.id === 'CLOSED' && <div className="sub-text">{item.outcome}</div>}
			</div>
		);
	}

	patientListRender() {
		const controller = this.state.controller;
		const lst = controller.getList();

		// list not initialized
		if (!lst) {
			return null;
		}

		// loading while paginating
		if (controller.isFetching()) {
			return (<div>
						<Card className="mtop">
							<WaitIcon type="card" />
						</Card>
					</div>);
		}

		const newPatientHeader = lst.length > 0 ? (<span style={{ fontSize: '1.0em', color: '#a0a0a0' }}>{__('cases.new.msg')}</span>) : null;

		return (
			<div>
				<Card className="no-margin-bottom">
					<CrudCounter controller={controller} />
					<CrudPagination controller={controller} showCounter className="mtop" />
					{
						lst.length > 0 &&
							<ReactTable values={lst}
								onClick={this.props.onSelect}
								columns={[
									{
										title: __('Patient'),
										size: { sm: 4 },
										content: item =>
											<Profile type={item.patient.gender.toLowerCase()}
												size="small"
												title={item.patient.name.name} subtitle={item.caseNumber} />
									},
									{
										title: __('Patient.birthDate'),
										size: { sm: 2 },
										content: item => moment(item.patient.birthDate).format('ll')
									},
									{
										title: __('cases.sit.current'),
										size: { sm: 6 },
										content: item => this.latestCaseRender(item)
									}
								]} />
					}
					<CrudPagination controller={controller} showCounter className="mtop" />
				</Card>
				<Card header={newPatientHeader} className="no-padding" style={{ backgroundColor: '#f6f6f6' }}>
					<Button bsStyle="success" onClick={this.selectPatient}>{__('cases.newpatient')}</Button>
				</Card>
			</div>
			);
	}

	render() {
		return (
			<Grid fluid>
				<Row>
					<Col mdOffset={2} md={9}>
						<Card title={__('cases.searchpatient')} className="mtop">
							<div>
								<Form ref="form"
									schema={fschema}
									doc={this.state.doc}
									errors={this.state.errors} />
								<Row>
									<Col sm={12}>
										<ButtonToolbar>
										<AsyncButton
											fetching={this.state.fetching}
											onClick={this.searchClick}
											bsStyle="primary">
											{'Search'}
										</AsyncButton>
										<Button bsStyle="link" onClick={this.props.onCancel}>{__('action.cancel')}</Button>
										</ButtonToolbar>
									</Col>
								</Row>
							</div>
						</Card>
						{this.patientListRender()}
					</Col>
				</Row>
			</Grid>
			);
	}
}

SearchPatient.propTypes = {
	onSelect: React.PropTypes.func,
	onCancel: React.PropTypes.func
};
