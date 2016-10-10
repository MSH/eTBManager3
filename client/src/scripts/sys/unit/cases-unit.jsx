import React from 'react';
import { Nav, NavItem, Alert } from 'react-bootstrap';
import { Card, Profile, WaitIcon, ReactTable } from '../../components';
import { server } from '../../commons/server';
import moment from 'moment';
import SessionUtils from '../session-utils';
import { getOptionName } from '../mock-option-lists';
import TreatProgress from '../case/treat/treat-progress';

/**
 * Display the active cases of the selected unit. The unit ID is in the URL
 */
export default class CasesUnit extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            sel: 0
        };
        this.tabSelect = this.tabSelect.bind(this);
    }

    componentWillMount() {
        this.updateState(this.props);
    }

    componentWillReceiveProps(nextProps) {
        this.updateState(nextProps);
    }

    updateState(props) {
        const newId = props.route.queryParam('id');
        const id = this.state.id;

        if (!id === newId) {
            return;
        }

        const self = this;

        server.post('/api/cases/unit/' + newId)
        .then(res => self.setState({
            cases: res,
            id: newId,
            sel: res.presumptives && res.presumptives.length > 0 ? 0 : 1
        }));

    }

    listCount(lst) {
        const count = lst.length > 0 ? lst.length : '-';
        return <div className="value-big text-success">{count}</div>;
    }

    tbCasesRender() {
        const lst = this.state.cases.tbCases;
        return this.confirmRender(lst);
    }

    drtbCasesRender() {
        const lst = this.state.cases.drtbCases;
        return this.confirmRender(lst);
    }

    confirmRender(lst) {
        if (lst.length === 0) {
            return this.noRecFoundRender();
        }

        return (
            <ReactTable className="mtop-2x"
                columns={[
                    {
                        title: __('Patient'),
                        size: { sm: 4 },
                        content: item =>
                            <Profile type={item.gender.toLowerCase()} size="small"
                                title={SessionUtils.nameDisplay(item.name)} subtitle={item.caseNumber} />
                    },
                    {
                        title: __('TbCase.registrationDate'),
                        size: { sm: 2 },
                        content: item => {
                            const dt = moment(item.registrationDate);
                            return (<div>{dt.format('L')}
                                <div className="sub-text">{dt.fromNow()}</div>
                            </div>);
                        }
                    },
                    {
                        title: __('TbCase.registrationGroup'),
                        size: { sm: 2 },
                        content: item => <div>{getOptionName('registrationGroup', item.registrationGroup)}<br/>{item.infectionSite ? item.infectionSite.name : null}</div>
                    },
                    {
                        title: __('TbCase.iniTreatmentDate'),
                        size: { sm: 2 },
                        content: item => {
                            if (!item.iniTreatmentDate) {
                                return <div>{'-'}</div>;
                            }
                            const dt = moment(item.iniTreatmentDate);
                            return (<div>{dt.format('L')}
                                <div className="sub-text">{dt.fromNow()}</div>
                            </div>);
                        }
                    },
                    {
                        title: __('cases.mantreatment'),
                        size: { sm: 2 },
                        align: 'center',
                        content: i => i.treatmentProgress ? <TreatProgress value={i.treatmentProgress} width={45} height={45}/> : '-'
                    }
                ]} values={lst} onClick={this.caseClick}/>
            );
    }

    /**
     * Return the list of cases to be displayed
     * @return {React.Component} Component displaying the cases
     */
    presumptiveRender() {
        const lst = this.state.cases.presumptives;

        // is there any case to display ?
        if (lst.length === 0) {
            return this.noRecFoundRender();
        }

        return (
            <ReactTable columns={
                [{
                    title: __('Patient'),
                    size: { sm: 4 },
                    content: item =>
                            <Profile type={item.gender.toLowerCase()} size="small"
                                title={SessionUtils.nameDisplay(item.name)} subtitle={item.caseNumber} />
                },
                {
                    title: __('TbCase.registrationDate'),
                    size: { sm: 3 },
                    content: item => {
                        const dt = moment(item.registrationDate);
                        return (<div>{dt.format('L')}
                            <div className="sub-text">{dt.fromNow()}</div>
                        </div>);
                    }
                },
                {
                    title: __('FollowUpType.XPERT'),
                    size: { sm: 2 },
                    content: item => item.xpertResult ? item.xpertResult.name : '-'
                },
                {
                    title: __('FollowUpType.EXAM_MICROSCOPY'),
                    size: { sm: 2 },
                    content: item => item.microscopyResult ? item.microscopyResult.name : '-'
                }
                ]} values={lst} className="mtop-2x" onClick={this.caseClick} />
        );
    }

    /**
     * Return a message displaying 'No record found'
     * @return {[type]} [description]
     */
    noRecFoundRender() {
        return (
            <div className="card-default mtop-2x">
                <div className="card-content">
                    <Alert bsStyle="warning">{__('form.norecordfound')}</Alert>
                </div>
            </div>
            );
    }

    /**
     * Called when user clicks on a case
     * @param  {[type]} id [description]
     * @return {[type]}    [description]
     */
    caseClick(item) {
        window.location.hash = SessionUtils.caseHash(item.id);
    }

    tabSelect(evt) {
        this.setState({ sel: evt });
    }

    render() {
        const cases = this.state.cases;

        if (!cases) {
            return <WaitIcon type="card"/>;
        }

        const tabs = (
            <Nav bsStyle="tabs" activeKey={this.state.sel} justified
                className="app-tabs2"
                onSelect={this.tabSelect}>
                {
                    cases.presumptives.length > 0 &&
                    <NavItem key={0} eventKey={0}>
                        {this.listCount(cases.presumptives)}
                        {__('cases.suspects')}
                    </NavItem>
                }
                <NavItem key={1} eventKey={1}>
                    {this.listCount(cases.tbCases)}
                    {__('cases.tb')}
                </NavItem>
                <NavItem key={2} eventKey={2}>
                    {this.listCount(cases.drtbCases)}
                    {__('cases.drtb')}
                </NavItem>
            </Nav>
            );

        return (
            <Card padding="none">
                {tabs}
                {
                    this.state.sel === 0 ? this.presumptiveRender() : null
                }
                {
                    this.state.sel === 1 ? this.tbCasesRender() : null
                }
                {
                    this.state.sel === 2 ? this.drtbCasesRender() : null
                }
            </Card>
            );
    }
}

CasesUnit.propTypes = {
    route: React.PropTypes.object.isRequired
};
