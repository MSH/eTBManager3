
import React from 'react';
import { Grid, Row, Col, Input, Fade } from 'react-bootstrap';
import { validateForm } from '../commons/validator';
import Server from '../commons/server';
import Success from './success';
import { Callout, Card, AsyncButton } from '../components/index';


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
                return 'Password is not the same';
            }
        }
    }
};


export default class NewWorkspace extends React.Component {
    constructor(props) {
        super(props);
        this.contClick = this.contClick.bind(this);
        this.state = { data: {} };
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
            adminEmail: v.email
        };

        const self = this;

        // request server to register workspace
        Server.post('/api/init/workspace', data)
            .then(res => {
                if (res.errors) {
                    self.setState({ errors: res.errors, fetching: false });
                }
                else {
                    self.setState({ success: true, wsname: v.wsname, fetching: false });
                }
            });
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
                        <Card title="Create a new workspace">
                            <div>
                                <Row>
                                    <Col sm={6}>
                                        <Input type="text" ref="wsname" label="Workspace name:" autoFocus help={err.wsname} bsStyle={err.wsname ? 'error' : undefined} />
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={12}>
                                        <Callout bsStyle="warning">
                                            <h4>{'Administrator'}</h4>
                                            <p>{'The administrator account is a special user with access to all data and functionalities inside e-TB Manager.' +
                                                'It is recommended that you use this account with caution.'}
                                            </p>
                                        </Callout>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={6}>
                                        <Input type="text" label="Administrator user name:" value="Admin" disabled />
                                    </Col>
                                    <Col sm={6}>
                                        <Input type="text" ref="email" label="Administrator e-mail:" help={err.email} bsStyle={err.email ? 'error' : undefined} />
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={6}>
                                        <Input type="password" ref="pwd" label="Administrator password:" help={err.pwd} bsStyle={err.pwd ? 'error' : undefined} />
                                    </Col>
                                    <Col sm={6}>
                                        <Input type="password" ref="pwd2" label="Administrator password (repeat):" help={err.pwd2} bsStyle={err.pwd2 ? 'error' : undefined} />
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={12}>
                                        <div className="pull-right">
                                            <AsyncButton fetching={fetching} pullRight bsSize="large" onClick={this.contClick}>
                                                {__('Create')}
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
