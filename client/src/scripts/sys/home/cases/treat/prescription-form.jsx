import React from 'react';
import { FormDialog } from '../../../../components';
import { server } from '../../../../commons/server';
import { app } from '../../../../core/app';

const newSchema = {
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

const edtSchema = {
    title: __('cases.treat.prescription.edit'),
    controls: [
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
export default class PrescriptionForm extends React.Component {

    constructor(props) {
        super(props);

        this.confirm = this.confirm.bind(this);
        this.cancel = this.cancel.bind(this);
    }

    componentWillMount() {
        const doc = {};
        const prescData = this.props.prescData;
        if (prescData) {
            console.log(prescData);
            // load editing prescription data
            doc.prescriptionId = prescData.prescriptionId;
            doc.iniDate = prescData.ini;
            doc.endDate = prescData.end;
            doc.doseUnit = prescData.doseUnit;
            doc.frequency = prescData.frequency;
            doc.comments = prescData.comments ? prescData.comments : '';
        }

        this.state = { doc: doc, isNew: !doc.prescriptionId };
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

        return this.state.isNew ? this.saveNew(doc) : this.saveEdt(doc);
    }

    saveNew(doc) {
        console.log('saving new', doc);
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

    saveEdt(doc) {
        console.log('saving edt', doc);
        return server.post('/api/cases/case/treatment/prescription/update', doc)
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
        if (!this.props.tbcase || !this.props.tbcase.id) {
            return null;
        }

        const schema = this.state.isNew ? newSchema : edtSchema;

        return (
            <FormDialog
                schema={schema}
                doc={doc}
                wrapType="modal" modalShow
                onConfirm={this.confirm}
                onCancel={this.cancel} />
            );
    }
}

PrescriptionForm.propTypes = {
    tbcase: React.PropTypes.object,
    onClose: React.PropTypes.func,
    prescData: React.PropTypes.object
};
