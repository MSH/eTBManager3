import React from 'react';
import { Alert, Button, OverlayTrigger, Tooltip } from 'react-bootstrap';
import { Card, Profile, Fa } from '../../../components';

const mockIssues = [
	{
		id: '12345-1',
		text: 'is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into',
		user: {
			id: '12345-22',
			name: 'Dave Murray'
		},
		status: { id: 'CLOSED', name: 'Closed' },
		date: new Date(2015, 1, 1, 5, 20, 5),
		answers: [
			{
				id: '54333-1',
				text: 'is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into',
				user: {
					id: '12345-22',
					name: 'Dave Murray'
				},
				date: new Date(2015, 1, 1, 5, 20, 5)
			},
			{
				id: '54333-2',
				text: 'is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into',
				user: {
					id: '12345-22',
					name: 'Dave Murray'
				},
				date: new Date(2015, 1, 1, 5, 20, 5)
			},
			{
				id: '54333-3',
				text: 'is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into',
				user: {
					id: '12345-22',
					name: 'Dave Murray'
				},
				date: new Date(2015, 1, 1, 5, 20, 5)
			}
		]
	},
	{
		id: '12345-2',
		text: 'patient didn\'t receive any attention from the primary health care service and now he is complaining about the treatment.\n' +
		'Please ask recommendation about what to do.\n\nRegards\nTreatment health care',
		user: {
			id: '12345-22',
			name: 'Nicko McBrain'
		},
		date: new Date(2015, 2, 3, 12, 35, 55)
	}
];

export default class CaseIssues extends React.Component {


	editClick(issue) {
		return null;
	}

	removeClick(issue) {
		return null;
	}

	/**
	 * Render the issues to be displayed
	 * @param  {[type]} issues [description]
	 * @return {[type]}        [description]
	 */
	contentRender(issues) {
		if (!issues || issues.length === 0) {
			return <Alert bsStyle="warning">{'No issue found'}</Alert>;
		}

		const self = this;

		return (
			<div>
			{
				issues.map((issue, index) => (
					<div key={issue.id}>
					<div className="media">
						<div className="media-left">
							<Profile type="user" size="small"/>
						</div>
						<div className="media-body">
							<div className="pull-right">
								<a className="lnk-muted" onClick={this.editClick(issue)}><Fa icon="pencil"/>{__('action.edit')}</a>
								<OverlayTrigger placement="top" overlay={<Tooltip id="actdel">{__('action.delete')}</Tooltip>}>
									<a className="lnk-muted" onClick={this.removeClick(issue)}><Fa icon="remove"/></a>
								</OverlayTrigger>
							</div>
							<span className="status-box bg-primary">
								{'Open'}
							</span>
							<div className="text-muted"><b>{issue.user.name}</b>{' wrote in '}<b>{'dec 20th, 2015'}</b></div>
							{issue.text.split('\n').map((item, i) =>
								<span key={i}>
									{item}
									<br/>
								</span>
								)
							}
							{
								self.answersRender(issue.answers)
							}
						</div>
					</div>
					{
						(index !== issues.length - 1) && <hr/>
					}
					</div>
					))
			}
			</div>
			);
	}

	answersRender(answers) {
		if (!answers || answers.length === 0) {
			return null;
		}

		return answers.map(it => (
			<div className="media" key={it.id}>
				<div className="media-left">
					<Profile type="user" size="small"/>
				</div>
				<div className="media-body">
					<div className="pull-right">
						<a className="lnk-muted" onClick={this.editClick(it)}><Fa icon="pencil"/>{__('action.edit')}</a>
						<OverlayTrigger placement="top" overlay={<Tooltip id="actdel">{__('action.delete')}</Tooltip>}>
							<a className="lnk-muted" onClick={this.removeClick(it)}><Fa icon="remove"/></a>
						</OverlayTrigger>
					</div>
					<div className="text-muted"><b>{it.user.name}</b>{' wrote in '}<b>{'dec 20th, 2015'}</b></div>
					{it.text.split('\n').map((item, index) =>
						<span key={index}>
							{item}
							<br/>
						</span>
						)
					}
				</div>
			</div>
			));
	}

	render() {
		const issues = mockIssues;

		const header = (
			<div>
				<Button className="pull-right">{'Add issue'}</Button>
				<h4>{'Issues'}</h4>
			</div>
			);

		return (
			<Card header={header}>
			{
				this.contentRender(issues)
			}
			</Card>
			);
	}
}


CaseIssues.propTypes = {
	tbcase: React.PropTypes.object.isRequired
};
