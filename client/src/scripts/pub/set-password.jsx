import React from 'react';
import { FormGroup, InputGroup, FormControl, Alert } from 'react-bootstrap';
import Fa from '../components/fa';
import Profile from '../components/profile';
import AsyncButton from '../components/async-button';
import WaitIcon from '../components/wait-icon';
import Card from '../components/card';
import Error from '../components/error';
import { server } from '../commons/server';
import { app } from '../core/app';
import Logo from './logo';
import BorderlessForm from './borderless-form';
import { validateForm } from '../commons/validator';


/**
 * Values to be validated
 */
const form = {
    pwd1: {
        required: true,
        password: true
    },
    pwd2: {
        required: true,
        validate: doc => doc.pwd1 === doc.pwd2 ? null : __('changepwd.wrongpass2')
    }
};

export default class SetPassword extends React.Component {

    constructor(props) {
        super(props);
        this.submit = this.submit.bind(this);
        this.state = { };
    }

    componentWillMount() {
        this.validateToken(this.props.token);
    }

    componentWillUpdate(newProps) {
        if (newProps.token !== this.props.token) {
            this.validateToken(newProps.token);
        }
    }

    validateToken(token) {
        const self = this;

        // post request to the server
        server.post('/api/pub/pwdtokeninfo/' + token)
        .then(res => {
            if (res) {
                self.setState({ info: res, invalid: false });
            }
            else {
                self.setState({ invalid: true });
            }
        });

        this.setState({ info: null, invalid: null });
    }

    /**
     * Submit the new password
     * @return {[type]} [description]
     */
    submit() {
        const data = validateForm(this, form);
        if (data.errors) {
            this.setState({ err: data.errors });
            return;
        }

        const params = {
            token: this.props.token,
            password: data.value.pwd1
        };

        // request password update
        const self = this;
        server.post('/api/pub/updatepwd', params)
        .then(res => {
            if (res.success) {
                self.setState({ success: true, fetching: false });
                app.setState({ login: this.state.info.login });
            }
            else {
                self.setState({ invalid: true, fetching: false });
            }
        });

        this.setState({ err: null, fetching: true });
    }

    /**
     * Render when the token is invalid
     * @return {React.Component} Component to be displayed
     */
    renderInvalid() {
        return (
            <Logo backLink>
            <Alert bsStyle="danger">
                <h3>
                    {__('changepwd.invalidtoken')}
                </h3>
            </Alert>
            </Logo>
        );
    }

    renderSuccess() {
        return (
            <Logo backLink>
            <Card title={__('changepwd')}>
                <div className="text-center">
                    <div className="text-primary">
                    <Fa icon="check-circle" size={3} />
                    <h1>{__('global.success')}</h1>
                    </div>
                    <p>
                        {__('changepwd.success1')}
                    </p>
                </div>
            </Card>
            </Logo>
        );
    }

    render() {
        if (this.state.invalid) {
            return this.renderInvalid();
        }

        if (this.state.success) {
            return this.renderSuccess();
        }

        const info = this.state.info;
        if (!info) {
            return <WaitIcon type="card" />;
        }

        const err = this.state.err || {};

        return (
            <Logo>
                <div className="text-center">
                    <h3>{this.props.title}</h3>
                    {
                        this.props.comments &&
                        <p>
                            {this.props.comments}
                        </p>
                    }
                    <BorderlessForm>
                        <FormGroup>
                            <div className="text-left">
                                <Profile type="user"
                                    title={info.name}
                                    subtitle={info.login}
                                    size="small" />
                            </div>
                        </FormGroup>

                        <FormGroup validationState={err.pwd1 ? 'error' : undefined} >
                            <InputGroup>
                                <InputGroup.Addon>
                                    <Fa icon="key" />
                                </InputGroup.Addon>
                                <FormControl type="password"
                                    ref="pwd1"
                                    placeholder={__('changepwd.newpass')} autoFocus
                                    />
                            </InputGroup>
                            <Error msg={err.pwd1} />
                        </FormGroup>

                        <FormGroup validationState={err.pwd2 ? 'error' : undefined} >
                            <InputGroup>
                                <InputGroup.Addon>
                                    <Fa icon="key" />
                                </InputGroup.Addon>
                                <FormControl type="password"
                                    ref="pwd2"
                                    placeholder={__('changepwd.newpass2')}
                                    />
                            </InputGroup>
                            <Error msg={err.pwd2} />
                        </FormGroup>

                    </BorderlessForm>

                    <AsyncButton bsStyle="primary"
                        bsSize="large"
                        block
                        fetching={this.state.fetching}
                        onClick={this.submit}>
                        {__('action.submit')}
                    </AsyncButton>
                </div>
            </Logo>
        );
    }
}

SetPassword.propTypes = {
    token: React.PropTypes.string.isRequired,
    title: React.PropTypes.string.isRequired,
    comments: React.PropTypes.string
};
