
import React from 'react';
import { Grid, Row, Col, Fade, FormControl, FormGroup, ControlLabel, HelpBlock, Button, Alert } from 'react-bootstrap';
import { AsyncButton, Card, WaitIcon } from '../components/index';
import { validateForm } from '../commons/validator';
import { server } from '../commons/server';
import { app } from '../core/app';


/**
 * Form validation model
 */
const form = {
    parentServerUrl: {
        required: true,
        min: 5
    },
    username: {
        required: true,
        min: 3
    },
    password: {
        required: true,
        password: true
    }
};

export default class OfflineInit extends React.Component {
    constructor(props) {
        super(props);
        this.findWorkspaces = this.findWorkspaces.bind(this);
        this.workspaceChange = this.workspaceChange.bind(this);
        this.workspaceSelected = this.workspaceSelected.bind(this);
        this.showUrlTextField = this.showUrlTextField.bind(this);
        this.gotoLogin = this.gotoLogin.bind(this);
        this.checkStatusUntilFinish = this.checkStatusUntilFinish.bind(this);

        this.state = {
            data: {},
            /*
                Error messages to be displayed on the top of login phase screen
             */
            globalMsgs: undefined,
            /*
                Indicate whitch phase is running on initialize process
             */
            phase: undefined,
            /*
                List of workspaces of the user.
                If it contains only one go straight to importing.
                If it contains more than one, show a selection box.
            */
            workspaces: undefined,
            /*
                Flag that, when true, change the URL select field to a text field.
                It is used on form phase
            */
            urlText: undefined,
            /*
                Flag that indicates that component is checking if system is already initializing
             */
            checking: false
        };
    }

    componentWillMount() {
        this.setState({ checking: true });

        server.get('/api/offline/init/status')
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
     * Called when user clicks on the continue button
     */
    findWorkspaces() {
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

        server.post('/api/offline/init/workspaces', req)
        .then(res => {
            if (!res.success) {
                this.setState({ globalMsgs: res.errors, fetching: false });
                return null;
            }

            const workspaces = res.result;

            if (workspaces && workspaces.length === 1) {
                this.setState({ workspaceId: workspaces[0].id, fetching: false, credentials: req, globalMsg: null });
                this.workspaceSelected();
            } else if (workspaces && workspaces.length > 1) {
                this.setState({ workspaces: workspaces, fetching: false, credentials: req, globalMsg: null });
            } else {
                const globalMsg = {
                    msg: __('init.offinit.error2')
                };
                this.setState({ globalMsgs: [globalMsg] });
            }

            return res;
        })
        .catch(err => {
            this.setState({ fetching: false });
            return Promise.reject(err);
        });
    }

    workspaceSelected() {
        if (!this.state.workspaceId) {
            this.setState({ errors: { ws: __('NotNull') } });
            return;
        }

        this.setState({ errors: {}, fetching: true });

        const req = this.state.credentials;
        req.workspaceId = this.state.workspaceId;

        server.post('/api/offline/init/initialize', req)
        .then(res => {
            this.setState({ phase: res, fetching: false, workspaces: null });
            // schedule status checking
            setTimeout(this.checkStatusUntilFinish, 800);
            return res;
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

    /**
     * Check initialization status until it finishes
     * @return {[type]} [description]
     */
    checkStatusUntilFinish() {
        this.clearAllIntervals();

        server.get('/api/offline/init/status')
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

    /**
     * Called when user select a workspace
     */
    workspaceChange(evt) {
        const val = evt.target.value;
        this.setState({ workspaceId: val });
    }

    /**
     * Called when user clicks on 'show text field' link
     */
    showUrlTextField() {
        this.setState({ urlText: true });
    }

    /**
     * Called when user clicks on the login button
     */
    gotoLogin() {
        app.goto('/pub/login');
        window.location.reload(true);
    }

    /**
     * Render the login form phase
     */
    renderLoginForm() {
        const err = this.state.errors || {};
        const fetching = this.state.fetching;

        return (
                <div>
                    {
                        this.state.urlText ?
                        <Row>
                            <Col sm={12}>
                                <FormGroup validationState={err.parentServerUrl ? 'error' : undefined}>
                                    <ControlLabel>{__('init.offinit.url') + ':'}</ControlLabel>
                                    <FormControl type="text" ref="parentServerUrl" autoFocus />
                                    <HelpBlock>{err.parentServerUrl}</HelpBlock>
                                </FormGroup>
                            </Col>
                        </Row> :
                        <Row>
                            <Col sm={12}>
                                <FormGroup validationState={err.parentServerUrl ? 'error' : undefined}>
                                    <ControlLabel>{__('init.offinit.url') + ':'}</ControlLabel>
                                    <FormControl componentClass="select" ref="parentServerUrl" size={1} autoFocus>
                                        <option value={'www.etbmanager.org'}>{'www.etbmanager.org'}</option>
                                        <option value={'www.etbmanagerbd.org'}>{'www.etbmanagerbd.org'}</option>
                                    </FormControl>
                                    <a className="lnk-muted" style={{ padding: '0px', fontSize: '0.9em' }} onClick={this.showUrlTextField}>
                                        {__('init.initoptions.urltxt')}
                                    </a>
                                    <HelpBlock>{err.parentServerUrl}</HelpBlock>
                                </FormGroup>
                            </Col>
                        </Row>
                    }
                    <Row>
                        <Col sm={12}>
                            <FormGroup validationState={err.username ? 'error' : undefined}>
                                <ControlLabel>{__('User.login') + ':'}</ControlLabel>
                                <FormControl type="text" ref="username" />
                                <HelpBlock>{err.username}</HelpBlock>
                            </FormGroup>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={12}>
                            <FormGroup validationState={err.password ? 'error' : undefined}>
                                <ControlLabel>{__('User.password') + ':'}</ControlLabel>
                                <FormControl type="password" ref="password" />
                                <HelpBlock>{err.password}</HelpBlock>
                            </FormGroup>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={12}>
                            <div className="pull-right">
                                <AsyncButton fetching={fetching} bsSize="large" onClick={this.findWorkspaces}>
                                    {__('action.continue')}
                                </AsyncButton>
                            </div>
                        </Col>
                    </Row>
                </div>
            );
    }

    /**
     * Render the workspace selecion phase
     */
    renderWorkspaceSelection() {
        const err = this.state.errors || {};
        const fetching = this.state.fetching;

        return (
                <div>
                    <Row>
                        <Col sm={12}>
                            <FormGroup validationState={err.ws ? 'error' : undefined}>
                                <ControlLabel>{__('init.offinit.selworkspace') + ':'}</ControlLabel>
                                <FormControl componentClass="select" size={8} autoFocus onChange={this.workspaceChange}>
                                    {
                                        this.state.workspaces.map(item =>
                                            <option key={item.id} value={item.id}>{item.name + ' - ' + item.unitName}</option>
                                        )
                                    }
                                </FormControl>
                                <HelpBlock>{err.ws}</HelpBlock>
                            </FormGroup>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={12}>
                            <div className="pull-right">
                                <AsyncButton fetching={fetching} bsSize="large" onClick={this.workspaceSelected}>
                                    {__('action.continue')}
                                </AsyncButton>
                            </div>
                        </Col>
                    </Row>
                </div>
                );
    }

    /**
     * Render the downloading file phase
     */
    renderWait() {
        return (
                <div>
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
                </div>
                );
    }

    renderSuccess() {
        return (
                <div>
                    <div className="text-center">
                        <h3>
                            {__('init.offinit.success1')}
                        </h3>
                        <br/>
                        <i className="fa fa-check-circle fa-4x text-success"/>
                        <br/>
                        <p className="mtop-2x">
                            {__('init.offinit.success2')}
                        </p>
                    </div>
                    <div>
                        <Button bsStyle="default" block onClick={this.gotoLogin}>{__('init.ws.gotologin')}</Button>
                    </div>
                </div>
                );
    }

    /**
     * Render the checking server phase
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
     * Render the component
     */
    render() {
        let content;

        if (this.state.checking) {
            content = this.renderChecking();
        } else if (this.state.success) {
            // success mesage
            content = this.renderSuccess();
        } else if (this.state.phase) {
            // downloading phase screen
            content = this.renderWait();

        } else if (this.state.workspaces) {
            // workspace selection phase screen
            content = this.renderWorkspaceSelection();

        } else {
            // login phase screen
            content = this.renderLoginForm();

        }

        return (
            <Fade in transitionAppear>
                <Grid>
                    <Col sm={6} smOffset={3}>
                        <Card title={__('init.offinit')} className="mtop-2x">
                            {
                                this.state.globalMsgs && this.state.globalMsgs.map((it, i) => <Alert key={i} bsStyle="danger">{it.msg}</Alert>)
                            }
                            {content}
                        </Card>
                    </Col>
                </Grid>
            </Fade>
        );
    }
}
