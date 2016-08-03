import React from 'react';
import { Alert, Button } from 'react-bootstrap';
import { Card, Fa, FormDialog } from '../../../components';
import Issues from './issues';
import { app } from '../../../core/app';

// definition of the form fields to edit tags
const editorDef = {
	controls: [
		{
			property: 'title',
			required: true,
			type: 'string',
			max: 200,
			label: __('Issue.title'),
			size: { md: 12 }
		},
		{
			property: 'description',
			type: 'text',
			required: true,
			label: __('global.description'),
			size: { md: 12 }
		}
	],
	title: __('cases.issues.new')
};

const mockIssues = [
	{
		id: '12345-2',
		title: 'Thia case is no TB',
		description: 'patient didn\'t receive any attention from the primary health care service and now he is complaining about the treatment.\n' +
		'Please ask recommendation about what to do.\n\nRegards\nTreatment health care',
		user: {
			id: '12345-22',
			name: 'Nicko McBrain'
		},
		unit: {
			id: '12345-22',
			name: 'Treatment health care'
		},
		creationDate: new Date(2015, 2, 3, 12, 35, 55),
		lastAnswerDate: new Date(2015, 2, 3, 12, 35, 55),
		closed: false
	},
	{
		id: '12345-1',
		title: 'Missing parent name',
		description: 'is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into',
		user: {
			id: '12345-22',
			name: 'Dave Murray'
		},
		unit: {
			id: '12345-22',
			name: 'Hospital do pulmão'
		},
		closed: true,
		creationDate: new Date(2015, 1, 1, 5, 20, 5),
		lastAnswerDate: new Date(2015, 2, 3, 12, 35, 55),
		followups: [
			{
				id: '54333-1',
				text: 'is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into',
				user: {
					id: '12345-22',
					name: 'Dave Murray'
				},
				unit: {
					id: '12345-22',
					name: 'UNIT2asdasd'
				},
				followupDate: new Date(2015, 1, 1, 5, 20, 5)
			},
			{
				id: '54333-2',
				text: 'is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into',
				user: {
					id: '12345-22',
					name: 'Dave Murray'
				},
				unit: {
					id: '12345-22',
					name: 'UNIT2asdasd'
				},
				followupDate: new Date(2015, 1, 1, 5, 20, 5)
			},
			{
				id: '54333-3',
				text: 'is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into',
				user: {
					id: '12345-22',
					name: 'Dave Murray'
				},
				unit: {
					id: '12345-22',
					name: 'UNIT2asdasd'
				},
				followupDate: new Date(2015, 1, 1, 5, 20, 5)
			}
		]
	}
];


export default class CaseIssues extends React.Component {

	constructor(props) {
		super(props);
		this.addIssue = this.addIssue.bind(this);
		this.modalOpen = this.modalOpen.bind(this);
		this.modalClose = this.modalClose.bind(this);

		this.onIssueEvent = this.onIssueEvent.bind(this);
		this.removeIssue = this.removeIssue.bind(this);
		this.editIssue = this.editIssue.bind(this);
		this.closeIssue = this.closeIssue.bind(this);
		this.reopenIssue = this.reopenIssue.bind(this);

		this.state = { newIssue: {}, showModal: false };
	}

	addIssue() {
		const newIssue = this.state.newIssue;

		if (!this.props.tbcase.issues) {
			this.props.tbcase.issues = [];
		}

		// add the new comment on UI
		newIssue.id = 'fakeid-' + this.props.tbcase.issues.length;
		newIssue.user = {
							id: app.getState().session.userId,
							name: app.getState().session.userName
						};
		newIssue.unit = {
							id: app.getState().session.unitId,
							name: app.getState().session.unitName
						};
		newIssue.creationDate = new Date();
		newIssue.closed = false;

		this.props.tbcase.issues.push(newIssue);
		this.forceUpdate();

		const self = this;
		return Promise.resolve([]).then(() => self.modalClose());
	}

	onIssueEvent(evt, issue, doc) {
		switch (evt) {
			case 'remove': return this.removeIssue(issue);
			case 'edit': return this.editIssue(issue, doc);
			case 'close': return this.closeIssue(issue);
			case 'reopen': return this.reopenIssue(issue);
			default: throw new Error('Invalid ' + evt);
		}
	}

	removeIssue(issue) {
		// removes the comment from UI
		const lst = this.props.tbcase.issues;
		const index = lst.indexOf(issue);
		this.props.tbcase.issues.splice(index, 1);
		this.forceUpdate();
	}

	editIssue(issue, doc) {
		// refresh the comment on UI
		issue.title = doc.title;
		issue.description = doc.description;
		this.forceUpdate();
	}

	closeIssue(issue) {
		// refresh the comment on UI
		issue.closed = true;
		this.forceUpdate();
	}

	reopenIssue(issue) {
		// refresh the comment on UI
		issue.closed = false;
		this.forceUpdate();
	}

	modalOpen() {
		this.setState({ showModal: true });
	}

	modalClose() {
		this.setState({ newIssue: {}, showModal: false });
	}

	render() {
		const issues = mockIssues;

		// choose a message to dsplay at the top
		const openIssues = issues ? issues.find(issue => !issue.closed) : null;
		const header = openIssues ?
							<h4 className="inlineb"><Fa icon="exclamation-triangle" />{__('Issue.openissuesmg')}</h4> :
							<h4 className="inlineb"><Fa icon="check" />{__('Issue.noopenissuesmg')}</h4>;

		return (
			<div>
				<Card>
					<div>
						{header}
						<Button className="pull-right" onClick={this.modalOpen}>{__('cases.issues.new')}</Button>
						<div className="clearb"/>
					</div>
				</Card>

				{
					!issues && <Alert bsStyle="warning">{__('Issue.notfound')}</Alert>
				}

				<Issues issues={issues} onIssueEvent={this.onIssueEvent} />

				<FormDialog
					schema={editorDef}
					doc={this.state.newIssue}
					onCancel={this.modalClose}
					onConfirm={this.addIssue}
					wrapType={'modal'}
					modalShow={this.state.showModal} />

			</div>
			);
	}
}


CaseIssues.propTypes = {
	tbcase: React.PropTypes.object.isRequired
};
