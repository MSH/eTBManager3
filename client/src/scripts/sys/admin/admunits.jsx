
import React from 'react';
import { Row, Col, DropdownButton, MenuItem } from 'react-bootstrap';
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
		return false;
	}

	nodeWrapper(content, item) {
		console.log(item);
		return (
			<Row key={item.name}>
				<div className="node-row">
					<Col xs={7}>{content}</Col>
					<Col xs={3}>{item.csName}</Col>
					<Col xs={2}>
						<DropdownButton id="optMenu" bsSize="small" pullRight
							onSelect={this.menuClick} style={{ margin: '2px' }}
							title={<span className="hidden-xs" >{__('form.options')}</span>}>
								<MenuItem key="edit" eventKey="edit">
										{__('action.edit')}
								</MenuItem>
								<MenuItem key="delete" eventKey="delete">
										{__('action.delete')}
								</MenuItem>
						</DropdownButton>
					</Col>
				</div>
			</Row>
			);
	}

	// title() {
	// 	return (
	// 		<Row key="title">
	// 			<div style={{ border: '1px solid #f0f0f0', textWeight: 'bold' }}>
	// 			<Col xs={6}>{'Name'}</Col>
	// 			<Col xs={2}>{'Title 1'}</Col>
	// 			<Col xs={2}>{'Title 2'}</Col>
	// 			<Col xs={2}>{'Title 3'}</Col>
	// 			</div>
	// 		</Row>
	// 		);
	// }

	// leafIcon(item) {
	// 	const icons = ['bell', 'book', 'bus', 'camera'];
	// 	const index = Math.floor(Math.random() * 4);
	// 	const iname = icons[index];

	// 	return <Fa icon={iname} className="text-success" />;
	// }

	loadNodes(parent) {
		const qry = parent ? { parentId: parent.id } : { rootUnits: true };
		return crud.query(qry)
			.then(res => res.list);
		// const level = parent ? parent.level : 0;
		// const lst = [];
		// const size = Math.round(Math.random() * 10 + 2);
		// for (var i = 1; i < size; i++) {
		// 	const name = parent ? parent.name + '.' + i : 'Item ' + i;
		// 	lst.push({ name: name, level: level + 1 });
		// }
		// return lst;
	}

	render() {
		// display the titles
		const title = (
			<Row key="title" className="title">
				<div style={{ border: '1px solid #f0f0f0', textWeight: 'bold' }}>
				<Col xs={7}>{__('form.name')}</Col>
				<Col xs={3}>{__('global.location')}</Col>
				</div>
			</Row>
			);

		// render the view
		return (
			<Card title={__('admin.adminunits')}>
				<TreeView onGetNodes={this.loadNodes}
					innerNode={this.renderNode}
					outerNode={this.nodeWrapper}
					checkLeaf={this.checkLeaf}
					iconLeaf=""
					title={title}
				/>
			</Card>
			);
	}
}
