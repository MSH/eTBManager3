import React from 'react';
import { Card } from '../../components/index';
import { Button, ButtonToolbar } from 'react-bootstrap';
import { app } from '../../core/app';

export default class ShowMessage extends React.Component {

	openInfo() {
		app.messageDlg({
			title: 'Test',
			message: 'This is a simple message displayed for testing',
			style: 'info',
			type: 'Ok'
		})
		.then(res => console.log('Called when closed: ', res));
	}

	openWarning() {
		app.messageDlg({
			title: 'Test',
			message: 'This is a simple message displayed for testing',
			style: 'warning',
			type: 'YesNo'
		})
		.then(res => console.log('Called when closed: ', res));
	}

	openError() {
		app.messageDlg({
			title: 'Error title',
			message: 'This is a simple message displayed for testing',
			style: 'danger',
			type: 'Ok'
		})
		.then(res => console.log('Called when closed: ', res));
	}

	openSequence() {
		app.messageDlg({
			title: 'Dialog 1',
			message: 'This is the first dialog',
			style: 'info',
			type: 'Ok'
		})
		.then(() => {
			app.messageDlg({
				title: 'Dialog 2',
				message: 'This is the second dialog',
				style: 'warning',
				type: 'Ok'
			});
		});
	}

	render() {
		return (
			<Card title="Message examples">
				<ButtonToolbar>
					<Button bsStyle="success" bsSize="large" onClick={this.openInfo}>{'Info'}</Button>
					<Button bsStyle="warning" bsSize="large" onClick={this.openWarning}>{'Warning'}</Button>
					<Button bsStyle="danger" bsSize="large" onClick={this.openError}>{'Danger'}</Button>
					<Button bsStyle="primary" bsSize="large" onClick={this.openSequence}>{'Sequence'}</Button>
				</ButtonToolbar>
				<p className="mtop-2x text-muted">
					{'When dialog closes, a message is displayed in the console'}
				</p>
			</Card>
			);
	}
}
