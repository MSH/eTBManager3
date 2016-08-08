import React from 'react';
import { Alert, Button, Row, Col } from 'react-bootstrap';
import { Card, Fa, FormDialog } from '../../../components';
import Issues from './issues';
import { app } from '../../../core/app';
import CRUD from '../../../commons/crud';

const crud = new CRUD('issue');

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
		newIssue.tbcaseId = this.props.tbcase.id;
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

		// create the new issue on database
		const prom = crud.create(newIssue)
						.then(id => {
							// updates new comment id, so edit and delete should work.
							newIssue.id = id;
							this.forceUpdate();

						})
						.catch(() => {
							newIssue.id = 'error-' + this.props.tbcase.issues.length;
							this.forceUpdate();
							this.modalClose();
						});

		this.modalClose();

		return prom;
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

		// delete the new comment on database
		crud.delete(issue.id);
	}

	editIssue(issue, doc) {
		// refresh the comment on UI
		issue.title = doc.title;
		issue.description = doc.description;
		this.forceUpdate();

		crud.update(issue.id, doc);
	}

	closeIssue(issue) {
		// refresh the comment on UI
		issue.closed = true;
		this.forceUpdate();

		crud.update(issue.id, issue);
	}

	reopenIssue(issue) {
		// refresh the comment on UI
		issue.closed = false;
		this.forceUpdate();

		crud.update(issue.id, issue);
	}

	modalOpen() {
		this.setState({ showModal: true });
	}

	modalClose() {
		this.setState({ newIssue: {}, showModal: false });
	}

	render() {
		const issues = this.props.tbcase.issues;

		// choose a message to dsplay at the top
		const openIssues = issues ? issues.find(issue => !issue.closed) : null;
		const header = openIssues ?
							<Alert bsStyle="warning" className="no-margin-bottom"><Fa icon="exclamation-triangle" />{__('Issue.openissuesmg')}</Alert> :
							<div><Fa icon="check" />{__('Issue.noopenissuesmg')}</div>;

		return (
			<div>
				<Card>
					<Row>
						<Col md={10}>
							{header}
						</Col>
						<Col md={2}>
							<Button className="pull-right" onClick={this.modalOpen}>{__('cases.issues.new')}</Button>
						</Col>
					</Row>
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
