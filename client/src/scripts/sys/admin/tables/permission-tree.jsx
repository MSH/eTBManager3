
import React from 'react';
import { Input, Row, Col } from 'react-bootstrap';
import Form from '../../../forms/form';
import { TreeView, WaitIcon } from '../../../components';

/**
 * A custom control used in the user profile editing page. It displays the permissions
 * in a tree and allows user to change it
 */
class PermissionTree extends React.Component {

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
		this.nodeRender = this.nodeRender.bind(this);
		this.checkboxClick = this.checkboxClick.bind(this);
		this.nodeInfo = this.nodeInfo.bind(this);
	}

	serverRequest(nextSchema, value, nextResources) {
		return this.props.resources || nextResources ? null : { cmd: 'perms-tree' };
	}

	onChange() {
		const value = this.refs.input.getChecked();

		this.props.onChange({ schema: this.props.schema, value: value });
	}

	getNodes(item) {
		return item.children;
	}

	nodeInfo(item) {
		const sel = this._isItemSelected(item);
		return { leaf: !item.children, expanded: sel };
	}

	checkboxClick(item) {
		const self = this;
		return () => {
			const vals = self.props.value ? self.props.value.slice(0) : [];

			// get a reference to the tree
			const tree = self.refs.tree;

			const index = vals.findIndex(p => p.permission === item.id);
			if (index === -1) {
				vals.push({ permission: item.id, canChange: false });
				tree.expand(item);
			}
			else {
				vals.splice(index, 1);
				tree.collapse(item);
			}

			self.props.onChange({ schema: self.props.schema, value: vals });
		};
	}

	nodeRender(item) {
		const checked = this._isItemSelected(item);

		return (
			<Row key={item.id} className="tbl-row">
				<Col sm={7}>
					<div className="checkbox" style={{ margin: '4px 0 0 0' }}>
						<label>
							<input type="checkbox"
								checked={checked}
								onChange={this.checkboxClick(item)}/>{item.name}
						</label>
					</div>
				</Col>
				<Col sm={5}>
					{item.changeable &&	<Input type="checkbox"/>}
				</Col>
			</Row>
			);
	}

	_isItemSelected(item) {
		const val = this.props.value;
		return val ? !!val.find(p => p.permission === item.id) : false;
	}

	render() {
		const title = (
			<Row key="title">
				<Col sm={7}>{'Permissions'}</Col>
				<Col sm={5}>{'Can modify ?'}</Col>
			</Row>
			);

		if (!this.props.resources) {
			return <WaitIcon type="card" />;
		}

		return	(
			<div className="tbl-hover form-group">
				<TreeView ref="tree"
					className="tbl-hover form-group"
					root={this.props.resources}
					onGetNodes={this.getNodes}
					innerRender={this.innerRender}
					nodeRender={this.nodeRender}
					nodeInfo={this.nodeInfo}
					iconLeaf="angle-right"
					title={title}
					/>
			</div>
			);
	}
}


PermissionTree.propTypes = {
	value: React.PropTypes.array,
	schema: React.PropTypes.object,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	resources: React.PropTypes.array
};

export default Form.control(PermissionTree);
