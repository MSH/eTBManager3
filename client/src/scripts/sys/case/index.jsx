
import React from 'react';
import { Grid, Row, Col, Nav, NavItem, Button, Alert, OverlayTrigger, Tooltip } from 'react-bootstrap';
import { Card, WaitIcon, Fa, CommandBar, observer } from '../../components';
import PatientPanel from './patient-panel';
import { server } from '../../commons/server';
import { app } from '../../core/app';
import Events from './events';
import SessionUtils from '../session-utils';

import CaseData from './case-data';
import CaseEditForm from './case-edit-form';
import OtherCases from './other-cases';
import CaseExams from './case-exams';
import CaseTreatment from './case-treatment';
import CaseClose from './case-close';
import CaseMove from './case-move';
import CaseIssues from './case-issues';
import CaseTags from './case-tags';
import SuspectFollowUp from './suspect-followup';


class Details extends React.Component {

    constructor(props) {
        super(props);
        this.selectTab = this.selectTab.bind(this);
        this.show = this.show.bind(this);
        this.deleteConfirm = this.deleteConfirm.bind(this);
        this.reopenConfirm = this.reopenConfirm.bind(this);
        this.validationConfirm = this.validationConfirm.bind(this);
        this.rollbackTransfer = this.rollbackTransfer.bind(this);
        this.transferInNotOnTreat = this.transferInNotOnTreat.bind(this);
        this.transferIn = this.transferIn.bind(this);
        this.closeSuspectFU = this.closeSuspectFU.bind(this);

        this.state = { selTab: 0 };
    }

    componentWillMount() {
        const id = this.props.route.queryParam('id');
        this.fetchData(id);
    }

    componentWillReceiveProps(nextProps) {
        // check if page must be updated
        const id = nextProps.route.queryParam('id');
        const oldId = this.state.tbcase ? this.state.tbcase.id : null;
        if (id !== oldId) {
            this.fetchData(id);
        }
    }

    /**
     * Called when case details must be updated
     * @param  {[type]} action [description]
     * @return {[type]}        [description]
     */
    handleEvent() {
        this.fetchData(this.state.tbcase.id);
    }

    fetchData(id) {
        const self = this;
        server.get('/api/tbl/case/' + id)
        .then(tbcase => self.setState({    tbcase: tbcase }));

        this.setState({ tbcase: null });
    }

    tagsRender() {
        const lst = this.state.tbcase.tags;

        if (!lst || lst.length < 1) {
            return (
                <div className="message-muted">
                    <Fa icon="tags" />
                    <div>{__('cases.details.tags.noresult')}</div>
                </div>
            );
        }

        return (
            <div>
                {
                    lst.map(item => (
                        <li key={item.id} className={'tag-' + item.type.toLowerCase()}>
                            <div className="tag-title">{item.name}</div>
                        </li>
                    ))
                }
            </div>
        );
    }

    selectTab(key) {
        this.setState({ selTab: key });
    }

    show(cpt, show) {
        const self = this;
        return () => {
            const obj = {};
            obj[cpt] = show;
            self.setState(obj);
        };
    }

    showConfirmDlg(title, message, onConfirm) {
        app.messageDlg({
            title: title,
            message: message,
            style: 'warning',
            type: 'YesNo'
        })
        .then(res => onConfirm(res));
    }

    deleteConfirm(action) {
        if (action === 'yes') {
            server.delete('/api/tbl/case/' + this.state.tbcase.id)
                .then(() => {
                    app.messageDlg({
                        title: __('action.delete'),
                        message: __('default.entity_deleted'),
                        style: 'info',
                        type: 'Ok'
                    })
                    .then(() => { window.location.hash = SessionUtils.unitHash(this.state.tbcase.ownerUnit.id, '/cases'); });
                });
        }

        this.setState({ showDelConfirm: false });
    }

    validationConfirm(action) {
        if (action === 'yes') {
            server.get('/api/cases/case/validate/' + this.state.tbcase.id)
                .then(res => {
                    if (res && res.errors) {
                        return Promise.reject(res.errors);
                    }

                    this.fetchData(this.state.tbcase.id);

                    return res;
                });
        }

        this.setState({ showValidationConfirm: false });
    }

    reopenConfirm(action) {
        if (action === 'yes') {
            server.get('/api/cases/case/reopen/' + this.state.tbcase.id)
                .then(res => {
                    if (res && res.errors) {
                        return Promise.reject(res.errors);
                    }

                    this.fetchData(this.state.tbcase.id);

                    return res;
                });
        }

        this.setState({ showReopenConfirm: false });
    }

    rollbackTransfer() {
        return server.get('/api/cases/case/undotransferout/' + this.state.tbcase.id)
                .then(res => {
                    if (!res.success) {
                        return Promise.reject(res.errors);
                    }

                    app.dispatch('case-update');

                    return res.result;
                });
    }

    transferIn() {
        if (this.state.tbcase.state === 'ONTREATMENT') {
            return this.show('showMoveCase', true);
        }

        return () => this.transferInNotOnTreat();
    }

    transferInNotOnTreat() {
        const doc = { tbcaseId: this.state.tbcase.id };
        return server.post('/api/cases/case/transferin', doc)
                .then(res => {
                    if (!res.success) {
                        return Promise.reject(res.errors);
                    }

                    app.dispatch('case-update');

                    return res.result;
                });
    }

    renderTransferMessage() {
        const tbcase = this.state.tbcase;

        if (!tbcase.transferring) {
            return null;
        }

        const confirmlnk = <a className="mright-2x" onClick={this.transferIn()}><Fa icon="check"/>{__('cases.move.confirm')}</a>;
        const confirmlbl = (
                    <OverlayTrigger placement="top" overlay={<Tooltip id="actdel">{__('cases.move.confirm.notallow')}</Tooltip>}>
                        <span className="mright-2x"><Fa icon="check"/>{__('cases.move.confirm')}</span>
                    </OverlayTrigger>
                );


        return (
            <Alert bsStyle="warning" className="transf-alert">
                <Fa icon="exchange" className="mright" />
                {__('cases.move.msg') + ' ' + tbcase.ownerUnit.name + '. '}
                <br/>
                {app.getState().session.playOtherUnits || app.getState().session.unitId === tbcase.ownerUnit.id ? confirmlnk : confirmlbl}
                <a onClick={this.rollbackTransfer}><Fa icon="undo"/>{__('cases.move.cancel') + ' ' + tbcase.transferOutUnit.name}</a>
            </Alert>
        );
    }

    closeSuspectFU() {
        this.setState({ showSuspectFollowUp: false, suspectFUCla: null });
    }

    render() {
        const tbcase = this.state.tbcase;

        if (!tbcase) {
            return <WaitIcon type="page" />;
        }

        const seltab = this.state.selTab;

        const tabs = (
            <Nav bsStyle="tabs" activeKey={seltab}
                onSelect={this.selectTab}
                className="app-tabs">
                <NavItem key={0} eventKey={0}>{__('cases.details.case')}</NavItem>
                <NavItem key={1} eventKey={1}>{__('cases.details.followup')}</NavItem>
                <NavItem key={2} eventKey={2}>{__('cases.details.treatment')}</NavItem>
                <NavItem key={3} eventKey={3}>{__('cases.issues')}</NavItem>
            </Nav>
            );

        const tagBtn = (<Button onClick={this.show('showTagEdt', true)} bsSize="small">
                            <Fa icon="pencil"/>
                        </Button>);

        // create command list
        const commands = [
            {
                title: __('cases.delete'),
                onClick: () => this.showConfirmDlg(__('action.delete'), __('form.confirm_remove'), this.deleteConfirm),
                icon: 'remove'
            },
            {
                title: __('cases.validate'),
                onClick: () => this.showConfirmDlg(__('cases.validate'), __('cases.validate.confirm'), this.validationConfirm),
                icon: 'check',
                visible: !this.state.tbcase.validated
            },
            {
                title: __('cases.reopen'),
                onClick: () => this.showConfirmDlg(__('cases.reopen'), __('cases.reopen.confirm'), this.reopenConfirm),
                icon: 'power-off',
                visible: this.state.tbcase.state === 'CLOSED'
            },
            {
                title: __('cases.close'),
                onClick: this.show('showCloseCase', true),
                icon: 'power-off',
                visible: this.state.tbcase.state !== 'CLOSED'
            },
            {
                title: __('cases.move'),
                onClick: this.show('showMoveCase', true),
                icon: 'exchange',
                visible: !this.state.tbcase.transferring && this.state.tbcase.state !== 'CLOSED'
            },
            {
                title: __('cases.suspect.followup'),
                icon: 'user-md',
                visible: this.state.tbcase.diagnosisType === 'SUSPECT' && this.state.tbcase.state !== 'CLOSED',
                submenu: [
                    {
                        title: __('CaseClassification.TB.confirmed'),
                        onClick: () => this.setState({ showSuspectFollowUp: true, suspectFUCla: 'TB' })
                    },
                    {
                        title: __('CaseClassification.DRTB.confirmed'),
                        onClick: () => this.setState({ showSuspectFollowUp: true, suspectFUCla: 'DRTB' })
                    },
                    {
                        title: __('cases.suspect.nottb'),
                        onClick: () => this.setState({ showSuspectFollowUp: true, suspectFUCla: 'NOT_TB' })
                    }
                ]
            }];

        return (
            <div>
                <PatientPanel tbcase={tbcase} />
                <Grid fluid>
                    <Row className="mtop">
                        <Col sm={3}>
                            <CommandBar commands={commands} />
                            <Card className="mtop" title={__('admin.tags')} headerRight={tagBtn}>
                            {
                                this.tagsRender()
                            }
                            </Card>
                            <OtherCases tbcase={tbcase} />
                        </Col>
                        <Col sm={9}>
                            {this.renderTransferMessage()}
                            {tabs}
                            {seltab === 0 && <CaseData tbcase={tbcase} />}
                            {seltab === 1 && <CaseExams tbcase={tbcase} />}
                            {seltab === 2 && <CaseTreatment tbcase={tbcase} />}
                            {seltab === 3 && <CaseIssues tbcase={tbcase} />}
                        </Col>
                    </Row>
                </Grid>

                <CaseEditForm tbcase={tbcase} />

                <CaseClose show={this.state.showCloseCase} onClose={this.show('showCloseCase', false)} tbcase={tbcase}/>

                <CaseMove show={this.state.showMoveCase} onClose={this.show('showMoveCase', false)} tbcase={tbcase}/>

                <CaseTags show={this.state.showTagEdt} onClose={this.show('showTagEdt', false)} tbcase={tbcase}/>

                <SuspectFollowUp
                    show={this.state.showSuspectFollowUp}
                    onClose={this.closeSuspectFU}
                    tbcase={tbcase}
                    classification={this.state.suspectFUCla}/>
            </div>
        );
    }
}

export default observer(Details, Events.caseUpdate);

Details.propTypes = {
    route: React.PropTypes.object
};
