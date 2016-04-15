/**
 * Base component to generate tree view in a vertical row. It basically
 * controls nodes and its state (collapsed and expanded), loading nodes in an
 * asynchronous way (using promises) and gives the parent
 * the role of rendering the row.
 */

import React from 'react';
import Fa from './fa';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';

import './tree-view.less';

export default class TreeView extends React.Component {

	constructor(props) {
		super(props);
		this.nodeClick = this.nodeClick.bind(this);

		if (props.root) {
			this.state = { root: this.createNodes(null, props.root) };
		}
		else {
			this.state = {};
		}
	}

	componentWillMount() {
		if (!this.props.root) {
			const self = this;
			this.loadNodes()
				.then(res => self.setState({ root: res }));
		}
	}

	componentDidMount() {
		this.mounted = true;
		if (this.props.onInit) {
			this.props.onInit(this.createHandler());
		}
	}

	componentWillUnmount() {
		this.mounted = false;
	}

	expand(item) {
		const node = this.findNode(item);
		this._expandNode(node);
	}

	collapse(item) {
		const node = this.findNode(item);
		this._collapseNode(node);
	}

	/**
	 * Create a handler that will be sent to the parent to have control over the items
	 * @return {[type]} [description]
	 */
	createHandler() {
		const self = this;

		return {
			self: self,
			/**
			 * Add a new node to the tree
			 * @param  {[type]} parent Object representing the parent node
			 * @param  {[type]} node   Object representing the new child
			 */
			addNode: (parent, item) => {
				const pnode = parent ? self.findNode(parent) : null;
				const cnode = self.createNode(pnode, item);

				// children are loaded ?
				if (pnode && !pnode.leaf && !pnode.children) {
					// if no, just load them and refresh it
					self._expandNode(pnode);
					return;
				}

				// check if children are initialized
				if (pnode && !pnode.children) {
					pnode.children = [];
					pnode.state = 'expanded';
					pnode.leaf = false;
				}

				const children = pnode ? pnode.children : self.getRoots();
				children.push(cnode);
				self.forceUpdate();
			},

			/**
			 * Remove a node
			 * @param  {[type]} item [description]
			 * @return {[type]}      [description]
			 */
			remNode: item => {
				const node = self.findNode(item);
				const lst = node.parent ? node.parent.children : this.getRoots();
				const index = lst.indexOf(node);
				lst.splice(index, 1);
				self.forceUpdate();
			},

			/**
			 * Update a node
			 * @param  {[type]} item [description]
			 * @return {[type]}      [description]
			 */
			updateNode: (olditem, newitem) => {
				const node = self.findNode(olditem);
				const newnode = self.createNode(node.parent, newitem);
				// preserve the current state
				newnode.state = node.state;
				newnode.children = node.children;
				// get the parent list
				const lst = node.parent ? node.parent.children : this.getRoots();
				// replace in the list
				const index = lst.indexOf(node);
				lst.splice(index, 1, newnode);
				// refresh tree view
				self.forceUpdate();
			},

			/**
			 * Expand a node, passing the item as argument
			 * @param  {[type]} item [description]
			 * @return {[type]}      [description]
			 */
			expand: item => {
				const node = self.findNode(item);
				self._expandNode(node);
			},

			/**
			 * Collapse a node, passing the item as argument
			 * @param  {[type]} item [description]
			 * @return {[type]}      [description]
			 */
			collapse: item => {
				const node = self.findNode(item);
				self._expandNode(node);
			}
		};
	}


	/**
	 * Search a node by its data. It travesses the whole tree searching for the node
	 * @param  {[type]} data  The data representing the node
	 * @param  {[type]} nodes The list of nodes
	 * @return {[type]}       The node or null if no node was found
	 */
	findNode(data, nodes) {
		const lst = nodes ? nodes : this.getRoots();
		for (var i = 0; i < lst.length; i++) {
			const n = lst[i];
			if (n.item === data) {
				return n;
			}

			if (n.children) {
				const res = this.findNode(data, n.children);
				if (res) {
					return res;
				}
			}
		}
		return null;
	}

	/**
	 * Load the nodes of the tree
	 * @param  {parent} parent The parent node to load items into
	 */
	loadNodes(parent) {
		const func = this.props.onGetNodes;

		if (!func) {
			return null;
		}

		const pitem = parent ? parent.item : undefined;
		let res = func(pitem);

		// no nodes, then return an empty list
		if (!res) {
			return Promise.resolve([]);
		}

		// is not a promise ?
		if (!res.then) {
			// force node resolution by promises
			res = Promise.resolve(res);
		}

		// create nodes wrapper when nodes are resolved
		const self = this;
		return res.then(items => {
			const nodes = self.createNodes(parent, items);
			return nodes;
		});
	}

	/**
	 * Create nodes from the list of items to add in the tree
	 * @param  {[type]} items [description]
	 * @return {[type]}       [description]
	 */
	createNodes(parent, items) {
		const self = this;
		return items.map(item => self.createNode(parent, item));
	}

	/**
	 * Create a node object from the data representing the node
	 * @param  {[type]} item [description]
	 * @return {[type]}      [description]
	 */
	createNode(parent, item) {
		const info = this.props.nodeInfo ? this.props.nodeInfo(item) : { leaf: false, expanded: false };

		const node = { item: item,
			parent: parent,
			state: 'collapsed',
			children: null,
			leaf: info.leaf };

		if (info.expanded) {
			this._expandNode(node);
		}
		return node;
	}


	/**
	 * Create the React components of the nodes to be displayed in the tree
	 * @param  {[type]} nodes [description]
	 * @return {[type]}       [description]
	 */
	createNodesView() {
		const self = this;
		// get the item being expanded (for animation)
		const expitem = this.state && this.state.expitem;

		// recursive function to create the expanded tree in a list
		const mountList = function(nlist, level, parentkey) {
			let count = 0;
			const lst = [];

			// is the root being rendered
			if (!parentkey && self.props.title) {
				lst.push(self.props.title);
			}

			nlist.forEach(node => {
				const key = (parentkey ? parentkey + '.' : '') + count;
				const row = self.createNodeRow(node, level, key);

				lst.push(row);
				if (node.state !== 'collapsed' && !node.leaf && node.children) {
					lst.push(mountList(node.children, level + 1, key));
				}
				count++;
			});

			// the children div key
			const divkey = (parentkey ? parentkey : '') + 'ch';

			// children are inside a div, in order to animate collapsing/expanding
			return (
				<ReactCSSTransitionGroup key={divkey + 'trans'} transitionName="node"
					transitionLeaveTimeout={250} transitionEnterTimeout={250} >
					{lst}
				</ReactCSSTransitionGroup>
				);
		};

		return mountList(this.getRoots(), 0, false);
	}

	/**
	 * Called to resolve the icon that will be used beside the node name
	 * @param  {[type]} node [description]
	 * @return {[type]}      [description]
	 */
	resolveIcon(node) {
		const p = this.props;

		let waiting = false;
		let icon;
		if (node.leaf) {
			icon = p.iconLeaf;
		}
		else {
			switch (node.state) {
				case 'expanding': icon = p.iconWait;
					waiting = true;
					break;
				case 'collapsed': icon = p.iconPlus;
					break;
				default: icon = p.iconMinus;
					break;
			}
		}

		if (typeof icon === 'function') {
			icon = icon(node.item);
		}

		if (typeof icon === 'string') {
			icon = <Fa icon={icon} size={p.size} spin={waiting} />;
		}

		return icon;
	}

	/**
	 * Create node row containing the content of the node
	 * @param  {[type]} node  [description]
	 * @param  {[type]} level [description]
	 * @param  {[type]} key   [description]
	 * @return {[type]}       [description]
	 */
	createNodeRow(node, level, key) {
		// is there any function to render the whole node ?
		if (this.props.nodeRender) {
			const cont = this.props.nodeRender(node.item);
			return this.wrapRow(cont, null, level, key);
		}

		const p = this.props;

		// get the node content
		const innerRender = p.innerRender;

		const content = innerRender ? innerRender(node.item) : node.item;

		const icon = this.resolveIcon(node);

		// the content
		const nodeIcon = node.leaf || node.expanding ?
			icon :
			<a className="node-link" onClick={this.nodeClick} data-item={key}>
				{icon}
			</a>;

		const nodeRow = this.wrapRow(content, nodeIcon, level, key);

		return p.outerRender ? p.outerRender(nodeRow, node.item) : nodeRow;
	}

	wrapRow(content, nodeIcon, level, key) {
		return (
			<div key={key} className="node" style={{ marginLeft: (level * this.props.indent) + 'px' }}>
			{nodeIcon}
			{content}
			</div>
			);
	}

	/**
	 * Called when user clicks on the plus/minus icon
	 * @param  {[type]} evt [description]
	 * @return {[type]}     [description]
	 */
	nodeClick(evt) {
		const key = evt.currentTarget.getAttribute('data-item');

		let lst = this.state.root;
		let node = null;

		key.split('.').forEach(index => {
			node = lst[Number(index)];
			lst = node.children;
		});

		if (node.state === 'collapsed') {
			this._expandNode(node);
		}
		else {
			this._collapseNode(node);
		}
	}

	/**
	 * Expand a given node
	 * @param  {[type]} node [description]
	 * @return {[type]}      [description]
	 */
	_expandNode(node) {
		// children are not loaded ?
		if (!node.children) {
			// node enters in the expanding state
			node.state = 'expanding';
			this.refreshTree();

			// load the children
			const self = this;
			this.loadNodes(node)
				.then(res => {
					node.state = 'expanded';
					node.children = res;
					// force tree to show the new expanded node
					self.refreshTree();
				});
		}
		else {
			node.state = 'expanded';
			// force tree to show the new expanded node
			this.refreshTree();
		}
	}

	/**
	 * Collapse a given node and refresh the tree
	 * @param  {[type]} node [description]
	 * @return {[type]}      [description]
	 */
	_collapseNode(node) {
		node.state = 'collapsed';
		this.forceUpdate();
	}

	refreshTree() {
		if (this.mounted) {
			this.forceUpdate();
		}
	}

	/**
	 * Return the root list of nodes
	 * @return {array} Array of objects with information about the nodes
	 */
	getRoots() {
		return this.state ? this.state.root : null;
	}

	/**
	 * Render the tree
	 * @return {[type]} [description]
	 */
	render() {
		const root = this.getRoots();

		if (!root) {
			return null;
		}

		return <div className="tree-view">{this.createNodesView(root)}</div>;
	}
}

TreeView.propTypes = {
	// array containing the items to be displayed
	root: React.PropTypes.array,
	// called to load the nodes in the format function(item): promise
	onGetNodes: React.PropTypes.func,
	// called to render the div area that will host the node content
	// in the format function(item): string | React component
	innerRender: React.PropTypes.func,
	outerRender: React.PropTypes.func,
	// called to render the whole node. func(nodeStateFunc, item)
	nodeRender: React.PropTypes.func,
	// an optional title to be displayed on the top of the treeview
	title: React.PropTypes.any,
	// opitional. Check if node has children or is a leaf node
	nodeInfo: React.PropTypes.func,
	// replace the default icons
	iconPlus: React.PropTypes.any,
	iconMinus: React.PropTypes.any,
	iconLeaf: React.PropTypes.any,
	iconWait: React.PropTypes.any,
	iconSize: React.PropTypes.number,
	// the indentation of each node level, in pixels
	indent: React.PropTypes.number,
	// called when tree is mounted in order to parent interact with tree
	onInit: React.PropTypes.func
};

TreeView.defaultProps = {
	iconPlus: 'plus-square-o',
	iconMinus: 'minus-square-o',
	iconLeaf: 'circle-thin',
	iconWait: 'circle-o-notch',
	iconSize: 1,
	indent: 16
};
