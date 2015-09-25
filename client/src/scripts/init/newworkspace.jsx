import React from 'react';
import { Grid, Row, Col, Input, Button, Fade, PageHeader } from 'react-bootstrap';
import { registerWs } from './actions';
import { navigator } from '../components/router.jsx';
import Title from '../components/title.jsx';
import { validateForm } from '../commons/validator.jsx';

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
        setTimeout(() => {
            comp.setState({fetching: false});
        }, 3000);
//        navigator.goto('/init/success');
//        this.props.dispatch(registerWs(data));
    }


    /**
     * Render the component
     */
    render() {
        let langs = this.props.appState.app.languages;
        let lg = window.app.getLang();
        var err = this.state.errors || {};

        return (
            <Fade in transitionAppear>
            <Grid>
                <Row>
                    <Col md={6} mdOffset={3}>
                        <Title text='Create a new workspace'></Title>
                    </Col>
                </Row>
                <Row>
                    <Col md={3} mdOffset={3}>
                        <Input type="text" ref="wsname" label="Workspace name:" autoFocus help={err.wsname} bsStyle={err.wsname?'error':undefined}>
                        </Input>
                    </Col>
                </Row>
                <Row>
                    <Col md={3} mdOffset={3}>
                        <Input type="text" label="Administrator user name:" value="Admin" disabled>
                        </Input>
                    </Col>
                    <Col md={3}>
                        <Input type="text" ref="email" label="Administrator e-mail address:" help={err.email} bsStyle={err.email?'error':undefined}>
                        </Input>
                    </Col>
                </Row>
                <Row>
                    <Col md={3} mdOffset={3}>
                        <Input type="password" ref="pwd" label="Administrator password:" help={err.pwd} bsStyle={err.pwd?'error':undefined}>
                        </Input>
                    </Col>
                    <Col md={3}>
                        <Input type="password" ref="pwd2" label="Administrator password (repeat):" help={err.pwd2}  bsStyle={err.pwd2?'error':undefined}>
                        </Input>
                    </Col>
                </Row>
                <Row>
                    <Col md={6} mdOffset={3}>
                        <div className="pull-right">
                            <Button bsStyle="primary" disabled={this.state.fetching} pullRight bsSize='large' onClick={this.contClick}>{__('Create')}
                            </Button>
                        </div>
                    </Col>
                </Row>
            </Grid>
                </Fade>
        );
    }
}