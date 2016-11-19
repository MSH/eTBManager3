
import React from 'react';
import { Grid, Col, Fade } from 'react-bootstrap';
import { Card, BigButton } from '../components';
import { app } from '../core/app';


export default class InitOptions extends React.Component {
    constructor(props) {
        super(props);
    }

    /**
     * Render the component
     */
    render() {
        const newWorkspaceFunc = () => app.goto('/init/newworkspace');
        const offlineInitFunc = () => app.goto('/init/offlineinit');

        return (
                <Fade in transitionAppear>
                    <Grid>
                        <Col sm={6} smOffset={3}>
                            <Card title={__('init.initoptions')} className="mtop-2x">
                                <div>
                                    <BigButton icon="globe"
                                        onClick={newWorkspaceFunc}
                                        title={__('init.initoptions.new')}
                                        description={__('init.initoptions.new.desc')} />

                                    <BigButton icon="cloud-upload"
                                        onClick={offlineInitFunc}
                                        title={__('init.initoptions.offinit')}
                                        description={__('init.initoptions.offinit.desc')} />
                                </div>
                            </Card>
                        </Col>
                    </Grid>
                </Fade>
        );
    }
}

