import React from 'react';
import { server } from '../../../commons/server';
import { Grid, Col, Row } from 'react-bootstrap';
import { FormDialog } from '../../../components';
import { app } from '../../../core/app';

/**
 * Component that displays and handle notification form
 */
export default class NotifForm extends React.Component {

    constructor(props) {
        super(props);
        this.getRemoteForm = this.getRemoteForm.bind(this);
        this.save = this.save.bind(this);
    }

    componentWillMount() {
        this.setState({ patientId: this.props.patient.id });
        delete this.props.patient.id;
    }

    getRemoteForm() {
        const req = { diagnosisType: this.props.diagnosisType, caseClassification: this.props.classification };
        return server.post('/api/cases/case/newnotif/form', req).then(res => {
            res.doc.patient = this.props.patient;
            return res;
        });
    }

    save(doc) {
        const req = { doc: doc };
        req.unitId = this.props.tbunit.id;
        req.patientId = this.state.patientId;

        return server.post('/api/cases/case/newnotif', req).then(res => {
            if (res.errors) {
                return Promise.reject(res.errors);
            }

            app.goto('/sys/case?id=' + res.result);

            return res.result;
        });
    }

    render() {
        return (
            <Grid fluid className="mtop-2x">
                <Row>
                    <Col mdOffset={2} md={9}>
                        <FormDialog
                            wrapType="card"
                            remotePath={this.getRemoteForm}
                            onCancel={this.props.onCancel}
                            onConfirm={this.save} />
                    </Col>
                </Row>
            </Grid>
        );
    }
}

NotifForm.propTypes = {
    patient: React.PropTypes.object,
    onCancel: React.PropTypes.func,
    diagnosisType: React.PropTypes.string,
    classification: React.PropTypes.string,
    tbunit: React.PropTypes.object
};
