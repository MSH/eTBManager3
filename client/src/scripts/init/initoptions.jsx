
import React from 'react';
import { Grid, Row, Col, Input, Button, Fade, OverlayTrigger, Popover } from 'react-bootstrap';
import { Card } from '../components/index';
import { app } from '../core/app';


export default class InitOptions extends React.Component {
    constructor(props) {
        super(props);
        this.contClick = this.contClick.bind(this);
    }

    /**
     * Called when user clicks on the continue button
     */
    contClick() {
        app.goto('/init/newworkspace');
    }


    /**
     * Render the component
     */
    render() {
        return (
            <Fade in transitionAppear>
                <Grid>
                    <Col sm={6} smOffset={3}>
                    <Card title={__('init.initoptions')}>
                        <div>
                            <OverlayTrigger trigger="focus" placement="bottom"
                                overlay={<Popover id="id1">It will start a fresh new instance of e-TB Manager from scratch</Popover>}>
                                <Input type="radio" name="sel" label={__('init.newworkspace')} />
                            </OverlayTrigger>
                            <OverlayTrigger trigger="focus" placement="bottom"
                                overlay={<Popover id="id2">This instance will be in sync with another e-TB Manager</Popover>}>
                                <Input type="radio" name="sel" label={__('init.initoptions.sync')} />
                            </OverlayTrigger>
                            <OverlayTrigger trigger="focus" placement="bottom"
                                overlay={<Popover id="id3">Data will be imported from a previous version of e-TB Manager just this first time</Popover>}>
                                <Input type="radio" name="sel" label={__('init.initoptions.import')} />
                            </OverlayTrigger>
                            <Row>
                                <Col sm={12}>
                                    <div className="pull-right">
                                        <Button bsStyle="primary" pullRight bsSize="large" onClick={this.contClick}>
                                            {__('action.continue')}
                                        </Button>
                                    </div>
                                </Col>
                            </Row>
                        </div>
                    </Card>
                        </Col>
                </Grid>
            </Fade>
        );
    }
}

