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
        this.state = { phase: this.checkInitialPhase() };
        this.renderContent = this.renderContent.bind(this);
        this.startSync = this.startSync.bind(this);
        this.mockSuccess = this.mockSuccess.bind(this);
    }

    checkInitialPhase() {
        // TODO: check the phase as the component loads
        return NOTRUNNING;
    }

    renderContent() {
        switch (this.state.phase) {
            case NOTRUNNING:
                return this.renderNotRunning();
            case GENERATINGFILE:
            case SENDINGFILE:
            case RECEIVINGFILE:
            case IMPORTINGFILE:
                return this.renderInProgress();
            case SUCCESS:
                return this.renderSuccess();
            default:
                return null;
        }
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

        server.post('/api/offline/sync/synchronize', req)
        .then(res => {
            if (!res.success) {
                this.setState({ globalMsgs: res.errors, fetching: false });
                return null;
            }

            this.setState({ phase: GENERATINGFILE });
            setTimeout(this.mockSuccess, 1000);

            return res;
        })
        .catch(err => {
            this.setState({ fetching: false });
            return Promise.reject(err);
        });
    }

    mockSuccess() {
        this.setState({ phase: SUCCESS });
        this.clearAllIntervals();
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
                            <h4 className="text-center">{this.state.phase}</h4>
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
