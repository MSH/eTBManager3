
import React from 'react';
import { Input, Row, Col } from 'react-bootstrap';
import Form from '../../../forms/form';
import { TreeView } from '../../../components';

class PermissionTree extends React.Component {

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
		this.nodeRender = this.nodeRender.bind(this);
		this.checkboxClick = this.checkboxClick.bind(this);
		this.getNodes = this.getNodes.bind(this);
	}

	static getServerRequest() {
		return { cmd: 'perms-tree' };
	}

	onChange() {
		const value = this.refs.input.getChecked();

		this.props.onChange({ schema: this.props.schema, value: value });
	}

	getNodes(item) {
		return this.adjustNodes(item.children);
	}

	/**
	 * Adjust nodes to include a property that indicates
	 * @param  {[type]} lst [description]
	 * @return {[type]}     [description]
	 */
	adjustNodes(lst) {
		if (!lst) {
			return null;
		}

		const vals = this.props.value ? this.props.value : [];
		// includes an extra property that indicates the state of the node (user value)
		lst.forEach(p => p.value = vals.find(item => item.permission === p.id));
		return lst;
	}

	nodeInfo(item) {
		return { leaf: !item.children, expanded: !!item.value };
	}

	checkboxClick(item) {
		const self = this;
		return (evt) => {
			let values = this.props.value ? this.props.value : [];

			const checked = evt.target.checked;
			if (checked) {
				// includ a new item in te list
				item.value = { permission: item.id, canChange: false };
				values.push(item.value);
				// create a copy of the array
				values = values.slice(0);
			}
			else {
				// remove a item of the list
				self.removeChildren(item, values);
			}

			console.log(values);
			self.props.onChange({ value: values, schema: self.props.schema });
			const tree = self.refs.tree;
			if (checked) {
				tree.expand(item);
			}
			else {
				tree.collapse(item);
			}
		};
	}

	/**
	 * Remove selected children of the given node (called when unchecked)
	 * @param  {[type]} item [description]
	 * @param  {[type]} lst  [description]
	 * @return {[type]}      [description]
	 */
	removeChildren(item, lst) {
		const index = lst.indexOf(item.value);
		if (index === -1) {
			return;
		}

		delete item.value;
		lst.splice(index, 1);

		if (item.children) {
			item.children.forEach(c => {
				if (c.value) {
					this.removeChildren(c, lst);
				}
			});
		}
	}

	/**
	 * Render the node content
	 * @param  {[type]} item [description]
	 * @return {[type]}      [description]
	 */
	nodeRender(item) {
		return (
			<Row key={item.id} className="tbl-cell">
				<Col sm={7}>
					<div className="checkbox" style={{ margin: '4px 0 0 0' }}>
						<label>
							<input type="checkbox" checked={!!item.value}
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

	render() {
		const title = (
			<Row key="title" className="title">
				<Col sm={7}>{'Permissions'}</Col>
				<Col sm={5}>{'Can modify ?'}</Col>
			</Row>
			);

		return	(
			<TreeView ref="tree"
				root={this.adjustNodes(this.props.resources)}
				onGetNodes={this.getNodes}
				nodeRender={this.nodeRender}
				nodeInfo={this.nodeInfo}
				iconLeaf="angle-right"
				title={title}
				/>
			);
	}
}


PermissionTree.options = { };


PermissionTree.propTypes = {
	value: React.PropTypes.array,
	schema: React.PropTypes.object,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	resources: React.PropTypes.array
};

export default Form.typeWrapper(PermissionTree);
