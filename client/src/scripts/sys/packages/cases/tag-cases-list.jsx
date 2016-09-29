import React from 'react';
import { Alert, Row, Col } from 'react-bootstrap';
import { Card, Profile, WaitIcon, ReactTable } from '../../../components';
import { app } from '../../../core/app';

import SessionUtils from '../../session-utils';

import CrudPagination from '../../packages/crud/crud-pagination';
import CrudCounter from '../../packages/crud/crud-counter';
import CrudController from '../../packages/crud/crud-controller';
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
        const handler = this.state.controller.on(evt => {
            self.eventHandler(evt);
        });
        this.setState({ handler: handler });

        this.loadList();
    }

    componentWillReceiveProps(nextProps) {
        const id = nextProps.route.queryParam('id');
        const tag = nextProps.route.queryParam('tagId');
        const changed = id !== this.state.id || tag !== this.state.tagId;

        if (changed) {
            this.loadList();
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

    loadList() {
        const self = this;
        const scope = this.props.scope;
        const id = this.props.route.queryParam('id');
        const tagId = this.props.route.queryParam('tag');

        const qry = {
            unitId: scope === 'UNIT' ? id : null,
            adminUnitId: scope === 'ADMINUNIT' ? id : null,
            tagId: tagId
        };

        this.state.controller.initList(qry).then(() => self.setState({ id: id, tagId: tagId }));
    }

    getClassificatonLabel(classification, diagnosistype) {
        const lists = app.getState().app.lists;
        const list = lists['CaseClassification' + classification.id];
        return list[diagnosistype.id];
    }

    render() {
        const controller = this.state.controller;

        if (controller.isFetching()) {
            return <WaitIcon type="card" />;
        }

        const res = controller.getServerResult();
        const tag = res ? res.tag : {};

        const type = 'prof-tag-' + tag.type.toLowerCase();
        const header = (
            <Profile
                imgClass={type}
                fa="tag" title={tag.name}
                size="small"
                />
            );

        return (
            <Card header={header}>
                {
                    // no results found
                    (!controller.getList() || controller.getList().length < 1) ?
                        <Alert bsStyle="warning">{__('form.norecordfound')}</Alert> : null
                }
                {
                    // show case list
                    controller.getList() && controller.getList().length > 0 ?
                    <span>
                        <Row>
                            <Col sm={6}>
                                <CrudCounter controller={controller} className="mtop-2x text-muted" />
                            </Col>
                            <Col sm={6}>
                                <CrudPagination controller={this.state.controller} showCounter className="pull-right"/>
                            </Col>
                        </Row>
                        <ReactTable
                            columns={[
                                {
                                    title: 'Patient',
                                    size: { sm: 4 },
                                    content: item =>
                                        <Profile type={item.gender.toLowerCase()} size="small"
                                            title={SessionUtils.nameDisplay(item.name)} subtitle={item.caseCode} />
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
                                            <div className="sub-text">{SessionUtils.adminUnitDisplay(item.ownerUnit.adminUnit, false, true)}</div></div>
                                }
                            ]} values={controller.getList()} onClick={this.caseClick}/>

                        <Row>
                            <Col sm={12}>
                                <CrudPagination controller={this.state.controller} showCounter className="pull-right" />
                            </Col>
                        </Row>
                    </span> : null
                }
            </Card>
            );
    }
}

TagCasesList.propTypes = {
    route: React.PropTypes.object.isRequired,
    scope: React.PropTypes.string.isRequired
};
