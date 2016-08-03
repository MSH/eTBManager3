import React from 'react';
import { Alert, Button } from 'react-bootstrap';
import { Card, Fa, FormDialog } from '../../../components';
import IssueCard from './issue-card';
import { app } from '../../../core/app';

// definition of the form fields to edit tags
const issueEditorDef = {
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

export default class Issues extends React.Component {

	constructor(props) {
		super(props);
		this.onIssueEvent = this.onIssueEvent.bind(this);
		this.removeIssue = this.removeIssue.bind(this);
		this.editIssue = this.editIssue.bind(this);
		this.closeIssue = this.closeIssue.bind(this);
		this.reopenIssue = this.reopenIssue.bind(this);

		this.state = { issue: null, doc: {} };
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
		const lst = this.props.issues;
		const index = lst.indexOf(issue);
		this.props.issues.splice(index, 1);
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

	render() {
		const issues = this.props.issues;

		// shown if no issues is registered
		if (!issues || issues.length === 0) {
			return null;
		}

		return (
			<div>
				{
					issues.map((issue) => (<IssueCard key={issue.id} issue={issue} onIssueEvent={this.onIssueEvent}/>))
				}

				<FormDialog
					schema={issueEditorDef}
					doc={this.state.doc}
					onCancel={this.modalClose}
					onConfirm={this.edtConfirm}
					wrapType={'modal'}
					modalShow={!!this.state.edtItem} />
			</div>
			);
	}
}


Issues.propTypes = {
	issues: React.PropTypes.array
};
