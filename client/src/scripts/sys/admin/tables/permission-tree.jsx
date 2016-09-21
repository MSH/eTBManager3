
import React from 'react';
import { Row, Col } from 'react-bootstrap';
import Form from '../../../forms/form';
import { TreeView, WaitIcon } from '../../../components';

/**
 * A custom control used in the user profile editing page. It displays the permissions
 * in a tree and allows user to change it
 */
class PermissionTree extends React.Component {

    constructor(props) {
        super(props);
        this.nodeRender = this.nodeRender.bind(this);
        this.checkboxClick = this.checkboxClick.bind(this);
        this.getNodes = this.getNodes.bind(this);
        this.nodeInfo = this.nodeInfo.bind(this);
    }

    componentWillMount() {
        this._updateResources(this.props);
    }

    componentWillReceiveProps(newprops) {
        this._updateResources(newprops);
    }

    serverRequest(nextSchema, value, nextResources) {
        return this.props.resources || nextResources ? null : { cmd: 'perms-tree' };
    }

    /**
     * Return the children nodes of the given item
     * @param  {object} item Data associated with the node
     * @return {Array}       Array of object representing the children, or null if there is no children
     */
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
        lst.forEach(p => {
            p.value = vals.find(item => item.permission === p.id);
        });
        return lst;
    }

    nodeInfo(item) {
        return { leaf: !item.children, expanded: item.checked };
    }

    checkboxClick(item) {
        const self = this;
        return () => {
            item.checked = !item.checked;
            // create the list of values
            const vals = self._createValues();

            // get a reference to the tree
            const tree = self.refs.tree;

            if (item.checked) {
                tree.expand(item);
            }
            else {
                tree.collapse(item);
            }

            self.props.onChange({ schema: self.props.schema, value: vals });
        };
    }

    canChangeClick(item) {
        const self = this;

        return () => {
            item.canChange = !item.canChange;
            const vals = self._createValues();

            self.props.onChange({ schema: self.props.schema, value: vals });
        };
    }

    /**
     * Create the list of selected permissions based on the values of the tree
     * @return {[type]} [description]
     */
    _createValues() {
        const self = this;
        const vals = [];
        this.props.resources.forEach(item => self._addSelItem(vals, item));

        return vals;
    }

    /**
     * Check if item is selected, and recursivelly search its children
     * @param {[type]} vals [description]
     * @param {[type]} item [description]
     */
    _addSelItem(vals, item) {
        if (!item.checked) {
            return;
        }

        vals.push({ permission: item.id, canChange: item.canChange });
        if (item.children) {
            const self = this;
            item.children.forEach(c => self._addSelItem(vals, c));
        }
    }

    /**
     * Update the resources based on the state of the values, including information
     * about node selection
     */
    _updateResources(props) {
        // get the resources given from the parent
        const resources = props.resources;

        if (!resources) {
            return;
        }

        const vals = props.value;

        // local recursive function to traverse the tree and set selections
        const traverse = function(item) {
            const v = vals ? vals.find(p => p.permission === item.id) : null;
            item.checked = !!v;
            item.canChange = !!v && v.canChange;

            // browse the children
            if (item.children) {
                item.children.forEach(c => traverse(c));
            }
        };

        resources.forEach(it => traverse(it));
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
        const noBorder = { margin: '4px 0 0 0' };
        return (
            <Row key={item.id} className="tbl-row">
                <Col sm={7}>
                    <div className="checkbox" style={noBorder}>
                        <label>
                            <input type="checkbox"
                                checked={item.checked}
                                onChange={this.checkboxClick(item)}/>
                            {item.name}
                        </label>
                    </div>
                </Col>
                <Col sm={5}>
                    {
                        item.changeable &&
                        <div className="checkbox" style={noBorder}>
                            <label>
                                <input
                                    type="checkbox"
                                    checked={item.canChange}
                                    onChange={this.canChangeClick(item)} />
                                {'Can modify?'}
                            </label>
                        </div>
                    }
                </Col>
            </Row>
            );
    }

    render() {
        const title = (
            <Row key="title">
                <Col sm={12}>{'Permissions'}</Col>
            </Row>
            );

        if (!this.props.resources) {
            return <WaitIcon type="card" />;
        }

        return (
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
