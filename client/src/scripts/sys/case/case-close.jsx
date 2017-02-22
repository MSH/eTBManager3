
import React from 'react';
import { FormDialog } from '../../components/index';
import { server } from '../../commons/server';
import { app } from '../../core/app';
import SessionUtils from '../session-utils';

const fschema = {
    title: __('cases.close'),
    controls: [
        {
            property: 'outcomeDate',
            required: true,
            type: 'date',
            label: __('cases.details.date'),
            size: { sm: 6 }
        },
        {
            property: 'outcome',
            required: true,
            type: 'select',
            label: __('TbCase.outcome'),
            options: [
                { id: 'CURED', name: 'Cured' },
                { id: 'TREAT_COMPLETED', name: 'Treatment completed' },
                { id: 'DIED', name: 'Died' },
                { id: 'LOST_FOLLOWUP', name: 'Lost follow-up' },
                { id: 'TREAT_INTERRUPTED', name: 'Treatment interrupted' },
                { id: 'OTHER', name: 'Other' }
            ],
            newLine: true,
            size: { sm: 12 }
        },
        {
            type: 'group',
            visible: value => value.outcome === 'OTHER',
            controls: [
                {
                    property: 'otherOutcome',
                    type: 'string',
                    label: __('CaseState.OTHER')
                }
            ]

        }
    ]
};

/**
 * The page controller of the public module
 */
export default class CaseClose extends React.Component {

    constructor(props) {
        super(props);

        this.state = { doc: {} };
        this.closeCase = this.closeCase.bind(this);
    }

    closeCase() {
        const doc = this.state.doc;
        doc.tbcaseId = this.props.tbcase.id;

        return server.post('/api/cases/case/close', doc)
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

    render() {
        fschema.title = __('cases.close') + ' - ' + SessionUtils.nameDisplay(this.props.tbcase.patient.name);
        return (
            <FormDialog
                schema={fschema}
                doc={this.state.doc}
                onConfirm={this.closeCase}
                onCancel={this.props.onClose}
                confirmCaption={__('cases.close')}
                wrapType={'modal'}
                modalShow={this.props.show}/>
        );
    }
}

CaseClose.propTypes = {
    tbcase: React.PropTypes.object,
    show: React.PropTypes.bool,
    onClose: React.PropTypes.func
};
