import React from 'react';
import { Grid, Row, Col, Input, Button, Fade } from 'react-bootstrap';
import { navigator } from '../components/router.jsx';
import Title from '../components/title.jsx';


/**
 * Wellcome page - First page displayed under e-TB Manager first time execution
 */
export default class Welcome extends React.Component {
    constructor(props) {
        super(props);
    }

    /**
     * Called when user clicks on the continue button
     */
    loginClick() {
        navigator.goto('/init/initoptions');
    }

    /**
     * Render the component
     */
    render() {
        return (
            <Fade in transitionAppear>
                <Grid>
                    <Row>
                        <Col md={6} mdOffset={3}>
                            <Title text='Login'></Title>
                        </Col>
                    </Row>
                </Grid>
            </Fade>
        );
    }
}