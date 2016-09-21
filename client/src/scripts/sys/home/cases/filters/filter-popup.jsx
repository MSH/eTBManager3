import React from 'react';
import { TreeView } from '../../../../components';

export default class FilterPopup extends React.Component {

    constructor(props) {
        super(props);
        this.nodeRender = this.nodeRender.bind(this);
        this.filterClick = this.filterClick.bind(this);
        this.groupClick = this.groupClick.bind(this);
    }

    /**
     * Called when user clicks on a filter
     */
    filterClick(filter) {
        return () => {
            this.props.onSelect(filter);
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
     * Render the node of the tree for selection of filter
     */
    nodeRender(item) {
        // verifica se é um grupo ou filtro
        return item.id ?
            <a className="selectable" onClick={this.filterClick(item)}>{item.label}</a> :
            <a className="lnk-label" onClick={this.groupClick(item)}><b style={{ fontSize: '1.2em' }}>{item.label}</b></a>;
    }

    /**
     * Return information about the node to the tree
     */
    nodeInfo(item) {
        return { leaf: !item.filters };
    }

    /**
     * Return the filters as children of the group
     */
    getChildNodes(item) {
        if (item.filters) {
            return item.filters;
        }
        return null;
    }

    render() {
        return (
            <div className="filter-popup">
                <TreeView ref="tv" root={this.props.filters}
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

FilterPopup.propTypes = {
    filters: React.PropTypes.array,
    onSelect: React.PropTypes.func.isRequired
};
