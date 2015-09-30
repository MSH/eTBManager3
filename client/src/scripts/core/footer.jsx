'use strict';

import React from 'react';
import { Grid, Col } from 'react-bootstrap';

/**
 * The home page of the initialization module
 */
export default class Footer extends React.Component {


    render() {
        return (
            <Grid fluid className='footer'>
                <Col md={4} mdOffset={1} className='text-center'>
                    <img src='../images/usaid.png' />
                </Col>
                <Col md={4} mdOffset={2} className='text-center'>
                    <img src='../images/siaps.png' />
                </Col>
            </Grid>
        );
    }
}