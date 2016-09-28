import React from 'react';
import { FormDialog, observer } from '../../../../components';
import { server } from '../../../../commons/server';
import { app } from '../../../../core/app';
import Events from '../events';

const schema = {
    title: __('cases.treat.prescription.edit'),
    controls: [
        {
            property: 'preservePrevPeriod',
            type: 'yesNo',
            label: __('cases.treat.preservePreviousPeriod'),
            required: true,
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
            validate: doc => doc.iniDate < doc.endDate,
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
export default class EdtPrescription extends React.Component {

    constructor(props) {
        super(props);

        this.confirm = this.confirm.bind(this);
        this.onClose = this.onClose.bind(this);
        this.handleEvent = this.handleEvent.bind(this);

        this.state = { doc: {}, show: false };
    }

    /**
     * Called when edt prescription form should be displayed
     * @param  {[type]} action [description]
     * @return {[type]}        [description]
     */
    handleEvent(evt, data) {
        const doc = {};

        doc.prescriptionId = data.prescriptionId;
        doc.iniDate = data.ini;
        doc.endDate = data.end;
        doc.doseUnit = data.doseUnit;
        doc.frequency = data.frequency;
        doc.comments = data.comments ? data.comments : '';
        doc.preservePrevPeriod = false;

        this.setState({ show: true, doc: doc });
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

        return server.post('/api/cases/case/treatment/prescription/update', doc)
                .then(res => {
                    if (!res.success) {
                        return Promise.reject(res.errors);
                    }

                    app.dispatch('update-treatment');

                    this.onClose();
                    return res.result;
                });
    }

    onClose() {
        this.setState({ doc: {}, show: false });
    }

    render() {
        const tbcase = this.props.tbcase;

        // testing show like this because form-dialog need to mount to get the updated doc.
        if (!tbcase || !tbcase.id || !tbcase.treatmentPeriod || !this.state.show) {
            return null;
        }

        return (
            <FormDialog
                schema={schema}
                doc={this.state.doc}
                wrapType="modal" modalShow
                onConfirm={this.confirm}
                onCancel={this.onClose} />
            );
    }
}

export default observer(EdtPrescription, Events.edtPrescription);

EdtPrescription.propTypes = {
    tbcase: React.PropTypes.object
};
