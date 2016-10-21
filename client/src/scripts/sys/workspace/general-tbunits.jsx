import React from 'react';
import { Alert, Row, Col } from 'react-bootstrap';
import { Card, Profile, WaitIcon, ReactGrid } from '../../components';
import Form from '../../forms/form';

import SessionUtils from '../session-utils';

import CrudPagination from '../packages/crud/crud-pagination';
import CrudCounter from '../packages/crud/crud-counter';
import CrudController from '../packages/crud/crud-controller';
import FakeCRUD from '../../commons/fake-crud';

export default class WorkspaceTbUnits extends React.Component {

    constructor(props) {
        super(props);
        this.loadList = this.loadList.bind(this);
        this.eventHandler = this.eventHandler.bind(this);

        // create fake-crud controller
        const unitFakeCRUD = new FakeCRUD('/api/tbl/unit/query');
        const opts = {
            pageSize: 50,
            readOnly: true,
            editorSchema: {},
            refreshAll: false
        };

        const controller = new CrudController(unitFakeCRUD, opts);
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

    componentWillUnmount() {
        // remove registration
        this.state.controller.removeListener(this.state.handler);
    }

    eventHandler(evt) {
        if (evt === 'list' || evt === 'fetching-list') {
            this.forceUpdate();
        }
    }

    loadList() {
        const self = this;
        this.state.controller.initList({ type: 'TBUNIT' }).then(() => self.forceUpdate());
    }

    cellRender(item) {
        const auname = Form.types.adminUnit.controlClass().displayText(item.adminUnit);

        return (
            <Profile type={item.type.toLowerCase()}
                title={item.name}
                subtitle={auname}
                size="small"
                style={{ margin: '5px', padding: '5px' }} />
            );
    }

    onCellClick(item) {
        window.location.hash = SessionUtils.unitHash(item.id, '/cases');
    }

    render() {
        const controller = this.state.controller;

        if (controller.isFetching()) {
            return <WaitIcon type="card" />;
        }

        return (
            <Card title={__('admin.tbunits')}>
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
                        <Row>
                            <Col sm={12} className="mtop-2x">
                                <ReactGrid
                                    values={controller.getList()}
                                    onCollapseRender={this.cellRender}
                                    onReactCellClick={this.onCellClick} />
                            </Col>
                        </Row>
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

WorkspaceTbUnits.propTypes = {
    route: React.PropTypes.object.isRequired
};
