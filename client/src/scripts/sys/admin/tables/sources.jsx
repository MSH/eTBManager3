
import React from 'react';
import CrudView from '../../crud/crud-view';
import CRUD from '../../../commons/crud';

const crud = new CRUD('source');

// definition of the form fields to edit substances
const editorDef = {
    defaultProperties: {
        active: true
    },
    controls: [
        {
            property: 'shortName',
            required: true,
            type: 'string',
            max: 20,
            label: __('form.shortName'),
            size: { md: 3 }
        },
        {
            property: 'name',
            required: true,
            type: 'string',
            max: 200,
            label: __('form.name'),
            size: { md: 6 }
        },
        {
            property: 'customId',
            type: 'string',
            max: 20,
            label: __('form.customId'),
            size: { md: 3 }
        },
        {
            property: 'active',
            type: 'yesNo',
            label: __('EntityState.ACTIVE'),
            size: { md: 5 }
        }
    ],
    title: doc => doc && doc.id ? __('admin.sources.edit') : __('admin.sources.new')
};

/**
 * The page controller of the public module
 */
export default class Sources extends React.Component {

    cellRender(item) {
        return (
            <div>
                <b>{item.shortName}</b>
                <div className="text-muted">{item.name}</div>
            </div>
            );
    }

    render() {
        // get information about the route of this page
        const data = this.props.route.data;

//        tableDef.title = data.title;

        return (
            <CrudView crud={crud}
                title={data.title}
                editorSchema={editorDef}
                onCellRender={this.cellRender}
                perm={data.perm} />
            );
    }
}

Sources.propTypes = {
    route: React.PropTypes.object
};
