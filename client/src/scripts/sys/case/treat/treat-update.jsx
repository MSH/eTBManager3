import React from 'react';
import { FormDialog, observer } from '../../../components';
import { server } from '../../../commons/server';
import { app } from '../../../core/app';
import Events from '../events';

const schema = {
    title: __('cases.regimens.changeperiod'),
    controls: [
        {
            property: 'endDate',
            type: 'date',
            label: __('Period.endDate'),
            required: true,
            size: { md: 6 }
        }
    ]
};

/**
 * Display modal dialog to add a new medicine to the treatment regimen
 */
class TreatmentUpdate extends React.Component {

    constructor(props) {
        super(props);

        this.confirm = this.confirm.bind(this);
        this.onClose = this.onClose.bind(this);

        this.state = { doc: {}, show: false };
    }

    /**
     * Called when add prescription form should be displayed
     * @param  {[type]} action [description]
     * @return {[type]}        [description]
     */
    handleEvent() {
        this.setState({ show: true });
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

        return server.post('/api/cases/case/treatment/update', doc)
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
        const doc = this.state.doc;

        // testing show like this because form-dialog need to mount to get the updated doc.
        if (!tbcase || !tbcase.id || !tbcase.treatmentPeriod || !this.state.show) {
            return null;
        }

        doc.endDate = tbcase.treatmentPeriod.end;

        return (
            <FormDialog
                schema={schema}
                doc={doc}
                wrapType="modal" modalShow
                onConfirm={this.confirm}
                onCancel={this.onClose} />
            );
    }
}

export default observer(TreatmentUpdate, Events.treatUpdateForm);

TreatmentUpdate.propTypes = {
    tbcase: React.PropTypes.object
};
