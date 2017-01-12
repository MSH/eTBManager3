import React from 'react';
import { Grid, Row, Col, Fade, FormControl, FormGroup, ControlLabel, HelpBlock, Button, Alert } from 'react-bootstrap';
import { AsyncButton, Card, WaitIcon } from '../../components/index';
import { validateForm } from '../../commons/validator';
import { server } from '../../commons/server';

const NOTRUNNING = 'NOTRUNNING';
const GENERATINGFILE = 'GENERATINGFILE';
const SENDINGFILE = 'SENDINGFILE';
const RECEIVINGFILE = 'RECEIVINGFILE';
const IMPORTINGFILE = 'IMPORTINGFILE';
const SUCCESS = 'SUCCESS';

// TODO: check and create messages

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
     * Check initialization status until it finishes
     * @return {[type]} [description]
     */
    checkStatusUntilFinish() {
        this.clearAllIntervals();

        server.get('/api/offline/client/sync/status')
        .then(res => {
            if (res.id !== 'NOT_RUNNING') {
                // update phase
                this.setState({ phase: res });
                // schedule next status checking
                setTimeout(this.checkStatusUntilFinish, 800);
            } else {
                // initialization has finished
                this.setState({ phase: undefined, success: true  });
            }
        });
    }

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
            setTimeout(this.checkStatusUntilFinish, 800);

            return res;
        })
        .catch(err => {
            this.setState({ fetching: false });
            return Promise.reject(err);
        });
    }

    /**
     * Clear all timeouts
     * @return {[type]} [description]
     */
    clearAllIntervals() {
        // clear all intervals
        const id = setInterval(() => {}, 9999);
        for (var i = 0; i <= id; i++) {
            clearInterval(i);
        }
    }

    renderContent() {
        if (this.state.checking) {
            return this.renderChecking();
        }

        if (this.state.success) {
            return this.renderSuccess();
        }

        if (!this.state.phase) {
            return this.renderNotRunning();
        }

        if (this.state.phase) {
            return this.renderInProgress();
        }

        return null;
    }

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

    renderNotRunning() {
        const err = this.state.errors || {};
        const fetching = this.state.fetching;

        return (
            <div>
                <Row>
                    <Col sm={12}>
                        <FormGroup validationState={err.password ? 'error' : undefined}>
                            <ControlLabel>{'Type your password:'}</ControlLabel>
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

    renderSuccess() {
        return (
                <div>
                    <div className="text-center">
                        <h3>
                            {'Synchronization Completed'}
                        </h3>
                        <br/>
                        <i className="fa fa-check-circle fa-4x text-success"/>
                        <br/>
                        <p className="mtop-2x">
                            {__('init.offinit.success2')}
                        </p>
                    </div>
                    <div>
                        <Button bsStyle="default" block>{'Go back to System'}</Button>
                    </div>
                </div>
                );
    }

    render() {
        const content = this.renderContent();
        return (
            <Grid>
                <Col sm={6} smOffset={3}>
                    <Card title={'Synchronize with server'} className="mtop-2x">
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
