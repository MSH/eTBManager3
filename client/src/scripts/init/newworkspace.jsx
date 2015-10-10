import React from 'react';
import { Grid, Row, Col, Input, Button, Fade, PageHeader } from 'react-bootstrap';
import { postSuccess } from './actions';
import { navigator } from '../components/router.jsx';
import Title from '../components/title.jsx';
import { validateForm } from '../commons/validator.jsx';
import Card from '../components/card.jsx';
import Http from '../commons/http.js';
import AsyncButton from '../components/async-button.jsx';

/**
 * Form validation model
 */
let form = {
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
                return "Password is not the same";
            }
        }
    }
}


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
        let res = validateForm(this, form);

        // there is any validation error ?
        if (res.errors) {
            this.setState({errors: res.errors});
            return;
        }
        else {
            this.setState({errors: {}, fetching: true});
        }

        let v = res.value;
        let data = {
            workspaceName: v.wsname,
            adminPassword: v.pwd,
            adminEmail: v.email
        };

        let comp = this;

        Http.post('/api/init/workspace', data)
            .end((err, res) => {
                comp.setState({fetching: false});
                if (!err) {
                    comp.props.dispatch(postSuccess(v.wsname));
                }
            });
    }


    /**
     * Render the component
     */
    render() {
        let langs = this.props.appState.app.languages;
        let lg = window.app.getLang();
        let err = this.state.errors || {};
        let fetching = this.state.fetching;

        return (
            <Fade in transitionAppear>
                <Grid fluid>
                    <Col sm={8} smOffset={2} lg={8} lgOffset={2} >
                        <Card title='Create a new workspace'>
                            <div>
                                <Row>
                                    <Col sm={6}>
                                        <Input type="text" ref="wsname" label="Workspace name:" autoFocus help={err.wsname} bsStyle={err.wsname?'error':undefined}>
                                        </Input>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={12}>
                                        <div className='bs-callout bs-callout-warning'>
                                            <h4>Administrator</h4>
                                            <p>The administrator account is a special user with access to all data and functionalities inside e-TB Manager.
                                                It is recommended that you use this account with caution.
                                            </p>
                                        </div>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={6}>
                                        <Input type="text" label="Administrator user name:" value="Admin" disabled>
                                        </Input>
                                    </Col>
                                    <Col sm={6}>
                                        <Input type="text" ref="email" label="Administrator e-mail:" help={err.email} bsStyle={err.email?'error':undefined}>
                                        </Input>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={6}>
                                        <Input type="password" ref="pwd" label="Administrator password:" help={err.pwd} bsStyle={err.pwd?'error':undefined}>
                                        </Input>
                                    </Col>
                                    <Col sm={6}>
                                        <Input type="password" ref="pwd2" label="Administrator password (repeat):" help={err.pwd2}  bsStyle={err.pwd2?'error':undefined}>
                                        </Input>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col sm={12}>
                                        <div className="pull-right">
                                            <AsyncButton fetching={fetching} pullRight bsSize='large' onClick={this.contClick}>
                                                {__('Create')}
                                            </AsyncButton>
                                        </div>
                                    </Col>
                                </Row>
                            </div>
                        </Card>
                    </Col>
                </Grid>
            </Fade>
        );
    }
}