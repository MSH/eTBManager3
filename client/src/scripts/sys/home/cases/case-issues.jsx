import React from 'react';
import { Alert, Button } from 'react-bootstrap';
import { Card, FormDialog } from '../../../components';
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
		this.openNewIssueForm = this.openNewIssueForm.bind(this);
		this.save = this.save.bind(this);
		this.closeForm = this.closeForm.bind(this);

		this.state = { doc: {}, showForm: false };
	}

	openNewIssueForm() {
		this.setState({ doc: {}, showForm: true });
	}

	save() {
		this.closeForm();
	}

	closeForm() {
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

		const header = (
			<div>
				<Button className="pull-right" onClick={this.openNewIssueForm}>{__('cases.issues.new')}</Button>
				<h4>{__('cases.issues')}</h4>
			</div>
			);

		return (
			<div>
				<Card header={header} />

				{
					this.contentRender(issues)
				}

				<FormDialog
					schema={issueEditorDef}
					doc={this.state.doc}
					onCancel={this.closeForm}
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
