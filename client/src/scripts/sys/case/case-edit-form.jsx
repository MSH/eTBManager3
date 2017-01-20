import React from 'react';
import { FormDialog, observer } from '../../components';
import { server } from '../../commons/server';
import { app } from '../../core/app';
import Events from './events';

/**
 * Component that displays and handle notification form
 */
class CaseEditForm extends React.Component {

    constructor(props) {
        super(props);
        this.handleEvent = this.handleEvent.bind(this);
        this.onClose = this.onClose.bind(this);
        this.confirm = this.confirm.bind(this);

        this.state = { show: false };
    }

    /**
     * Called when update tbcase form should be displayed
     * @param  {[type]} action [description]
     * @return {[type]}        [description]
     */
    handleEvent() {
        this.setState({ show: true });
    }

    confirm(doc) {
        const req = {
            doc: doc,
            caseId: this.props.tbcase.id,
            patientId: this.props.tbcase.patient.id
        };

        return server.post('/api/cases/case/edit/save', req).then(res => {
            if (res.errors) {
                return Promise.reject(res.errors);
            }

            this.onClose();
            app.dispatch('case-update');

            return res.result;
        });
    }

    onClose() {
        this.setState({ show: false });
    }

    render() {
        const tbcase = this.props.tbcase;

        // testing show like this because form-dialog need to mount to get the updated form.
        if (!tbcase || !tbcase.id || !this.state.show) {
            return null;
        }

        const path = '/api/cases/case/edit/form/' + this.props.tbcase.id;

        return (
            <FormDialog
                modalShow
                wrapType="modal"
                remotePath={path}
                onCancel={this.onClose}
                onConfirm={this.confirm}
                modalBsSize="large" />
        );
    }
}

export default observer(CaseEditForm, Events.caseEditForm);

CaseEditForm.propTypes = {
    tbcase: React.PropTypes.object
};
