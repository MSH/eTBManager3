
import React from 'react';
import { Grid, Row, Col, Fade, FormControl, FormGroup, ControlLabel, HelpBlock } from 'react-bootstrap';
import { validateForm } from '../commons/validator';
import { server } from '../commons/server';
import Success from './success';
import Callout from '../components/callout';
import Card from '../components/card';
import YesNoControl from '../sys/packages/types/yesno-control';
import AsyncButton from '../components/async-button';


/**
 * Form validation model
 */
const form = {
    wsname: {
        required: true,
        min: 3,
        max: 250
    },
    email: {
        required: true,
        email: true
    },
    pwd: {
        required: true,
        password: true
    },
    pwd2: {
        required: true,
        validate: function() {
            if (this.pwd2 !== this.pwd) {
                return __('validation.pwdNotSame');
            }
            return null;
        }
    }
};


export default class NewWorkspace extends React.Component {
    constructor(props) {
        super(props);
        this.contClick = this.contClick.bind(this);
        this.onDemoChange = this.onDemoChange.bind(this);
        this.state = { data: { demoData: true } };
    }

    /**
     * Called when user clicks on the continue button
     */
    contClick() {
        const vals = validateForm(this, form);

        // there is any validation error ?
        if (vals.errors) {
            this.setState({ errors: vals.errors });
            return;
        }

        this.setState({ errors: {}, fetching: true });

        const v = vals.value;
        const data = {
            workspaceName: v.wsname,
            adminPassword: v.pwd,
            adminEmail: v.email,
            demoData: this.state.data.demoData
        };

        const self = this;

        // request server to register workspace
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
    }

    onDemoChange(comp) {
        const data = this.state.data;
        data.demoData = comp.value;
        this.setState({ data: data });
    }

    /**
     * Render the component
     */
    render() {
        const err = this.state.errors || {};
        const fetching = this.state.fetching;
        const success = this.state.success;

        let content;

        if (success) {
            content = <Success wsname={this.state.wsname}/>;
        }
        else {
            content = (
                <Grid fluid>
                    <Col sm={8} smOffset={2} lg={8} lgOffset={2} >
                        <Card title={__('init.ws.create')} className="mtop-2x">
                            <div>
                                <Row>
                                    <Col sm={6}>
                                        <FormGroup validationState={err.wsname ? 'error' : undefined}>
                                            <ControlLabel>{__('init.ws.name') + ':'}</ControlLabel>
                                            <FormControl type="text" ref="wsname" autoFocus />
                                            <HelpBlock>{err.wsname}</HelpBlock>
                                        </FormGroup>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={12}>
                                        <Callout bsStyle="warning">
                                            <h4>{'Administrator'}</h4>
                                            <p>{__('init.ws.admininfo')}
                                            </p>
                                        </Callout>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={6}>
                                        <FormGroup>
                                            <ControlLabel>{__('init.ws.adminname') + ':'}</ControlLabel>
                                            <FormControl type="text" disabled value="ADMIN" />
                                        </FormGroup>
                                    </Col>
                                    <Col sm={6}>
                                        <FormGroup validationState={err.email ? 'error' : undefined}>
                                            <ControlLabel>{__('init.ws.adminemail') + ':'}</ControlLabel>
                                            <FormControl type="text" ref="email" />
                                            <HelpBlock>{err.email}</HelpBlock>
                                        </FormGroup>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={6}>
                                        <FormGroup validationState={err.pwd ? 'error' : undefined}>
                                            <ControlLabel>{__('init.ws.adminpwd') + ':'}</ControlLabel>
                                            <FormControl type="password" ref="pwd" />
                                            <HelpBlock>{err.pwd}</HelpBlock>
                                        </FormGroup>
                                    </Col>
                                    <Col sm={6}>
                                        <FormGroup validationState={err.pwd2 ? 'error' : undefined}>
                                            <ControlLabel>{__('init.ws.adminpwd2') + ':'}</ControlLabel>
                                            <FormControl type="password" ref="pwd2" />
                                            <HelpBlock>{err.pwd2}</HelpBlock>
                                        </FormGroup>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={6}>
                                        <FormGroup validationState={err.demoData ? 'error' : undefined}>
                                            <ControlLabel>{'Include demonstration data?'}</ControlLabel>
                                            <YesNoControl ref="demoData" value={this.state.data.demoData} onChange={this.onDemoChange} />
                                            <HelpBlock>{err.demoData}</HelpBlock>
                                        </FormGroup>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={12}>
                                        <div className="pull-right">
                                            <AsyncButton fetching={fetching} bsSize="large" onClick={this.contClick}>
                                                {__('action.create')}
                                            </AsyncButton>
                                        </div>
                                    </Col>
                                </Row>
                            </div>
                        </Card>
                    </Col>
                </Grid>
            );
        }

        return (
            <Fade in transitionAppear>
                {content}
            </Fade>
        );
    }
}
