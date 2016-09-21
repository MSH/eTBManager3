
import React from 'react';
import Form from '../../../forms/form';
import FormUtils from '../../../forms/form-utils';
import { PopupControl, TreeView, WaitIcon } from '../../../components';


class CommandTypeControl extends React.Component {

    constructor(props) {
        super(props);
        this.popupRender = this.popupRender.bind(this);
        this.renderNode = this.renderNode.bind(this);
        this.clickItem = this.clickItem.bind(this);
        this.clickTV = this.clickTV.bind(this);
    }

    serverRequest(nextSchema, value, nextResources) {
        return this.props.resources || nextResources ? null : { cmd: 'command-types' };
    }

    clickItem(item) {
        return () => {
            this.props.onChange({
                schema: this.props.schema,
                value: item.id
            });
            this._canClose = true;
        };
    }

    renderNode(item) {
        return <a onClick={this.clickItem(item)}>{item.name}</a>;
    }

    nodeInfo(item) {
        return { leaf: !item.children };
    }

    getNodes(item) {
        return item.children;
    }

    clickTV() {
        if (!this._canClose) {
            this.refs.ctrl.preventHide();
        }
        delete this._canClose;
    }

    // search for item by its path code
    findOption(path) {
        const ids = path.split('.');
        let item = null;
        let lst = this.props.resources;
        let pref = null;

        ids.forEach(id => {
            const spath = pref ? pref + '.' + id : id;
            item = lst.find(k => k.id === spath);
            lst = item.children;
            pref = spath;
        });

        return item;
    }

    popupRender() {
        return (
            <div onClick={this.clickTV}>
                <TreeView root={this.props.resources}
                    onGetNodes={this.getNodes}
                    innerRender={this.renderNode}
                    nodeInfo={this.nodeInfo}
                    style={{ minWidth: '280px' }}
                    />
            </div>
            );
    }

    render() {
        if (!this.props.resources) {
            return <WaitIcon type="field" />;
        }

        const schema = this.props.schema;

        const label = FormUtils.labelRender(schema.label, schema.requred);

        const content = this.props.value ? this.findOption(this.props.value) : null;

        return (
            <PopupControl
                ref="ctrl"
                label={label}
                content={content ? content.name : ''}
                popupContent={this.popupRender()} />
            );
    }
}

CommandTypeControl.propTypes = {
    value: React.PropTypes.string,
    schema: React.PropTypes.object.isRequired,
    onChange: React.PropTypes.func,
    errors: React.PropTypes.any,
    resources: React.PropTypes.array
};

export default Form.control(CommandTypeControl);
