import React from 'react';
import { Alert } from 'react-bootstrap';
import { MessageDlg, Errors } from '../../../components';
import controlWrapper from './crud-control-wrapper';

class CrudMessage extends React.Component {

    constructor(props) {
        super(props);

        this.hideMessage = this.hideMessage.bind(this);
        this.confirmDlgClose = this.confirmDlgClose.bind(this);

        this.state = { msg: null };
    }

    eventHandler(evt, data) {
        if (evt === 'show-msg') {
            this.setState({ msg: data.msg, type: data.type });
            return;
        }

        if (evt === 'confirm-delete') {
            this.setState({ confirm: data });
            return;
        }
    }

    /**
     * Hide the alert box displaying the message
     */
    hideMessage() {
        this.setState({ msg: null, type: null });
    }

    confirmDlgClose(action) {
        if (action === 'yes') {
            this.props.controller.confirmDelete();
        }
        this.setState({ confirm: null });
    }

    render() {
        const msg = this.state.msg;
        const msgpanel = msg ?
            <Alert bsStyle={this.state.type === 'error' ? 'danger' : 'warning'}
                onDismiss={this.hideMessage}
                style={{ marginTop: '10px', marginBottom: '10px' }}>
                <Errors messages={msg} />
            </Alert> :
            null;

        const confirm = this.state.confirm;

        const confirmDlg = confirm ?
            <MessageDlg show
                title={confirm.title}
                message={confirm.msg}
                type="YesNo"
                onClose={this.confirmDlgClose} /> :
            null;

        return (
            <div>
                {msgpanel}
                {confirmDlg}
            </div>
            );
    }
}

CrudMessage.propTypes = {
    controller: React.PropTypes.object.isRequired
};

export default controlWrapper(CrudMessage);
