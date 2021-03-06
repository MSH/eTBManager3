import React from 'react';
import { Grid, Col, Row, DropdownButton, MenuItem } from 'react-bootstrap';
import { Card, WaitIcon, Fa, observer } from '../../components';
import Form from '../../forms/form';
import { server } from '../../commons/server';
import { app } from '../../core/app';
import TreatProgress from './treat/treat-progress';
import TreatTimeline from './treat/treat-timeline';
import TreatmentUpdate from './treat/treat-update';
import EdtPrescription from './treat/edt-prescription';
import AddPrescription from './treat/add-prescription';
import TreatFollowup from './treat/treat-followup';
import NoTreatPanel from './treat/no-treat-panel';

import Events from './events';

/**
 * Display the content of the case treatment tab
 */
class CaseTreatment extends React.Component {

    constructor(props) {
        super(props);

        this.menuClick = this.menuClick.bind(this);
        this.fetchData = this.fetchData.bind(this);
        this.handleEvent = this.handleEvent.bind(this);
        this.closeDlg = this.closeDlg.bind(this);
        this.updateTreatment = this.updateTreatment.bind(this);

        this.state = {
            sc1: {
                controls: [
                    {
                        type: 'string',
                        label: __('Regimen'),
                        value: doc => !doc.movedToIndividualized && doc.regimen ? doc.regimen.name : __('regimens.individualized'),
                        size: { md: 12 }
                    },
                    {
                        type: 'text',
                        property: 'regimenIni.name',
                        label: __('TbCase.regimenIni'),
                        visible: doc => doc.regimen && doc.regimenIni && doc.regimenIni.id !== doc.regimen.id,
                        size: { md: 12 }
                    },
                    {
                        type: 'period',
                        property: 'period',
                        label: __('cases.treat'),
                        size: { md: 12 }
                    }
                ]
            }
        };
    }

    componentWillMount() {
        this.fetchData();
    }

    /**
     * Called when treatment timeline must be updated
     * @param  {[type]} action [description]
     * @return {[type]}        [description]
     */
    handleEvent(evt, doc) {
        switch (evt) {
            case Events.delPrescription:
                this.deletePrescription(doc);
                break;
            case Events.updateTreatment:
                this.updateTreatment();
                break;
            default:
        }
    }

    deletePrescription(doc) {
        return server.delete('/api/cases/case/treatment/prescription/delete/' + doc.data.prescriptionId)
                .then(() => {
                    this.updateTreatment();
                });
    }

    updateTreatment() {
        this.setState({ fetching: true });

        // remove treatment information
        const tbcase = this.props.tbcase;
        delete tbcase.treatment;

        // start updating treatment
        this.fetchData();
    }

    fetchData() {
        const self = this;
        const tbcase = this.props.tbcase;

        // check if case is on treatment and has treatment information
        if (!tbcase.treatment && tbcase.treatmentPeriod) {
            const id = tbcase.id;

            server.get('/api/cases/case/treatment/' + id)
                .then(res => {
                    tbcase.treatment = res;
                    tbcase.treatmentPeriod = res.period;
                    self.setState({ fetching: false });
                });
        }
    }

    menuClick(key) {
        switch (key) {
            case 1:
                app.dispatch(Events.addPrescription);
                break;
            case 2:
                app.dispatch(Events.treatUpdateForm);
                break;
            case 3:
                this.undoTreatment();
                break;
            default:
        }
    }

    /**
     * Undo the treatment, moving the case back to the 'not on treatment' state
     */
    undoTreatment() {
        app.messageDlg({
            title: __('cases.treat.undo'),
            message: __('cases.treat.undo.confirm'),
            style: 'warning',
            type: 'YesNo'
        })
        .then(res => {
            if (res !== 'yes') {
                return Promise.reject();
            }

            const caseId = this.props.tbcase.id;
            return server.post('/api/cases/case/treatment/undo/' + caseId);
        })
        .then(() => app.dispatch('case-update'))
        .catch(() => {});
    }

    closeDlg() {
        this.setState({ show: null, prescDataEdt: null });
    }

    render() {
        const tbcase = this.props.tbcase;

        // is not on treatment
        if (!tbcase.treatmentPeriod) {
            return <NoTreatPanel tbcase={this.props.tbcase} />;
        }

        const data = tbcase.treatment;

        if (!data) {
            return <WaitIcon type="card" />;
        }

        const optionsBtn = (
            <DropdownButton className="lnk-muted" bsStyle="link"
                title={<Fa icon="cog" />} id="ttmenu" pullRight
                onSelect={this.menuClick}>
                <MenuItem eventKey={1}>{__('Regimen.add')}</MenuItem>
                <MenuItem eventKey={2}>{__('cases.regimens.changeperiod')}</MenuItem>
                <MenuItem eventKey={3}>{__('cases.treat.undo')}</MenuItem>
            </DropdownButton>
            );

        return (
            <div>
                <Card title={__('cases.details.treatment')}>
                    <Grid fluid>
                        <Row>
                            <Col md={6}>
                            {
                                !this.state.fetching &&
                                    <Form doc={data} schema={this.state.sc1} readOnly />
                            }
                            </Col>
                            <Col md={6}>
                                <TreatProgress value={data.progress} label={__('cases.mantreatment')}/>
                            </Col>
                        </Row>
                    </Grid>
                </Card>

                <Card title={__('cases.details.treatment.prescmeds')} headerRight={optionsBtn}>
                    <TreatTimeline treatment={data} />
                </Card>

                <TreatFollowup treatment={data} tbcase={this.props.tbcase} />

                <TreatmentUpdate tbcase={tbcase} />

                <AddPrescription tbcase={tbcase} />

                <EdtPrescription tbcase={tbcase} />
            </div>
        );
    }
}

export default observer(CaseTreatment, [Events.updateTreatment, Events.delPrescription]);

CaseTreatment.propTypes = {
    tbcase: React.PropTypes.object.isRequired
};
