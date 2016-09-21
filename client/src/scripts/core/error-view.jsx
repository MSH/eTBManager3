import React from 'react';
import { Modal, Button } from 'react-bootstrap';
import { ERROR } from './actions';
import { app } from './app';

/**
 * Standard view of application errors
 */
export default class ErrorView extends React.Component {

    constructor(props) {
        super(props);
        this._onAppChange = this._onAppChange.bind(this);
        this.close = this.close.bind(this);
    }

    componentDidMount() {
        app.add(this._onAppChange);
    }

    componentWillUmount() {
        app.remove(this._onAppChange);
    }

    /**
     * Called when application state changes
     * @param  {[type]} action [description]
     * @return {[type]}        [description]
     */
    _onAppChange(action, data) {
        if (action === ERROR) {
            this.setState(data);
        }
    }

    /**
     * Called to close the error message
     */
    close() {
        // clean up the error message
        this.setState({ error: null });
    }

    render() {
        const err = this.state ? this.state.error : null;
        const show = err !== null;

        return (
            <Modal show={show} onHide={this.close} >
                <Modal.Header closeButton>
                    <h2>{__('error.title')}</h2>
                </Modal.Header>
                <Modal.Body>
                    <div className="pull-left" style={{ marginRight: '20px' }} >
                        <i className="fa fa-5x fa-bomb"/>
                    </div>
                    <div style={{ minHeight: '60px' }}>
                        <p className="lead">{__('error.msg1')}</p>
                        <p className="text-muted"><b>{__('form.reason') + ': '}</b>{err}</p>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button bsStyle="danger" onClick={this.close}>{__('action.close')}</Button>
                </Modal.Footer>
            </Modal>
            );
    }
}
