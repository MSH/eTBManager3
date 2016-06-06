import React from 'react';
import { Grid, Row, Col, Button, FormGroup, FormControl, HelpBlock } from 'react-bootstrap';
import Logo from './logo';


/**
 * Wellcome page - First page displayed under e-TB Manager first time execution
 */
export default class ForgotPwd extends React.Component {

    constructor(props) {
        super(props);
        this.submit = this.submit.bind(this);
        this.state = {};
    }

    submit() {
        console.log('hi');
    }

    /**
     * Render the component
     */
    render() {
        const err = this.state.err;

        return (
            <Logo backLink>
                <Row>
                    <Col md={12} className="text-center">
                        <h3>{__('forgotpwd')}</h3>

                        <p className="text-muted">
                            {__('forgotpwd.msg')}
                        </p>

                        <FormGroup validationState={err ? 'error' : undefined} bsSize="large">
                            <FormControl type="text" ref="email" placeholder={__('forgotpwd.emailuser')} autoFocus />
                            {err && <HelpBlock>{err}</HelpBlock>}
                        </FormGroup>
                    </Col>
                </Row>
                <Row className="mtop-2x">
                    <Col md={12}>
                        <Button block bsStyle="primary" bsSize="large" onClick={this.submit}>
                        {__('action.submit')}
                        </Button>
                    </Col>
                </Row>
            </Logo>
        );
    }
}
