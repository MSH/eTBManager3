
import React from 'react';
import { Card } from '../../components/index';
import CRUD from '../../commons/crud';
import TreeView from '../../components/tree-view';

const crud = new CRUD('adminunit');

/**
 * The page controller of the public module
 */
export class AdmUnits extends React.Component {

	renderNode(item) {
		return item.name;
	}

	checkLeaf(item) {
		return item ? false : false;
	}

	loadNodes(parent) {
//		const qry = parent ? { parentId: parent.id } : { rootUnits: true };
		const lst = [];
		const size = Math.round(Math.random() * 10 + 3);
		for (var i = 1; i < size; i++) {
			const name = parent ? parent.name + '.' + i : 'Item ' + i;
			lst.push({ name: name });
		}
		return lst;
		// return crud.query(qry)
		// 	.then(res => res.list);
	}

	render() {

		return (
			<Card title={__('admin.adminunits')}>
				<TreeView onGetNodes={this.loadNodes}
					onRenderNode={this.renderNode}
					checkLeaf={this.checkLeaf}
				/>
			</Card>
			);
	}
}
