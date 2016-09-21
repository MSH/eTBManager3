import React from 'react';
import { Popover, ButtonGroup, Button, OverlayTrigger } from 'react-bootstrap';
import { Card, Fa } from '../../../../components';
import { app } from '../../../../core/app';
import StartRegimen from './start-regimen';
import Events from '../events';

/**
 * Display a card informing the treatment has not started, with a command
 * to start a treatment by a standardized or individualized regimen
 */
export default class NoTreatPanel extends React.Component {

    constructor(props) {
        super(props);

        this.startStandard = this.startStandard.bind(this);
        this.startIndiv = this.startIndiv.bind(this);
        this.state = {};
    }

    startStandard() {
        app.dispatch(Events.startStandardRegimen, this.props.tbcase);
    }

    startIndiv() {
        app.dispatch(Events.startInvidRegimen, this.props.tbcase);
    }

    render() {
        const popup = (
                <Popover id="ppmenu">
                    <ButtonGroup vertical>
                        <Button bsStyle="link" onClick={this.startStandard}>{__('regimens.standard')}</Button>
                        <Button bsStyle="link" onClick={this.startIndiv}>{__('regimens.individualized')}</Button>
                    </ButtonGroup>
                </Popover>
            );

        return (
            <Card>
                <Fa icon="exclamation-triangle" className="text-warning"/>
                <span className="text-muted">
                {__('cases.details.notreatment')}
                </span>
                <div className="mtop">
                    <OverlayTrigger key="ppstarttreat" trigger="click" placement="bottom"
                        overlay={popup} rootClose>
                            <Button bsSize="large">{__('cases.details.starttreatment')}</Button>
                    </OverlayTrigger>
                </div>
                <StartRegimen />
            </Card>
            );
    }
}

NoTreatPanel.propTypes = {
    tbcase: React.PropTypes.object.isRequired
};
