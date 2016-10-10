import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { TreeView, Card, Fa, WaitIcon } from '../../../components';
import SessionUtils from '../../session-utils';
import { server } from '../../../commons/server';


/**
 * Display a card with a consolidated number of cases by administrative units
 * and units, grouped by suspected and confirmed TB, DR-TB and NTM
 */
export default class CasesDistribution extends React.Component {

    constructor(props) {
        super(props);
        this.nodeRender = this.nodeRender.bind(this);
    }

    componentWillMount() {
        const auId = this.props.scope === 'ADMINUNIT' ?
            this.props.route.queryParam('id') : null;

        const self = this;

        this.fetchView(auId)
        .then(res => self.setState({ root: res.places }));

        this.setState({ root: null });
    }


    fetchView(adminUnitId) {
        const params = adminUnitId ? '/adminunit?adminUnitId=' + adminUnitId : '';
        return server.post('/api/cases/view' + params);
    }

    getNodes(parent) {
        return server.post('/api/cases/view/places?parentId=' + parent.id);
    }

    nodeRender(node) {
        const hash = node.type === 'UNIT' ?
            SessionUtils.unitHash(node.id, '/cases') :
            SessionUtils.adminUnitHash(node.id, '/cases');

        return (
            <a href={hash} className="lnk-label">{node.name}</a>
            );
    }

    outerRender(content, node) {
        function addRow(value) {
            return <Col xs={2} className="text-right">{value ? value : '-'}</Col>;
        }

        return (
            <Row key={node.id} className="tbl-cell">
                <Col xs={4}>{content}</Col>
                {addRow(node.suspectCount)}
                {addRow(node.tbCount)}
                {addRow(node.drtbCount)}
                {addRow(node.ntmCount)}
            </Row>
            );
    }

    nodeInfo(node) {
        return { leaf: !node.hasChildren };
    }

    iconLeaf(node) {
        const icon = node.type === 'UNIT' ? 'hospital-o' : 'circle-thin';
        return <Fa icon={icon} size={1.4} className="text-muted" />;
    }

    titles() {
        return (
            <Row key="title" className="tbl-title">
                <Col xs={4}>{__('cases.title.places')}</Col>
                <Col xs={2} className="text-right">{__('cases.suspects')}</Col>
                <Col xs={2} className="text-right">{__('cases.title.tbcases')}</Col>
                <Col xs={2} className="text-right">{__('cases.title.drtbcases')}</Col>
                <Col xs={2} className="text-right">{__('cases.title.ntmcases')}</Col>
            </Row>
            );
    }

    render() {
        const root = this.state.root;

        if (!root) {
            return <WaitIcon type="card"/>;
        }

        return (
            <Card title={__('cases.distrib.places')}>
                <Grid fluid>
                    <TreeView root={root}
                        title={this.titles()}
                        iconSize={1.4}
                        onGetNodes={this.getNodes}
                        innerRender={this.nodeRender}
                        outerRender={this.outerRender}
                        iconLeaf={this.iconLeaf}
                        nodeInfo={this.nodeInfo} />
                </Grid>
            </Card>
            );
    }
}

CasesDistribution.propTypes = {
    scope: React.PropTypes.string,
    route: React.PropTypes.object
};
