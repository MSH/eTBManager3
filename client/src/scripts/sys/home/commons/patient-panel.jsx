import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { Profile, Fluidbar, Fa } from '../../../components/index';
import { app } from '../../../core/app';
import SessionUtils from '../../session-utils';

import './patient-panel.less';

export default class PatientPanel extends React.Component {


	stateClass() {
		switch (this.props.tbcase.state) {
			case 'WAITING_TREATMENT': return 'cs-NOT_ONTREATMENT';
			case 'ONTREATMENT': return 'cs-ONTREATMENT';
			default: return 'cs-CLOSED';
		}
	}

	render() {
		const tbcase = this.props.tbcase;
		const patient = this.props.tbcase.patient;
		if (!patient) {
			return null;
		}

		const type = patient.gender === 'MALE' ? 'male' : 'female';

		const lists = app.getState().app.lists;

		const claName = lists['CaseClassification' + tbcase.classification][tbcase.diagnosisType];

		const stateName = lists.CaseState[tbcase.state];
		const validationName = lists.ValidationState[tbcase.validationState];
		const ownerUnit = tbcase.ownerUnit;
		const adminUnit = SessionUtils.adminUnitLink(tbcase.ownerUnit.adminUnit, false, true, '/cases');

		const subtitle = (
			<div className="case-subtitle">
				<div>{claName}</div>
				<div>{tbcase.caseCode}</div>
				<div className="case-unit">
					<div>
						<Fa icon="hospital-o"/>
						<a href={SessionUtils.unitHash(ownerUnit.id, '/cases')}>{ownerUnit.name}</a>
						<br/>
						<Fa icon="map-marker"/>
						{adminUnit}
					</div>
				</div>
			</div>);

		return (
			<Fluidbar>
				<Grid>
					<Row>
						<div className="margin-2x">
						<Col md={12}>
								<Profile title={patient.name}
									subtitle={subtitle}
									type={type}
									size="large"
									/>
						</Col>
						</div>
					</Row>
					<Row>
						<Col xs={6} sm={3} smOffset={3} md={2} mdOffset={1}>
							<div className={'state-box ' + this.stateClass()}>
							{stateName}
							</div>
						</Col>
						<Col xs={6} sm={3} md={2} >
							<div className={'state-box vs-' + tbcase.validationState}>
							{validationName}
							</div>
						</Col>
					</Row>
				</Grid>
			</Fluidbar>
			);
	}
}

PatientPanel.propTypes = {
	tbcase: React.PropTypes.object,
	recordNumber: React.PropTypes.string,
	content: React.PropTypes.node
};
