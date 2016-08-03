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

export default class CaseIssues extends React.Component {

	constructor(props) {
		super(props);
		this.addIssue = this.addIssue.bind(this);
		this.modalOpen = this.modalOpen.bind(this);
		this.modalClose = this.modalClose.bind(this);

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

				<Issues issues={issues} />

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
