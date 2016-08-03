import React from 'react';
import { OverlayTrigger, Tooltip, Button } from 'react-bootstrap';
import { Profile, Fa, FormDialog, Card, AutoheightInput } from '../../../components';
import moment from 'moment';
import { app } from '../../../core/app';
import IssueFollowUpBox from './issue-followup-box';

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

export default class Issue extends React.Component {

	constructor(props) {
		super(props);
		this.editClick = this.editClick.bind(this);
		this.saveEdit = this.saveEdit.bind(this);
		this.closeForm = this.closeForm.bind(this);
		this.closeIssue = this.closeIssue.bind(this);
		this.reopenIssue = this.reopenIssue.bind(this);
		this.removeIssue = this.removeIssue.bind(this);
		this.answerChange = this.answerChange.bind(this);
		this.onFollowupEvent = this.onFollowupEvent.bind(this);

		this.addAnswer = this.addAnswer.bind(this);
		this.removeAnswer = this.removeAnswer.bind(this);
		this.editAnswer = this.editAnswer.bind(this);


		this.state = { doc: {}, showForm: false, answerBtnDisabled: true };
	}

	onFollowupEvent(evt, data, txt) {
		switch (evt) {
			case 'add': return this.addAnswer(data);
			case 'remove': return this.removeAnswer(data);
			case 'edit': return this.editAnswer(data, txt);
			default: throw new Error('Invalid ' + evt);
		}
	}

	addAnswer(txt) {
		if (!this.props.issue.followups) {
			this.props.issue.followups = [];
		}

		// add the new comment on UI
		const newAnswer = {
			id: 'fakeid-' + this.props.issue.followups.length,
			text: txt,
			user: {
				id: app.getState().session.userId,
				name: app.getState().session.userName
			},
			unit: {
				id: app.getState().session.unitId,
				name: app.getState().session.unitName
			},
			followupDate: new Date()
		};

		this.props.issue.followups.push(newAnswer);
		this.forceUpdate();
	}

	removeAnswer(item) {
		// removes the comment from UI
		const lst = this.props.issue.followups;
		const index = lst.indexOf(item);
		this.props.issue.followups.splice(index, 1);
		this.forceUpdate();
	}

	editAnswer(item, txt) {
		// refresh the comment on UI
		item.text = txt;
		this.forceUpdate();
	}





	editClick() {
		this.setState({ doc: this.props.issue, showForm: true });
	}

	saveEdit() {
		console.log('edit issue');
		this.closeForm();
		return null;
	}

	closeForm() {
		this.setState({ doc: {}, showForm: false });
	}

	closeIssue() {
		console.log('close issue');
	}

	reopenIssue() {
		console.log('reopen issue');
	}

	removeIssue() {
		console.log('remove issue');
	}

	answerChange() {
		this.setState({ answerBtnDisabled: !this.refs.input.getText().trim() });
	}

	confirmRemove() {
		app.messageDlg({
					message: __('form.confirm_remove'),
					style: 'warning',
					type: 'YesNo'
				}).then(res =>
					{
						if (res === 'yes') {
							this.removeIssue();
						}
					});
	}

	followUpsRender(followups) {


		return followups.map(it => (
			<div className="media" key={it.id}>
				<IssueFollowUpBox followup={it} />
			</div>
			));
	}

	render() {
		return (<Card>

					<div className="media">
						<div className="media-left">
							<Profile type="user" size="small"/>
						</div>
						<div className="media-body">
							<div className="pull-right">
								{
									this.props.issue.closed ?
										<a className="lnk-muted" onClick={this.reopenIssue}><Fa icon="folder-open-o"/>{__('action.reopen')}</a> :
										<a className="lnk-muted" onClick={this.closeIssue}><Fa icon="power-off"/>{__('action.close')}</a>
								}

								<a className="lnk-muted" onClick={this.editClick}><Fa icon="pencil"/>{__('action.edit')}</a>
								<OverlayTrigger placement="top" overlay={<Tooltip id="actdel">{__('action.delete')}</Tooltip>}>
									<a className="lnk-muted" onClick={this.confirmRemove}><Fa icon="trash-o"/></a>
								</OverlayTrigger>
							</div>

							{this.props.issue.closed ? <span className="status-box bg-default2 mright">{__('Issue.closed')}</span> : <span className="status-box bg-danger2 mright">{__('Issue.open')}</span>}

							<div className="inlineb"><b>{this.props.issue.title}</b></div>
							<div className="text-muted"><b>{this.props.issue.user.name}</b>{' ' + __('global.wrotein') + ' '}<b>{moment(this.props.issue.creationDate).format('lll')}</b></div>
							<div className="sub-text mbottom">{this.props.issue.unit.name}</div>

							{this.props.issue.description.split('\n').map((item, i) =>
								<span key={i}>
									{item}
									<br/>
								</span>
								)
							}

							<IssueFollowUpBox issue={this.props.issue} onEvent={this.onFollowupEvent} />

						</div>
					</div>

					<FormDialog
						schema={issueEditorDef}
						doc={this.state.doc}
						onCancel={this.closeForm}
						onConfirm={this.saveEdit}
						wrapType={'modal'}
						modalShow={this.state.showForm} />

				</Card>
				);
	}
}


Issue.propTypes = {
	issue: React.PropTypes.object.isRequired
};
