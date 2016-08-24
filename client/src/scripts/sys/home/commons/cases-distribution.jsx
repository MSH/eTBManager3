import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { TreeView, Card, Fa } from '../../../components';
import SessionUtils from '../../session-utils';


/**
 * Display a card with a consolidated number of cases by administrative units
 * and units, grouped by suspected and confirmed TB, DR-TB and NTM
 */
export default class CasesDistribution extends React.Component {

	constructor(props) {
		super(props);
		this.nodeRender = this.nodeRender.bind(this);
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
		const root = this.props.root;

		if (!this.props.root) {
			return null;
		}

		return (
			<Card title={__('cases.distrib.places')}>
				<Grid fluid>
					<TreeView root={root}
						title={this.titles()}
						iconSize={1.4}
						onGetNodes={this.props.onGetChildren}
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
	root: React.PropTypes.array,
	onGetChildren: React.PropTypes.func
};
