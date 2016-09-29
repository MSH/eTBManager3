import React from 'react';
import { FormDialog, observer } from '../../../components';
import { server } from '../../../commons/server';
import { app } from '../../../core/app';
import Events from '../events';

/**
 * Display the form to start a standard regimen
 */
class StartRegimen extends React.Component {

    constructor(props) {
        super(props);
        this.save = this.save.bind(this);
        this.close = this.close.bind(this);
    }

    /**
     * Called by the observer class to inform about a registered event
     */
    handleEvent(evt, tbcase) {
        const individualized = evt === Events.startInvidRegimen;

        const formSchema = individualized ?
        // structure of the individualized form
        {
            title: __('cases.treatment.startindiv'),
            controls: [
                {
                    property: 'unitId',
                    type: 'unit',
                    label: __('Tbunit.treatmentHealthUnit'),
                    unitType: 'TBUNIT',
                    required: true
                },
                {
                    property: 'iniDate',
                    type: 'date',
                    label: __('TbCase.iniTreatmentDate'),
                    required: true,
                    size: { sm: 6 }
                },
                {
                    property: 'prescriptions',
                    label: __('cases.details.treatment.prescmeds'),
                    type: 'tableForm',
                    required: true,
                    fschema: {
                        controls: [
                            {
                                property: 'productId',
                                required: true,
                                type: 'select',
                                label: __('Medicine'),
                                options: 'medicines',
                                size: { sm: 6 }
                            },
                            {
                                property: 'doseUnit',
                                type: 'select',
                                required: true,
                                label: __('PrescribedMedicine.doseUnit'),
                                options: { from: 1, to: 12 },
                                size: { sm: 3 }
                            },
                            {
                                property: 'frequency',
                                type: 'select',
                                required: true,
                                label: __('PrescribedMedicine.frequency'),
                                options: { from: 1, to: 7 },
                                size: { sm: 3 }
                            },
                            {
                                property: 'monthIni',
                                type: 'select',
                                required: true,
                                label: __('PrescribedMedicine.iniMonth'),
                                options: { from: 1, to: 24 },
                                size: { sm: 3, smOffset: 6 }
                            },
                            {
                                property: 'months',
                                type: 'select',
                                required: true,
                                label: __('PrescribedMedicine.months'),
                                options: { from: 1, to: 24 },
                                size: { sm: 3 }
                            }

                        ]
                    }
                }
            ]
        } :
        // structure of the standardized regimen form
        {
            title: __('cases.treatment.startstandard'),
            controls: [
                {
                    property: 'unitId',
                    type: 'unit',
                    label: __('Tbunit.treatmentHealthUnit'),
                    unitType: 'TBUNIT',
                    required: true
                },
                {
                    property: 'iniDate',
                    type: 'date',
                    label: __('TbCase.iniTreatmentDate'),
                    required: true,
                    size: { sm: 6 }
                },
                {
                    property: 'regimenId',
                    type: 'select',
                    label: __('Regimen'),
                    options: 'regimens',
                    params: {
                        classification: tbcase.classification
                    },
                    required: true
                }
            ]
        };

        this.setState({
            tbcase: tbcase,
            show: true,
            individualized: individualized,
            schema: formSchema,
            doc: {
                caseId: tbcase.id,
                unitId: tbcase.ownerUnit.id
            }
        });
    }

    /**
     * Request the server to start the treatment
     */
    save(doc) {
        return server.post('/api/cases/case/treatment/start', doc)
        .then(res => {
            if (res.success) {
                app.dispatch(Events.caseUpdate);
                return;
            }
        });
    }

    /**
     * Close the form
     */
    close() {
        this.setState({ tbcase: null, show: false });
    }

    render() {
        if (!this.state || !this.state.show) {
            return null;
        }

        return (
            <FormDialog schema={this.state.schema}
                wrapType="modal"
                modalShow
                doc={this.state.doc}
                onConfirm={this.save}
                onCancel={this.close}
                modalBsSize={this.state.individualized ? 'large' : null}
                />
        );
    }
}

export default observer(StartRegimen, [Events.startStandardRegimen, Events.startInvidRegimen]);
