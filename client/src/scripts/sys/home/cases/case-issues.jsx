import React from 'react';
import { Alert, Button } from 'react-bootstrap';
import { Card, Fa } from '../../../components';
import Issue from './issue';
import { app } from '../../../core/app';

export default class CaseIssues extends React.Component {

	constructor(props) {
		super(props);
		this.onIssueEvent = this.onIssueEvent.bind(this);
		this.addIssue = this.addIssue.bind(this);
		this.removeIssue = this.removeIssue.bind(this);
		this.editIssue = this.editIssue.bind(this);

		this.state = { doc: {}, showForm: false };
	}


	onIssueEvent(evt, issue, doc) {
		switch (evt) {
			case 'add': return this.addIssue(doc);
			case 'remove': return this.removeIssue(issue);
			case 'edit': return this.editIssue(issue, doc);
			default: throw new Error('Invalid ' + evt);
		}
	}

	addIssue(doc) {
		if (!this.props.tbcase.issues) {
			this.props.tbcase.issues = [];
		}

		// add the new comment on UI
		const newIssue = {
			id: 'fakeid-' + this.props.tbcase.issues.length,
			title: doc.title,
			user: {
				id: app.getState().session.userId,
				name: app.getState().session.userName
			},
			unit: {
				id: app.getState().session.unitId,
				name: app.getState().session.unitName
			},
			creationDate: new Date(),
			closed: false
		};

		this.props.tbcase.issues.push(newIssue);
		this.forceUpdate();
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

	render() {
		const issues = this.props.tbcase.issues;

		// choose a message to dsplay at the top
		const openIssues = issues ? issues.find(issue => !issue.closed) : null;
		const header = openIssues ?
							<h4 className="inlineb"><Fa icon="exclamation-triangle" />{__('Issue.openissuesmg')}</h4> :
							<h4 className="inlineb"><Fa icon="check" />{__('Issue.noopenissuesmg')}</h4>;

		// shown if no issues is registered
		if (!issues || issues.length === 0) {
			return (<div>
						<Card>
							<div>
								{header}
								<Button className="pull-right" onClick={this.openNewIssueForm}>{__('cases.issues.new')}</Button>
								<div className="clearb"/>
							</div>
						</Card>

						<Alert bsStyle="warning">{__('Issue.notfound')}</Alert>
					</div>);
		}

		return (
			<div>
				<Card>
					<div>
						{header}
						<Button className="pull-right" onClick={this.openNewIssueForm}>{__('cases.issues.new')}</Button>
						<div className="clearb"/>
					</div>
				</Card>

				{
					issues.map((issue) => (<Issue key={issue.id} issue={issue} onIssueEvent={this.onIssueEvent}/>))
				}

			</div>
			);
	}
}


CaseIssues.propTypes = {
	tbcase: React.PropTypes.object.isRequired
};
