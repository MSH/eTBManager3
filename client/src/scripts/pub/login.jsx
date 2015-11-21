import React from 'react';
import { Row, Col, Input, Button, Fade, Alert } from 'react-bootstrap';
import Card from '../components/card.jsx';
import { validateForm } from '../commons/validator.jsx';
import Server from '../commons/server.js';
import AsyncButton from '../components/async-button.jsx';
import { LOGIN } from '../core/actions';


/**
 * Values to be validated
 */
const formModel = {
    user: {
        required: true
    },
    pwd: {
        required: true
    }
};

/**
 * Wellcome page - First page displayed under e-TB Manager first time execution
 */
export default class Login extends React.Component {
    constructor(props) {
        super(props);
        this.loginClick = this.loginClick.bind(this);
    }


    forgotPwdClick() {
        this.props.app.goto('/pub/forgotpwd');
    }

    /**
     * Called when user clicks on the continue button
     */
    loginClick() {
        const res = validateForm(this, formModel);

        if (res.errors) {
            this.setState({ errors: res.errors });
            return;
        }

        // hide messages and disable button
        this.setState({ invalid: false, errors: null, fetching: true });
        const val = res.value;
        const app = this.props.app;

        const self = this;
        // post to the server
        Server.post('/api/auth/login', { username: val.user, password: val.pwd })
        .then(data => {
            if (data.success) {
                const authToken = data.authToken;
                window.app.setCookie('autk', authToken);
                app.goto('/sys/home');
            }
            else {
                self.setState({ fetching: false, invalid: true });
                app.dispatch(LOGIN);
            }
        });
    }

    /**
     * Render the component
     */
    render() {
        const st = this.state;
        const err = st && st.errors || {};
        const fetching = st && st.fetching;

        const iconUser = (<i className="fa fa-user fa-fw"></i>);
        const iconPwd = (<i className="fa fa-key fa-fw"></i>);

        return (
            <Fade in transitionAppear>
                <div className="container central-container-md">
                    <Card title="Please login">
                        <div>
                            <Row>
                                <Col sm={12}>
                                    <Input type="text" ref="user" placeholder="User name" autoFocus
                                        addonBefore={iconUser}
                                        help={err.user} bsStyle={err.user ? 'error' : undefined}
                                    />
                                </Col>
                            </Row>
                            <Row>
                                <Col sm={12}>
                                    <Input type="password" ref="pwd" placeholder="Password"
                                        addonBefore={iconPwd}
                                        help={err.pwd} bsStyle={err.pwd ? 'error' : undefined}/>
                                </Col>
                            </Row>
                            <Row>
                                <Col sm={12}>
                                    <Input type="checkbox" label="Remember me" />
                                </Col>
                            </Row>
                            {st && st.invalid && (
                            <Row >
                                <Col sm={12}>
                                    <Alert bsStyle="danger">
                                        {'Invalid user name or password'}
                                    </Alert>
                                </Col>
                            </Row>
                            )}
                            <Row>
                                <Col sm={12}>
                                    <AsyncButton block onClick={this.loginClick}
                                        fetching={fetching} fetchCaption="Entering...">
                                        {'Enter'}
                                    </AsyncButton>
                                </Col>
                            </Row>
                            <Row>
                                <hr/>
                            </Row>
                            <Row>
                                <Col sm={12}>
                                    <Button bsStyle="link" onClick={this.forgotPwdClick}>{'Forgot your password?'}</Button>
                                </Col>
                            </Row>
                            <Row>
                                <Col sm={12}>
                                    <p className="mtop-2x">{'Don\'t you have an user name?'}</p>
                                    <Button bsStyle="default" block>{'Create an account'}</Button>
                                </Col>
                            </Row>
                        </div>
                    </Card>
                </div>
            </Fade>
        );
    }
}

Login.propTypes = {
    app: React.PropTypes.object
};
