import React from 'react';
import { Profile } from '../../../components';
import { CrudView } from '../../packages/crud';
import CRUD from '../../../commons/crud';

export default class Workspaces extends React.Component {

    componentWillMount() {
        const editor = {
            controls: [
                {
                    type: 'string',
                    label: __('form.name'),
                    property: 'name',
                    size: { sm: 6 },
                    required: true
                }
            ],
            title: doc => doc.id ? __('admin.workspaces.edt') : __('admin.workspaces.new')
        };

        this.setState({ crud: new CRUD('workspace'), editor: editor });
    }

    cellRender(item) {
        return <Profile title={item.name} type="ws" size="small" />;
    }

    render() {
        return (
            <CrudView
                title={this.props.route.data.title}
                onCellRender={this.cellRender}
                crud={this.state.crud}
                editorSchema={this.state.editor}
                perm="WORKSPACES" />
        );
    }
}

Workspaces.propTypes = {
    route: React.PropTypes.object
};
