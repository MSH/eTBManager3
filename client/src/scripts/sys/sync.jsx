import React from 'react';
import { Grid, Row, Col, FormControl, FormGroup, ControlLabel, HelpBlock, Button, Alert } from 'react-bootstrap';
import { AsyncButton, Card, WaitIcon } from '../components/index';
import { validateForm } from '../commons/validator';
import { server } from '../commons/server';
import SessionUtils from './session-utils';

/**
 * Form validation model
 */
const form = {
    password: {
        required: true,
        password: true
    }
};

export default class Sync extends React.Component {

    constructor(props) {
        super(props);
        this.renderContent = this.renderContent.bind(this);
        this.startSync = this.startSync.bind(this);
        this.checkStatusUntilFinish = this.checkStatusUntilFinish.bind(this);
    }

    /**
     * Checks if sync is already running
     * @return {[type]} [description]
     */
    componentWillMount() {
        this.setState({ checking: true });

        server.get('/api/offline/client/sync/status')
        .then(res => {
            if (res.id !== 'NOT_RUNNING') {
                // update phase
                this.setState({ phase: res });
                // schedule next status checking
                setTimeout(this.checkStatusUntilFinish, 800);
            }

            this.setState({ checking: false });
            return res;
        });
    }

    /**
     * Called when user clicks on the login button
     */
    gotoHome() {
        SessionUtils.gotoHome();
    }

    /**
     * Check sync status until it finishes
     * @return {[type]} [description]
     */
    checkStatusUntilFinish() {
        server.get('/api/offline/client/sync/status')
        .then(res => {
            if (res.id === 'NOT_RUNNING') {
                // initialization has succesfully finished
                this.setState({ phase: undefined, success: true  });
            } else if (res.id === 'ERROR') {
                // initialization has an error
                this.setState({ phase: undefined, error: true  });
            } else {
                // initialization is running
                // update phase
                this.setState({ phase: res });
                // schedule next status checking
                setTimeout(this.checkStatusUntilFinish, 800);
            }
        });
    }

    /**
     * Starts sync process
     * @return {[type]} [description]
     */
    startSync() {
        // clear previous global msgs
        this.setState({ globalMsgs: null });

        const vals = validateForm(this, form);

        // there is any validation error ?
        if (vals.errors) {
            this.setState({ errors: vals.errors });
            return;
        }

        this.setState({ errors: {}, fetching: true });

        const req = vals.value;

        server.post('/api/offline/client/sync/synchronize', req)
        .then(res => {
            if (res.errors) {
                this.setState({ globalMsgs: res.errors, fetching: false });
                return null;
            }

            setTimeout(this.checkStatusUntilFinish, 800);

            return res;
        })
        .catch(err => {
            this.setState({ fetching: false });
            return Promise.reject(err);
        });
    }

    /**
     * Checks in whitch phase sync is and returns the render content
     * @return {[type]} [description]
     */
    renderContent() {
        if (this.state.checking) {
            return this.renderChecking();
        }

        if (this.state.success) {
            return this.renderSuccess();
        }

        if (this.state.error) {
            return this.renderError();
        }

        if (!this.state.phase) {
            return this.renderNotRunning();
        }

        if (this.state.phase) {
            return this.renderInProgress();
        }

        return null;
    }

    /**
     * Render content when system is checking is sync is already running
     * @return {[type]} [description]
     */
    renderChecking() {
        return (
            <div>
                <Row>
                    <Col sm={12}>
                        <h4 className="text-center">{__('global.pleasewait')}</h4>
                    </Col>
                </Row>
                <Row>
                    <Col sm={12}>
                        <WaitIcon type="card" />
                    </Col>
                    <Col sm={12}>
                        <span className="text-muted">{__('global.pleasewait')}</span>
                    </Col>
                </Row>
            </div>
            );
    }

    /**
     * Render password field and dtart sync button
     * @return {[type]} [description]
     */
    renderNotRunning() {
        const err = this.state.errors || {};
        const fetching = this.state.fetching;

        return (
            <div>
                <Row>
                    <Col sm={12}>
                        <FormGroup validationState={err.password ? 'error' : undefined}>
                            <ControlLabel>{__('sync.password')}</ControlLabel>
                            <FormControl type="password" ref="password" />
                            <HelpBlock>{err.password}</HelpBlock>
                        </FormGroup>
                    </Col>
                </Row>
                <Row>
                    <Col sm={12}>
                        <div className="pull-right">
                            <AsyncButton fetching={fetching} bsSize="large" onClick={this.startSync}>
                                {__('action.continue')}
                            </AsyncButton>
                        </div>
                    </Col>
                </Row>
            </div>
            );
    }

    /**
     * Render 'sync on progress' content and sync status
     * @return {[type]} [description]
     */
    renderInProgress() {
        return (<div>
                    <Row>
                        <Col sm={12}>
                            <h4 className="text-center">{this.state.phase.title}</h4>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={12}>
                            <WaitIcon type="card" />
                        </Col>
                        <Col sm={12}>
                            <span className="text-muted">{__('global.pleasewait')}</span>
                        </Col>
                    </Row>
                </div>);
    }

    /**
     * Render success content
     * @return {[type]} [description]
     */
    renderSuccess() {
        return (
                <div>
                    <div className="text-center">
                        <h3>
                            {__('sync.success')}
                        </h3>
                        <br/>
                        <i className="fa fa-check-circle fa-4x text-success"/>
                        <br/>
                        <p className="mtop-2x">
                            {__('init.offinit.success2')}
                        </p>
                    </div>
                    <div>
                        <Button bsStyle="default" block onClick={this.gotoHome}>{__('sync.success.btn')}</Button>
                    </div>
                </div>
                );
    }

    /**
     * Render error content
     * @return {[type]} [description]
     */
    renderError() {
        return (
                <div>
                    <div className="text-center">
                        <h3>
                            {__('error.title')}
                        </h3>
                        <br/>
                        <i className="fa fa-exclamation-triangle fa-4x text-danger"/>
                        <br/>
                        <p className="mtop-2x">
                            {__('init.error.msg2')}
                        </p>
                    </div>
                </div>
                );
    }

    render() {
        const content = this.renderContent();
        return (
            <Grid>
                <Col sm={6} smOffset={3}>
                    <Card title={__('sync.title')} className="mtop-2x">
                        {
                            this.state.globalMsgs && this.state.globalMsgs.map((it, i) => <Alert key={i} bsStyle="danger">{it.msg}</Alert>)
                        }
                        {content}
                    </Card>
                </Col>
            </Grid>
        );
    }
}

Sync.propTypes = {
    route: React.PropTypes.object
};
