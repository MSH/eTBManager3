import React from 'react';
import { Alert, Row, Col } from 'react-bootstrap';
import { Profile, WaitIcon, ReactTable } from '../../../components';

import U from '../../session-utils';

import CrudPagination from '../crud/crud-pagination';
import CrudCounter from '../crud/crud-counter';


/**
 * Standard displaying of a list of cases inside a react table, with pagination support
 */
export default class CasesList extends React.Component {

    constructor(props) {
        super(props);
        this._caseClick = this._caseClick.bind(this);
    }

    componentWillMount() {
        this._registerListener(this.props.controller);
    }

    componentWillReceiveProps(nextProps) {
        if (this.props.controller !== nextProps.controller) {
            this._removeListener();
            this._registerListener(nextProps.controller);
        }
    }

    componentWillUnmount() {
        this._removeListener();
    }

    _registerListener(controller) {
        const self = this;
        const handler = controller.on(evt => {
            if (evt === 'list' || evt === 'fetching-list') {
                self.forceUpdate();
            }
        });
        this.setState({ handler: handler });
    }

    _removeListener() {
        // remove registration
        this.props.controller.removeListener(this.state.handler);
    }

    /**
     * Called when user clicks on a case
     */
    _caseClick(item) {
        if (this.props.onCaseClick) {
            this.props.onCaseClick(item);
        }
    }

    render() {
        const controller = this.props.controller;

        if (controller.isFetching()) {
            return <WaitIcon type="card" />;
        }

        return (
            <div>
                {
                    // no results found
                    !controller.isFetching() && (!controller.getList() || controller.getList().length < 1) ?
                        <Alert bsStyle="warning">{__('form.norecordfound')}</Alert> : null
                }
                {
                    // show case list
                    !controller.isFetching() && controller.getList() && controller.getList().length > 0 ?
                    <span>
                        <Row>
                            <Col sm={6}>
                                <CrudCounter controller={controller} className="mtop-=2x text-muted"/>
                            </Col>
                            <Col sm={6}>
                                <CrudPagination controller={controller} showCounter className="pull-right" />
                            </Col>
                        </Row>
                        <ReactTable className="mtop-2x"
                            columns={[
                                {
                                    title: 'Patient',
                                    size: { sm: 4 },
                                    content: item =>
                                        <Profile type={item.gender.toLowerCase()} size="small"
                                            title={U.nameDisplay(item.name)} subtitle={item.caseCode} />
                                },
                                {
                                    title: 'Case Info',
                                    size: { sm: 2 },
                                    content: item =>
                                        <div>{U.classifDisplay(item.classification, item.diagnosisType)}
                                            <br/><div className="sub-text">{U.caseStateDisplay(item.state)}</div>
                                        </div>
                                },
                                {
                                    title: 'Unit',
                                    size: { sm: 6 },
                                    content: item =>
                                        <div>{item.unit.name}<br/>
                                            <div className="sub-text">
                                                {U.adminUnitDisplay(item.unit.adminUnit, false, true)}
                                            </div>
                                        </div>
                                }
                            ]} values={controller.getList()} onClick={this._caseClick}/>

                        <CrudPagination controller={this.props.controller} showCounter className="mtop" />
                    </span> : null
                }
            </div>
        );
    }
}

CasesList.propTypes = {
    controller: React.PropTypes.object,
    query: React.PropTypes.object,
    onCaseClick: React.PropTypes.func
};
