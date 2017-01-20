import React from 'react';
import { Fade, Grid, Row, Col } from 'react-bootstrap';
import Fa from '../components/fa';


import './logo.less';

export default function Logo(props) {
    const path = window.app.contextPath + '/images/';

    return (
        <div className="logo container central-container-md">
            <div className="text-center">
                <img src={path + 'etbm_icon_128x128.png'}/>
                <div className="title">{'e-TB Manager'}
                    <div style={{ fontSize: '0.4em' }}>{'version 3.0-beta'}</div>
                </div>
            </div>
            <div className="mtop-2x">
            <Fade in transitionAppear>
                <Grid fluid>
                    {props.children}
                    {
                        props.backLink &&
                        <Row className="mtop-2x">
                            <Col md={12}>
                                <a href="#/pub/login" className="text-small">
                                    <Fa icon="chevron-left" />{__('login.back')}
                                </a>
                            </Col>
                        </Row>
                    }
                </Grid>
            </Fade>
            </div>
        </div>
    );
}

Logo.propTypes = {
    children: React.PropTypes.any,
    backLink: React.PropTypes.bool
};
