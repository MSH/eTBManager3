
import React from 'react';
import { Input, Row, Col } from 'react-bootstrap';
import Form from '../../../forms/form';
import { TreeView } from '../../../components';

class PermissionTree extends React.Component {

	static getServerRequest() {
		return { cmd: 'perms-tree' };
	}

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
		this.nodeRender = this.nodeRender.bind(this);
		this.checkboxClick = this.checkboxClick.bind(this);
	}

	onChange() {
		const value = this.refs.input.getChecked();

		this.props.onChange({ schema: this.props.schema, value: value });
	}

	getNodes(item) {
		return item.children;
	}

	nodeInfo(item) {
		return { leaf: !item.children };
	}

	checkboxClick(item) {
		const self = this;
		return (evt) => {
			const checked = evt.target.checked;
			const tree = self.refs.tree;
			if (checked) {
				tree.expand(item);
			}
			else {
				tree.collapse(item);
			}
		};
	}

	nodeRender(item) {
		return (
			<Row key={item.id}>
				<Col sm={7}>
					<div className="checkbox" style={{ margin: '4px 0 0 0' }}>
						<label><input type="checkbox" onChange={this.checkboxClick(item)}/>{item.name}</label>
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
			<Row key="title">
				<Col sm={7}>{'Permissions'}</Col>
				<Col sm={5}>{'Can modify ?'}</Col>
			</Row>
			);

		return	(
			<TreeView ref="tree"
				root={this.props.resources} onGetNodes={this.getNodes}
				innerRender={this.innerRender}
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
