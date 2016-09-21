import React from 'react';
import { Row, Col, FormGroup, FormControl, HelpBlock, Alert } from 'react-bootstrap';
import Logo from './logo';
import AsyncButton from '../components/async-button';
import Fa from '../components/fa';
import Card from '../components/card';
import { server } from '../commons/server';


/**
 * Wellcome page - First page displayed under e-TB Manager first time execution
 */
export default class ForgotPwd extends React.Component {

    constructor(props) {
        super(props);
        this.submit = this.submit.bind(this);
        this.inputChange = this.inputChange.bind(this);
        this.state = { };
    }

    submit() {
        const self = this;
        server.post('/api/pub/forgotpwd?id=' + this.state.value)
        .then(res => {
            self.setState({ fetching: null, requested: true, success: res.success });
        })
        .catch(() => self.setState({ fetching: null, requested: false }));

        this.setState({ fetching: true });
    }

    inputChange(evt) {
        this.setState({ value: evt.target.value });
    }

    /**
     * Render the component
     */
    render() {
        const err = this.state.err;
        const val = this.state.value ? this.state.value : '';
        const title = __('forgotpwd');

        return (
            <Logo backLink>
                {
                    !this.state.success ?
                    <div>
                        <Row>
                            <Col md={12} className="text-center">
                                <h3>{title}</h3>

                                <p className="text-muted">
                                    {__('forgotpwd.msg')}
                                </p>

                                <FormGroup validationState={err ? 'error' : undefined} bsSize="large">
                                    <FormControl type="text" ref="email"
                                        value={val}
                                        onChange={this.inputChange}
                                        placeholder={__('forgotpwd.emailuser')}
                                        autoFocus />
                                    {err && <HelpBlock>{err}</HelpBlock>}
                                </FormGroup>
                                {
                                    this.state.requested &&
                                    <Alert bsStyle="danger">{__('forgotpwd.invalid')}</Alert>
                                }
                            </Col>
                        </Row>
                        <Row className="mtop-2x">
                            <Col md={12}>
                                <AsyncButton block bsStyle="primary" bsSize="large"
                                    onClick={this.submit}
                                    disabled={val.length < 4}
                                    fetching={this.state.fetching}>
                                {__('action.submit')}
                                </AsyncButton>
                            </Col>
                        </Row>
                    </div> :
                    <div>
                        <Card title={title}>
                            <div className="text-center">
                                <div className="text-primary">
                                    <Fa icon="check-circle" size={3} />
                                    <h1>{__('global.success')}</h1>
                                </div>
                                <p>
                                    {__('forgotpwd.success.1')}
                                </p>
                            </div>
                        </Card>
                    </div>
                }
            </Logo>
        );
    }
}
