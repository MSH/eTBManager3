import React from 'react';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import { Profile, Fa, FormDialog, Card } from '../../../components';
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

		this.state = { doc: {}, showForm: false };
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

	showConfirmMessage(title, message, doIfConfirm) {
		return () => {
				app.messageDlg({
							title: title,
							message: message,
							style: 'warning',
							type: 'YesNo'
						}).then(res =>
							{
								if (res === 'yes') {
									doIfConfirm();
								}
							});
			};
	}

	followUpsRender(followups) {
		if (!followups || followups.length === 0) {
			return null;
		}

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
										<a className="lnk-muted" onClick={this.showConfirmMessage('sakdljaksl', 'asçlkd', this.reopenIssue)}><Fa icon="folder-open-o"/>{__('action.reopen')}</a> :
										<a className="lnk-muted" onClick={this.showConfirmMessage('sakdljaksl', 'asçlkd', this.closeIssue)}><Fa icon="power-off"/>{__('action.close')}</a>
								}

								<a className="lnk-muted" onClick={this.editClick}><Fa icon="pencil"/>{__('action.edit')}</a>
								<OverlayTrigger placement="top" overlay={<Tooltip id="actdel">{__('action.delete')}</Tooltip>}>
									<a className="lnk-muted" onClick={this.showConfirmMessage('sakdljaksl', 'asçlkd', this.removeIssue)}><Fa icon="trash-o"/></a>
								</OverlayTrigger>
							</div>

							{this.props.issue.closed ? <span className="status-box bg-primary mright">{__('Issue.closed')}</span> : <span className="status-box bg-danger2 mright">{__('Issue.open')}</span>}

							<div className="inlineb"><b>{this.props.issue.title}</b></div>
							<div className="text-muted"><b>{this.props.issue.user.name}</b>{' ' + __('global.wrotein') + ' '}<b>{moment(this.props.issue.creationDate).format('lll')}</b></div>
							<div className="sub-text">{this.props.issue.unit.name}</div>

							<br/>

							{this.props.issue.description.split('\n').map((item, i) =>
								<span key={i}>
									{item}
									<br/>
								</span>
								)
							}
							{
								this.followUpsRender(this.props.issue.followups)
							}
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
