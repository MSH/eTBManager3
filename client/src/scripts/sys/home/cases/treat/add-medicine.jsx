import React from 'react';
import { FormDialog } from '../../../../components';
import { server } from '../../../../commons/server';
import { app } from '../../../../core/app';

const schema = {
    title: __('Regimen.add'),
    controls: [
        {
            property: 'productId',
            type: 'select',
            label: __('Medicine'),
            required: true,
            options: 'medicines',
            size: { md: 12 }
        },
        {
            property: 'iniDate',
            type: 'date',
            label: __('Period.iniDate'),
            required: true,
            size: { md: 6 }
        },
        {
            property: 'endDate',
            type: 'date',
            label: __('Period.endDate'),
            required: true,
            size: { md: 6 },
            validate: doc => doc.ini < doc.end,
            validateMessage: __('period.inienddate')
        },
        {
            property: 'doseUnit',
            type: 'select',
            label: __('PrescribedMedicine.doseUnit'),
            required: true,
            size: { md: 6 },
            options: {
                from: 1,
                to: 10
            }
        },
        {
            property: 'frequency',
            type: 'select',
            required: true,
            label: __('PrescribedMedicine.frequency'),
            size: { md: 6 },
            options: { from: 1, to: 7 }
        },
        {
            property: 'comments',
            type: 'text',
            label: __('global.comments')
        }
    ]
};

/**
 * Display modal dialog to add a new medicine to the treatment regimen
 */
export default class AddMedicine extends React.Component {

    constructor(props) {
        super(props);

        this.state = { doc: {} };

        this.confirm = this.confirm.bind(this);
        this.cancel = this.cancel.bind(this);
    }

    /**
     * Add the medicine to the treatment
     * @param  {[type]} doc [description]
     * @return {[type]}     [description]
     */
    confirm() {
        if (!this.props.tbcase || !this.props.tbcase.id) {
            return Promise.reject();
        }

        const doc = this.state.doc;
        doc.caseId = this.props.tbcase.id;

        return server.post('/api/cases/case/treatment/prescription/add', doc)
                .then(res => {
                    if (!res.success) {
                        return Promise.reject(res.errors);
                    }

                    this.setState({ doc: {} });
                    this.props.onClose();

                    app.dispatch('update-treatment');

                    return res.result;
                });
    }

    cancel() {
        this.props.onClose(null);
    }

    render() {
        const doc = this.state.doc;
        if (!doc) {
            return null;
        }

        return (
            <FormDialog
                schema={schema}
                doc={doc}
                wrapType="modal"
                modalShow={this.props.show}
                onConfirm={this.confirm}
                onCancel={this.cancel} />
            );
    }
}

AddMedicine.propTypes = {
    tbcase: React.PropTypes.object,
    onClose: React.PropTypes.func,
    show: React.PropTypes.bool
};
