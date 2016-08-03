import React from 'react';
import { Alert, Button } from 'react-bootstrap';
import { Card, FormDialog, Fa } from '../../../components';
import Issue from './issue';

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

export default class CaseIssues extends React.Component {

	constructor(props) {
		super(props);
		this.openNewIssueForm = this.openNewIssueForm.bind(this);
		this.save = this.save.bind(this);
		this.closeNewIssueForm = this.closeNewIssueForm.bind(this);

		this.state = { doc: {}, showForm: false };
	}

	openNewIssueForm() {
		this.setState({ doc: {}, showForm: true });
	}

	save() {
		this.closeForm();
	}

	closeNewIssueForm() {
		this.setState({ doc: {}, showForm: false });
	}

	/**
	 * Render the issues to be displayed
	 * @param  {[type]} issues [description]
	 * @return {[type]}        [description]
	 */
	contentRender(issues) {
		if (!issues || issues.length === 0) {
			return <Alert bsStyle="warning">{__('Issue.notfound')}</Alert>;
		}

		return (
			<div>
			{
				issues.map((issue) => (<Issue key={issue.id} issue={issue}/>))
			}
			</div>
			);
	}

	render() {
		const issues = mockIssues;

		const openIssues = issues.find(issue => !issue.closed);
		const message = openIssues ?
							<h4 className="inlineb"><Fa icon="exclamation-triangle" />{__('Issue.openissuesmg')}</h4> :
							<h4 className="inlineb"><Fa icon="check" />{__('Issue.noopenissuesmg')}</h4>;
		return (
			<div>
				<Card>
					<div>
						{message}
						<Button className="pull-right" onClick={this.openNewIssueForm}>{__('cases.issues.new')}</Button>
						<div className="clearb"/>
					</div>
				</Card>

				{
					this.contentRender(issues)
				}

				<FormDialog
					schema={issueEditorDef}
					doc={this.state.doc}
					onCancel={this.closeNewIssueForm}
					onConfirm={this.save}
					wrapType={'modal'}
					modalShow={this.state.showForm} />

			</div>
			);
	}
}


CaseIssues.propTypes = {
	tbcase: React.PropTypes.object.isRequired
};
