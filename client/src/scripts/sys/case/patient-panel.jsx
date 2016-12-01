import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { Profile, Fluidbar } from '../../components/index';
import { app } from '../../core/app';
import SessionUtils from '../session-utils';

import './patient-panel.less';

export default class PatientPanel extends React.Component {


    stateClass() {
        switch (this.props.tbcase.state) {
            case 'NOT_ONTREATMENT': return 'cs-NOT_ONTREATMENT';
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

        console.log('tbcase = ', tbcase);
        const stateName = lists.CaseState[tbcase.state];
        const validationName = tbcase.validated ? __('TbCase.validated') : __('TbCase.waitingValidation');
        const ownerUnit = tbcase.ownerUnit;

        const subtitle = (
            <div className="case-subtitle">
                <div>{claName}</div>
                <div>{tbcase.caseCode}</div>
                <div className="case-unit">
                    <div>
                    {
                        SessionUtils.unitDisplay(ownerUnit, '/cases')
                    }
                    </div>
                </div>
            </div>);

        const patientName = SessionUtils.nameDisplay(patient.name);
        const diagnosisType = SessionUtils.diagnosisTypeDisplay(tbcase.diagnosisType);

        return (
            <Fluidbar>
                <Grid>
                    <Row>
                        <div className="margin-2x">
                        <Col md={12}>
                                <Profile title={patientName}
                                    subtitle={subtitle}
                                    type={type}
                                    size="large"
                                    />
                        </Col>
                        </div>
                    </Row>
                    <Row>
                        <Col xs={6} sm={3} smOffset={3} md={2} mdOffset={1}>
                            <div className={'state-box dt-' + tbcase.diagnosisType}>
                            {diagnosisType}
                            </div>
                        </Col>
                        <Col xs={6} sm={3} md={2}>
                            <div className={'state-box ' + this.stateClass()}>
                            {stateName}
                            </div>
                        </Col>
                        <Col xs={6} sm={3} md={2} >
                            <div className={'state-box ' + (tbcase.validated ? 'vs-validated' : 'vs-notvalidated')}>
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
