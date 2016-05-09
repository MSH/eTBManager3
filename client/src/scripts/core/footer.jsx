
import React from 'react';
import { Grid, Col } from 'react-bootstrap';

/**
 * The home page of the initialization module
 */
export default class Footer extends React.Component {


    render() {
        const path = window.app.contextPath + '/images/';

        return (
            <Grid fluid className="footer">
                <Col md={4} mdOffset={1} className="text-center">
                    <img src={path + 'usaid.png'} />
                </Col>
                <Col md={4} mdOffset={2} className="text-center">
                    <img src={path + 'siaps.png'} />
                </Col>
            </Grid>
        );
    }
}
