
import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { Card } from '../../components/index';
import CRUD from '../../commons/crud';
import TreeView from '../../components/tree-view';
import Fa from '../../components/fa';

const crud = new CRUD('adminunit');

/**
 * The page controller of the public module
 */
export class AdmUnits extends React.Component {

	renderNode(item) {
		return item.name;
	}

	checkLeaf(item) {
		if (item.level < 2) {
			return false;
		}
		return Math.random() <= 0.5;
	}

	nodeWrapper(content, item) {
		return (
			<Row key={item.name}>
				<div style={{ border: '1px solid #f0f0f0' }}>
				<Col xs={6}>{content}</Col>
				<Col xs={2}>{'Column 2'}</Col>
				<Col xs={2}>{'Column 3'}</Col>
				<Col xs={2}>{'Col 4'}</Col>
				</div>
			</Row>
			);
	}

	title() {
		return (
			<Row key="title">
				<div style={{ border: '1px solid #f0f0f0', textWeight: 'bold' }}>
				<Col xs={6}>{'Name'}</Col>
				<Col xs={2}>{'Title 1'}</Col>
				<Col xs={2}>{'Title 2'}</Col>
				<Col xs={2}>{'Title 3'}</Col>
				</div>
			</Row>
			);
	}

	leafIcon() {
		const icons = ['bell', 'book', 'bus', 'camera'];
		const index = Math.floor(Math.random() * 4);
		const iname = icons[index];

		return <Fa icon={iname} className="text-success" />;
	}

	loadNodes(parent) {
//		const qry = parent ? { parentId: parent.id } : { rootUnits: true };
		const level = parent ? parent.level : 0;
		const lst = [];
		const size = Math.round(Math.random() * 10 + 2);
		for (var i = 1; i < size; i++) {
			const name = parent ? parent.name + '.' + i : 'Item ' + i;
			lst.push({ name: name, level: level + 1 });
		}
		return lst;
		// return crud.query(qry)
		// 	.then(res => res.list);
	}

	render() {

		return (
			<Card title={__('admin.adminunits')}>
				<TreeView onGetNodes={this.loadNodes}
					innerNode={this.renderNode}
					outerNode={this.nodeWrapper}
					checkLeaf={this.checkLeaf}
					iconLeaf={this.leafIcon}
					iconPlus="plus"
					iconMinus={<Fa icon="minus-square" className="text-muted" />}
					indent={10}
					title={this.title()}
				/>
			</Card>
			);
	}
}
