import React from 'react';
import { Popover, ButtonGroup, Button, OverlayTrigger } from 'react-bootstrap';
import { Card, Fa } from '../../../../components';
import { app } from '../../../../core/app';
import StartStandRegimen from './start-stand-regimen';

/**
 * Display a card informing the treatment has not started, with a command
 * to start a treatment by a standardized or individualized regimen
 */
export default class NoTreatPanel extends React.Component {

	constructor(props) {
		super(props);

		this.startStandard = this.startStandard.bind(this);
		this.startIndiv = this.startIndiv.bind(this);
		this.closeDialog = this.closeDialog.bind(this);
		this.state = {};
	}

	startStandard() {
		this.setState({ openStandard: true });
	}

	startIndiv() {
		this.setState({ openIndiv: true });
	}

	closeDialog(res) {
		if (res) {
			app.dispatch('case_update');
		}
		this.setState({ openIndiv: null, openStandard: null });
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
				{
					this.state.openStandard &&
					<StartStandRegimen tbcase={this.props.tbcase} onClose={this.closeDialog} />
				}
			</Card>
			);
	}
}

NoTreatPanel.propTypes = {
	tbcase: React.PropTypes.object.isRequired
};
