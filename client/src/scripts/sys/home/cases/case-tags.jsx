
import React from 'react';
import { FormDialog } from '../../../components/index';
import { server } from '../../../commons/server';
import { app } from '../../../core/app';
import SessionUtils from '../../session-utils';

const tfschema = {
    controls: [{
        property: 'newTag',
        required: true,
        type: 'string',
        max: 20,
        label: 'New tag',
        size: { sm: 12 }
    }
    ]
};

const fschema = {
    title: __('Permission.CASE_TAG'),
    controls: [
        {
            property: 'tagIds',
            type: 'multiSelect',
            options: 'manualTags',
            label: __('admin.tags'),
            size: { sm: 12 },
            required: true
        },
        {
            property: 'newTags',
            type: 'tableForm',
            fschema: tfschema,
            min: 0,
            size: { sm: 12 }
        }
    ]
};

/**
 * The page controller of the public module
 */
export default class CaseTags extends React.Component {

    constructor(props) {
        super(props);

        this.state = { doc: {} };
        this.saveTags = this.saveTags.bind(this);
        this.onCancel = this.onCancel.bind(this);
    }

    componentWillMount() {
        const doc = this.state.doc;
        // set as selected only manual assigned tags
        doc.tagIds = this.props.tbcase.tags.filter(item => item.type === 'MANUAL');
        // set only tag ids
        doc.tagIds = doc.tagIds.map(item => item.id);
    }

    saveTags() {
        const req = {};
        req.tbcaseId = this.props.tbcase.id;
        req.newTags = this.state.doc.newTags ? this.state.doc.newTags.map(item => item.newTag) : null;
        req.tagIds = this.state.doc.tagIds;

        return server.post('/api/cases/tag/update', req)
                .then(res => {
                    if (!res.success) {
                        return Promise.reject(res.errors);
                    }

                    this.setState({ doc: {} });
                    this.props.onClose();

                    app.dispatch('case-update');

                    return res.result;
                });
    }

    onCancel() {
        this.setState({ doc: {} });
        this.props.onClose();
    }

    render() {
        fschema.title = __('Permission.CASE_TAG') + ' - ' + SessionUtils.nameDisplay(this.props.tbcase.patient.name);

        return (
            <FormDialog
                schema={fschema}
                doc={this.state.doc}
                onConfirm={this.saveTags}
                onCancel={this.onCancel}
                confirmCaption={__('action.save')}
                wrapType={'modal'}
                modalShow={this.props.show}/>
        );
    }
}

CaseTags.propTypes = {
    tbcase: React.PropTypes.object,
    show: React.PropTypes.bool,
    onClose: React.PropTypes.func
};
