
import React from 'react';
import { Row, Col, DropdownButton, MenuItem, Button } from 'react-bootstrap';
import { Card } from '../../components/index';
import CRUD from '../../commons/crud';
import TreeView from '../../components/tree-view';
import { app } from '../../core/app';
import { hasPerm } from '../../core/session';


const crud = new CRUD('adminunit');

/**
 * The page controller of the public module
 */
export class AdmUnits extends React.Component {

	constructor(props) {
		super(props);
		this.loadNodes = this.loadNodes.bind(this);
		this.nodeInfo = this.nodeInfo.bind(this);
		this.nodeWrapper = this.nodeWrapper.bind(this);

		const session = app.getState().session;
		this.root = { name: session.workspaceName, id: session.workspaceId, level: 0 };
	}

	renderNode(item) {
		return item.name;
	}

	nodeInfo(item) {
		if (item === this.root) {
			return { leaf: false, expanded: true };
		}
		return { leaf: item.unitsCount === 0 };
	}

	nodeWrapper(content, item) {
		let btn;

		// has permission to edit
		if (hasPerm(this.props.route.data.perm + '_EDT')) {
			btn = item === this.root ?
				<Button bsSize="small" pullRight>{__('action.add') + ' ' + this.csname(1)}</Button> :
				<DropdownButton id="optMenu" bsSize="small" pullRight
					onSelect={this.menuClick}
					title={<span className="hidden-xs" >{__('form.options')}</span>}>
						<MenuItem key="edit" eventKey="edit">
								{__('action.edit')}
						</MenuItem>
						<MenuItem key="delete" eventKey="delete">
								{__('action.delete')}
						</MenuItem>
						{
							item.level < this.state.maxlevel &&
							<MenuItem key="add" eventKey="add">
								{__('action.add') + ' ' + this.csname(item.level + 1)}
							</MenuItem>
						}
				</DropdownButton>;
		}
		else {
			btn = null;
		}

		return (
			<Row key={item.name}>
				<div className="tbl-cell">
					<Col xs={7}>{content}</Col>
					<Col xs={3}>{item.csName}</Col>
					<Col xs={2}>
						{btn}
					</Col>
				</div>
			</Row>
			);
	}


	/**
	 * Return the name of the country structure division in the given level
	 * @param  {[type]} level [description]
	 * @return {[type]}       [description]
	 */
	csname(level) {
		if (!this.state || !this.state.cslist) {
			return null;
		}

		const name = this.state.cslist
			.filter(item => item.level === level)
			.map(item => item.name)
			.join(', ');
		return name;
	}

	loadNodes(parent) {
		const qry = parent !== this.root ? { parentId: parent.id } : { rootUnits: true };

		if (!this.state || !this.state.cslist) {
			qry.fetchCountryStructure = true;
		}

		const self = this;
		return crud.query(qry)
			.then(res => {
				res.list.forEach(item => item.level = parent.level + 1);

				if (res.csList) {
					let maxlevel = 0;
					res.csList.forEach(item => {
						if (item.level > maxlevel) {
							maxlevel = item.level;
						}
					});
					self.setState({ cslist: res.csList, maxlevel: maxlevel });
				}
				return res.list;
			});
	}


	render() {
		// display the titles
		const title = (
			<Row key="title" className="title">
				<div style={{ textWeight: 'bold' }}>
				<Col xs={7}>{__('form.name')}</Col>
				<Col xs={3}>{__('global.location')}</Col>
				</div>
			</Row>
			);

		// render the view
		return (
			<Card title={__('admin.adminunits')}>
				<TreeView onGetNodes={this.loadNodes}
					root={[this.root]}
					innerNode={this.renderNode}
					outerNode={this.nodeWrapper}
					nodeInfo={this.nodeInfo}
					title={title}
				/>
			</Card>
			);
	}
}

AdmUnits.propTypes = {
	route: React.PropTypes.object
};
