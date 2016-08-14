import React from 'react';
import { Button } from 'react-bootstrap';
import { Card, Fa } from '../../../../components';
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
				<h4>
					<Fa icon="exclamation-triangle" className="text-warning"/>
					{__('cases.details.notreatment')}
				</h4>
				<Button bsSize="large" onClick={this.startTreatClick}>{__('cases.details.starttreatment')}</Button>
			</Card>
			);
	}
}
