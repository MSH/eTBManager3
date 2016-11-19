
import React from 'react';
import { Grid, Row, Col, Fade, FormControl, FormGroup, ControlLabel, HelpBlock, Button, ProgressBar, Alert } from 'react-bootstrap';
import { AsyncButton, Card, WaitIcon } from '../components/index';
import { validateForm } from '../commons/validator';
import { format } from '../commons/utils';
import { server } from '../commons/server';
import { app } from '../core/app';


/**
 * Form validation model
 */
const form = {
    serverURL: {
        required: true,
        min: 5
    },
    login: {
        required: true,
        min: 3
    },
    pwd: {
        required: true,
        password: true
    }
};

const mockWorkspaces = [
    { id: 1, name: 'Brasil', unitName: 'Hospital Helio Fraga' },
    { id: 2, name: 'Bangladesh', unitName: 'Kalianjour Upazilla' }
];

export default class OfflineInit extends React.Component {
    constructor(props) {
        super(props);
        this.findWorkspaces = this.findWorkspaces.bind(this);
        this.workspaceChange = this.workspaceChange.bind(this);
        this.workspaceSelected = this.workspaceSelected.bind(this);

        this.state = { data: {}, globalMsg: 'You don\'t have access to do this' };
    }

    /**
     * Called when user clicks on the continue button
     */
    findWorkspaces() {
        const vals = validateForm(this, form);

        // there is any validation error ?
        if (vals.errors) {
            this.setState({ errors: vals.errors });
            return;
        }

        this.setState({ errors: {}, fetching: true });

        if (mockWorkspaces && mockWorkspaces.length === 1) {
            setTimeout(() => {
                this.setState({ workspaceId: mockWorkspaces[0].id, fetching: false, globalMsg: null });
                this.workspaceSelected();
            }, 500);
        } else {
            setTimeout(() => this.setState({ workspaces: mockWorkspaces, fetching: false, globalMsg: null }), 500);
        }

        /*
        const v = vals.value;
        const data = {
            serverURL: v.serverURL,
            login: v.login,
            password: v.pwd
        };

        const self = this;

        server.post('/api/init/workspace', data)
            .then(res => {
                if (res.errors) {
                    self.setState({ errors: res.errors, fetching: false });
                }
                else {
                    self.setState({ success: true, wsname: v.wsname, fetching: false });
                }
            })
            .catch(() => self.setState({ fetching: false }));
        */
    }

    workspaceSelected() {
        if (!this.state.workspaceId) {
            this.setState({ errors: { ws: 'Value is required' } });
            return;
        }

        this.setState({ errors: {}, fetching: true });

        setTimeout(() => {
            this.setState({ workspaces: null, fetching: false, importingP: 0 });
            setTimeout(() => {

                this.setState({ importingP: 1 });
                setInterval(() => {
                    const p = this.state.importingP;
                    if (p < 100) {
                        this.setState({ importingP: (p + 1) });
                    } else {
                        this.clearAllIntervals();
                        this.setState({ success: true });
                    }
                }, 100);
            }, 12000);

        }, 800);
    }

    /**
     * Mock progress
     * @return {[type]} [description]
     */
    clearAllIntervals() {
        // clear all intervals
        const id = setInterval(() => console.log('fakeinterval'), 9999);
        for (var i = 0; i <= id; i++) {
            clearInterval(i);
        }
    }

    /**
     * Called when user select a workspace
     */
    workspaceChange(evt) {
        const val = evt.target.value;
        this.setState({ workspaceId: val });
    }

    /**
     * Called when user clicks on the login button
     */
    gotoLogin() {
        app.setState({ login: 'admin' });
        app.goto('/pub/login');
    }

    /**
     * Render the component
     */
    render() {
        const err = this.state.errors || {};
        const fetching = this.state.fetching;

        let content;

        if (this.state.success) {
            content = (
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
        } else if (this.state.importingP > 0) {
            content = (
                <div>
                    <Row>
                        <Col sm={12}>
                            <h4 className="text-center">{__('init.offinit.reading')}</h4>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={12}>
                            <ProgressBar now={this.state.importingP} label={`${this.state.importingP}%`} />
                        </Col>
                    </Row>
                </div>
                );
        } else if (this.state.importingP === 0) {
            content = (
                <div>
                    <Row>
                        <Col sm={12}>
                            <h4 className="text-center">{__('init.offinit.downloading')}</h4>
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
        } else if (this.state.workspaces) {
            content = (
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
        } else {
            content = (
                <div>
                    <Row>
                        <Col sm={12}>
                            <FormGroup validationState={err.serverURL ? 'error' : undefined}>
                                <ControlLabel>{__('init.offinit.url') + ':'}</ControlLabel>
                                <FormControl type="text" ref="serverURL" autoFocus />
                                <HelpBlock>{err.serverURL}</HelpBlock>
                            </FormGroup>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={12}>
                            <FormGroup validationState={err.login ? 'error' : undefined}>
                                <ControlLabel>{__('User.login') + ':'}</ControlLabel>
                                <FormControl type="text" ref="login" />
                                <HelpBlock>{err.login}</HelpBlock>
                            </FormGroup>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={12}>
                            <FormGroup validationState={err.pwd ? 'error' : undefined}>
                                <ControlLabel>{__('User.password') + ':'}</ControlLabel>
                                <FormControl type="password" ref="pwd" />
                                <HelpBlock>{err.pwd}</HelpBlock>
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

        return (
            <Fade in transitionAppear>
                <Grid>
                    <Col sm={6} smOffset={3}>
                        <Card title={__('init.offinit')} className="mtop-2x">
                            {
                                this.state.globalMsg &&
                                <Alert bsStyle="danger">{this.state.globalMsg}</Alert>
                            }
                            {content}
                        </Card>
                    </Col>
                </Grid>
            </Fade>
        );
    }
}
