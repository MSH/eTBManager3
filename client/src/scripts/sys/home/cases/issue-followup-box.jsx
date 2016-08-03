import React from 'react';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import { Profile, Fa, FormDialog } from '../../../components';
import moment from 'moment';
import { app } from '../../../core/app';

// definition of the form fields to edit tags
const issueEditorDef = {
	controls: [
		{
			property: 'text',
			type: 'text',
			required: true,
			label: __('global.comments'),
			size: { md: 12 }
		}
	],
	title: __('cases.issues.new')
};

export default class IssueFollowUpBox extends React.Component {

	constructor(props) {
		super(props);
		this.editClick = this.editClick.bind(this);
		this.saveFollowUp = this.saveFollowUp.bind(this);
		this.closeForm = this.closeForm.bind(this);
		this.removeFollowUp = this.removeFollowUp.bind(this);
		this.showRemConfirmMessage = this.showRemConfirmMessage.bind(this);

		this.state = { doc: {}, showForm: false };
	}

	editClick() {
		this.setState({ doc: this.props.followup, showForm: true });
	}

	saveFollowUp() {
		console.log('edit issue FU');
		this.closeForm();
		return null;
	}

	closeForm() {
		this.setState({ doc: {}, showForm: false });
	}

	removeFollowUp() {
		console.log('remove issue FU');
	}

	showRemConfirmMessage() {
		app.messageDlg({
					title: 'aSDKLÇJASD',
					message: 'SALÇKDASÇL',
					style: 'warning',
					type: 'YesNo'
				}).then(res =>
					{
						if (res === 'yes') {
							this.removeFollowUp();
						}
					});
	}

	render() {
		const followup = this.props.followup;

		return (<div>
					<div className="media-left">
						<Profile type="user" size="small"/>
					</div>
					<div className="media-body">
						<div className="pull-right">
							<a className="lnk-muted" onClick={this.editClick}><Fa icon="pencil"/>{__('action.edit')}</a>
							<OverlayTrigger placement="top" overlay={<Tooltip id="actdel">{__('action.delete')}</Tooltip>}>
								<a className="lnk-muted" onClick={this.showRemConfirmMessage}><Fa icon="remove"/></a>
							</OverlayTrigger>
						</div>
						<div className="text-muted"><b>{followup.user.name}</b>{' ' + __('global.wrotein') + ' '}<b>{moment(followup.followupDate).format('lll')}</b></div>
						<div className="sub-text">{followup.unit.name}</div>
						{followup.text.split('\n').map((item, index) =>
							<span key={index}>
								{item}
								<br/>
							</span>
							)
						}
					</div>

					<FormDialog
						schema={issueEditorDef}
						doc={this.state.doc}
						onCancel={this.closeForm}
						onConfirm={this.saveFollowUp}
						wrapType={'modal'}
						modalShow={this.state.showForm} />

				</div>
				);
	}
}


IssueFollowUpBox.propTypes = {
	followup: React.PropTypes.object.isRequired
};
