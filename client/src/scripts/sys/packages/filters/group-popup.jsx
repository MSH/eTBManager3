import React from 'react';
import { TreeView } from '../../../components';

/**
 * Display the content of filter or variables in a popup tree box.
 * The root level of the tree displays the groups, while the second level
 * displays the child items.
 */
export default class GroupPopup extends React.Component {

    constructor(props) {
        super(props);
        this.nodeRender = this.nodeRender.bind(this);
        this.itemClick = this.itemClick.bind(this);
        this.groupClick = this.groupClick.bind(this);
        this.nodeInfo = this.nodeInfo.bind(this);
        this.getChildNodes = this.getChildNodes.bind(this);
        this.preventClose = this.preventClose.bind(this);
    }

    /**
     * Called when user clicks on an item (filter or variable)
     */
    itemClick(item) {
        return () => {
            this.props.onSelect(item);
            this._canClose = true;
        };
    }

    /**
     * Called when user clicks on a group
     */
    groupClick(group) {
        return () => {
            const handler = this.refs.tv.createHandler();
            handler.toggle(group);
        };
    }

    /**
     * Render the node of the tree for selection of the children
     */
    nodeRender(item) {
        // check if is a group or an child
        return item.id ?
            <a className="selectable" onClick={this.itemClick(item)}>{item.name}</a> :
            <a className="lnk-label" onClick={this.groupClick(item)}><b style={{ fontSize: '1.2em' }}>{item.label}</b></a>;
    }

    /**
     * Return information about the node to the tree
     */
    nodeInfo(item) {
        return { leaf: !item[this.props.childrenProperty] };
    }

    /**
     * Return the children of the group
     */
    getChildNodes(item) {
        const children = item[this.props.childrenProperty];
        return children ? children : null;
    }

    /**
     * This function is called by the div around the tree view to prevent
     * the event from being propagated to the parent popup
     */
    preventClose(evt) {
        if (!this._canClose) {
            evt.preventDefault();
        }
        this._canClose = false;
    }

    render() {
        return (
            <div className="filter-popup" onClick={this.preventClose}>
                <TreeView ref="tv" root={this.props.groups}
                    innerRender={this.nodeRender}
                    onGetNodes={this.getChildNodes}
                    nodeInfo={this.nodeInfo}
                    iconLeaf=""
                    iconSize={1.3}
                    indent={4}
                />
            </div>
        );
    }
}

GroupPopup.propTypes = {
    // the list of groups to be displayed
    groups: React.PropTypes.array,
    // function to be called when item is selected
    onSelect: React.PropTypes.func.isRequired,
    // the property in the groups that contains the children items
    childrenProperty: React.PropTypes.string.isRequired
};
