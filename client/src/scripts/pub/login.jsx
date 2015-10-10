import React from 'react';
import { Grid, Row, Col, Input, Button, Fade } from 'react-bootstrap';
import { navigator } from '../components/router.jsx';
import Title from '../components/title.jsx';
import Card from '../components/card.jsx';
import { validateForm } from '../commons/validator.jsx';
import Http from '../commons/http.js';
import AsyncButton from '../components/async-button.jsx';


/**
 * Values to be validated
 */
let formModel = {
    user: {
        required: true
    },
    pwd: {
        required: true
    }
}

/**
 * Wellcome page - First page displayed under e-TB Manager first time execution
 */
export default class Login extends React.Component {
    constructor(props) {
        super(props);
        this.loginClick = this.loginClick.bind(this);
    }


    forgotPwdClick() {
        navigator.goto('/pub/forgotpwd');
    }

    /**
     * Called when user clicks on the continue button
     */
    loginClick() {
        let res = validateForm(this, formModel);
        if (res.errors) {
            this.setState({errors: res.errors });
            return;
        }

        this.setState({errors: {}, fetching: true});
        let val = res.value;

        let comp = this;
        // post to the server
        Http.post('/api/auth/login', {username: val.user, password: val.pwd}, (err, res) => {
            comp.setState({ fetching: false });
            if (err || (res.body && !res.body.success)) {
                return;
            }

            let authToken = res.body.authToken;
            window.app.setCookie('autk', authToken);
            navigator.goto('/sys/home');
        });
    }

    /**
     * Render the component
     */
    render() {
        let st = this.state;
        let err = st && st.errors || {};
        let fetching = st && st.fetching;

        let iconUser = (<i className='fa fa-user fa-fw'></i>);
        let iconPwd = (<i className='fa fa-key fa-fw'></i>);

        return (
            <Fade in transitionAppear>
                <div className='container central-container-md'>
                    <Card title='Please login'>
                        <div>
                            <Row>
                                <Col sm={12}>
                                    <Input type='text' ref='user' placeholder='User name' autoFocus
                                           addonBefore={iconUser}
                                           help={err.user} bsStyle={err.user?'error':undefined}/>
                                </Col>
                            </Row>
                            <Row>
                                <Col sm={12}>
                                    <Input type='password' ref='pwd' placeholder='Password'
                                           addonBefore={iconPwd}
                                           help={err.pwd} bsStyle={err.pwd?'error':undefined}/>
                                </Col>
                            </Row>
                            <Row>
                                <Col sm={12}>
                                    <Input type='checkbox' label='Remember me' />
                                </Col>
                            </Row>
                            <Row>
                                <Col sm={12}>
                                    <AsyncButton block onClick={this.loginClick} fetching={fetching} fetchCaption='Entering...'>
                                        Enter
                                    </AsyncButton>
                                </Col>
                            </Row>
                            <Row>
                                <hr/>
                            </Row>
                            <Row>
                                <Col sm={12}>
                                    <Button bsStyle='link' onClick={this.forgotPwdClick}>Forgot your password?</Button>
                                </Col>
                            </Row>
                            <Row>
                                <Col sm={12}>
                                    <p className='mtop-2x'>Don't you have an user name?</p>
                                    <Button bsStyle="default" block>Create an account</Button>
                                </Col>
                            </Row>
                        </div>
                    </Card>
                </div>
            </Fade>
        );
    }
}