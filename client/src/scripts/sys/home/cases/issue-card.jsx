import React from 'react';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import { Profile, Fa, Card } from '../../../components';
import moment from 'moment';
import { app } from '../../../core/app';
import IssueFollowUpsBox from './issue-followups-box';

export default class IssueCard extends React.Component {

	constructor(props) {
		super(props);
		this.addAnswer = this.addAnswer.bind(this);
		this.removeAnswer = this.removeAnswer.bind(this);
		this.editAnswer = this.editAnswer.bind(this);
		this.onFollowupEvent = this.onFollowupEvent.bind(this);


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
			issueId: this.props.issue.id,
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

	editIssueClick() {

	}

	confirmRemove() {
		return () => app.messageDlg({
							message: __('form.confirm_remove'),
							style: 'warning',
							type: 'YesNo'
						}).then(res =>
							{
								if (res === 'yes') {
									this.props.onIssueEvent('remove', this.props.issue);
								}
							});
	}

	closeIssueClick() {
		this.props.onIssueEvent('close', this.props.issue);
	}

	reopenIssueClick() {
		this.props.onIssueEvent('reopen', this.props.issue);
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
										<a className="lnk-muted" onClick={this.reopenIssueClick}><Fa icon="folder-open-o"/>{__('action.reopen')}</a> :
										<a className="lnk-muted" onClick={this.closeIssueClick}><Fa icon="power-off"/>{__('action.close')}</a>
								}

								<a className="lnk-muted" onClick={this.editIssueClick}><Fa icon="pencil"/>{__('action.edit')}</a>
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

							<IssueFollowUpsBox issue={this.props.issue} onEvent={this.onFollowupEvent} />

						</div>
					</div>
				</Card>
				);
	}
}


IssueCard.propTypes = {
	issue: React.PropTypes.object.isRequired,
	onEditEvent: React.PropTypes.func,
	onRemoveEvent: React.PropTypes.func,
	/**
	 * Possible events: edit, remove, reopen, close
	 */
	onIssueEvent: React.PropTypes.func
};
