
import React from 'react';
import { Grid, Row, Col, Input, Button, Fade, OverlayTrigger, Popover } from 'react-bootstrap';
import Card from '../components/card.jsx';


export default class InitOptions extends React.Component {
    constructor(props) {
        super(props);
        this.contClick = this.contClick.bind(this);
    }

    /**
     * Called when user clicks on the continue button
     */
    contClick() {
        this.props.app.goto('/init/newworkspace');
    }


    /**
     * Render the component
     */
    render() {
        return (
            <Fade in transitionAppear>
                <Grid>
                    <Col sm={6} smOffset={3}>
                    <Card title={__('e-TB Manager initialization')}>
                        <div>
                                    <OverlayTrigger trigger="focus" placement="bottom"
                                        overlay={<Popover id="id1">It will start a fresh new instance of e-TB Manager from scratch</Popover>}>
                                        <Input type="radio" name="sel" label={__('Start e-TB Manager with a new workspace')} />
                                    </OverlayTrigger>
                                    <OverlayTrigger trigger="focus" placement="bottom"
                                        overlay={<Popover id="id2">This instance will be in sync with another e-TB Manager</Popover>}>
                                        <Input type="radio" name="sel" label={__('I already have an e-TB Manager account')} />
                                    </OverlayTrigger>
                                    <OverlayTrigger trigger="focus" placement="bottom"
                                        overlay={<Popover id="id3">Data will be imported from a previous version of e-TB Manager just this first time</Popover>}>
                                        <Input type="radio" name="sel" label={__('Import database from previous e-TB Manager version')} />
                                    </OverlayTrigger>
                            <Row>
                                <Col sm={12}>
                                    <div className="pull-right">
                                        <Button bsStyle="primary" pullRight bsSize="large" onClick={this.contClick}>
                                            {__('Continue')}
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


InitOptions.propTypes = {
    app: React.PropTypes.object
};
