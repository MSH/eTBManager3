import React from 'react';
import { Button } from 'react-bootstrap';
import { Card, Fa, Callout } from '../../../../components';
import { app } from '../../../../core/app';

export default class NoTreatPanel extends React.Component {

	startTreatClick() {
		app.messageDlg({
			title: 'Not implemented',
			message: 'Please wait for the next version',
			style: 'info',
			type: 'ok'
		});
	}

	render() {
		return (
			<Card>
				<Fa icon="exclamation-triangle" className="text-warning"/>
				<span className="text-muted">
				{__('cases.details.notreatment')}
				</span>
				<div className="mtop">
					<Button bsSize="large" onClick={this.startTreatClick}>{__('cases.details.starttreatment')}</Button>
				</div>
			</Card>
			);
	}
}
