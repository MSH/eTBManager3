
import React from 'react';
import { Row, Col, Alert, FormGroup, FormControl, InputGroup, Checkbox } from 'react-bootstrap';
import AsyncButton from '../components/async-button';
import Fa from '../components/fa';
import Error from '../components/error';
import { validateForm } from '../commons/validator';
import { app } from '../core/app';
import { server } from '../commons/server';
import Logo from './logo';
import BorderlessForm from './borderless-form';


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

    /**
     * Called when user clicks on the continue button
     */
    loginClick() {
        const res = validateForm(this, formModel);

        // is there any validation error ?
        if (res.errors) {
            this.setState({ errors: res.errors });
            return;
        }

        // hide messages and disable button
        this.setState({ invalid: false, errors: null, fetching: true });
        const val = res.value;

        const self = this;

        // request login to the server
        this.login(val.user, val.pwd)
        .then(data => {
            if (data) {
                app.goto('/sys/home/workspace/cases');
            }
            else {
                self.setState({ fetching: false, invalid: true });
            }
        })
        .catch(err => {
            self.setState({ fetching: false });
            return Promise.reject(err);
        });
    }

    /**
     * Perform login into the system. Returns a promise that will indicate if user
     * @param  {String} user the user account
     * @param  {String} pwd  the user password
     * @return {Promise}      Promise that will be resolved with the authentication token, or null if failed
     */
    login(user, pwd) {
        return server.post('/api/auth/login', { username: user, password: pwd })
        .then(data => {
            if (!data.success) {
                return null;
            }

            // register the authentication token in the cookies
            const authToken = data.authToken;
            app.setCookie('autk', authToken);
            return authToken;
        });
    }


    /**
     * Render the component
     */
    render() {
        const st = this.state;
        const err = st && st.errors || {};
        const fetching = st && st.fetching;
        const login = app.getState().login || '';
        const allowRegPage = app.getState().app.allowRegPage;

        return (
                <Logo>
                    <div>
                    <Row>
                        <Col md={12}>
                            <div className="text-center">
                                <p className="text-muted">{__('login.msg1')}</p>
                            </div>
                            <BorderlessForm>
                                <FormGroup validationState={err.user ? 'error' : undefined} >
                                    <InputGroup>
                                        <InputGroup.Addon>
                                            <Fa icon="user" />
                                        </InputGroup.Addon>
                                        <FormControl type="text"
                                            ref="user"
                                            placeholder={__('User.login')} autoFocus={!login}
                                            defaultValue={login}
                                            />
                                    </InputGroup>
                                    <Error msg={err.user} />
                                </FormGroup>

                                <FormGroup validationState={err.pwd ? 'error' : undefined} >
                                    <InputGroup>
                                        <InputGroup.Addon>
                                            <Fa icon="key" />
                                        </InputGroup.Addon>
                                        <FormControl type="password"
                                            autoFocus={!!login}
                                            ref="pwd"
                                            placeholder={__('User.password')}
                                            />
                                    </InputGroup>
                                    <Error msg={err.pwd} />
                                </FormGroup>
                            </BorderlessForm>
                        </Col>
                    </Row>
                    {st && st.invalid && (
                        <Row >
                            <Col sm={12}>
                                <Alert bsStyle="danger">
                                    {__('login.invaliduserpwd')}
                                </Alert>
                            </Col>
                        </Row>
                    )}
                    <Row>
                        <Col sm={12}>
                            <Checkbox>{__('login.rememberme')}</Checkbox>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={12}>
                            <AsyncButton block onClick={this.loginClick}
                                bsSize="large"
                                fetching={fetching} fetchCaption={__('action.entering')}>
                                {__('action.enter')}
                            </AsyncButton>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={12} className="text-center mtop-2x">
                            <a href="#/pub/forgotpwd">{__('forgotpwd')}</a>
                        </Col>
                    </Row>
                    {
                        allowRegPage &&
                        <Row>
                            <Col sm={12} className="mtop-2x">
                                <a className="btn btn-block btn-lg btn-default" href="#/pub/selfreg">
                                    {__('userreg')}
                                </a>
                            </Col>
                        </Row>
                    }
                    </div>
                </Logo>
        );
    }
}
