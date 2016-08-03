import React from 'react';
import { Button, ButtonToolbar, OverlayTrigger, Tooltip, Modal } from 'react-bootstrap';
import { Card, Profile, Fa, AutoheightInput, MessageDlg } from '../../../components';
import moment from 'moment';

import './comments-box.less';


export default class CommentsBox extends React.Component {

	constructor(props) {
		super(props);
		this.addComment = this.addComment.bind(this);
		this.showCommentsBox = this.showCommentsBox.bind(this);
		this.editClick = this.editClick.bind(this);
		this.modalClose = this.modalClose.bind(this);
		this.editConfirm = this.editConfirm.bind(this);
		this.closeRemoveConfDlg = this.closeRemoveConfDlg.bind(this);
		this.textChange = this.textChange.bind(this);
		this.editTextChange = this.editTextChange.bind(this);

		this.state = { disabled: true };
	}

	/**
	 * Called when user adds a new comment
	 */
	addComment() {
		const txt = this.refs.input.getText();
		this.props.onEvent('add', txt);
		this.refs.input.setText('');
	}

	/**
	 * Render of the initial state of the comments box, when there is no comments to display.
	 * In this case, just a link to add comments is displayed
	 * @return {[type]} [description]
	 */
	emptyRender() {
		return (
			<Card padding="small" className="def-margin-bottom">
				<a className="comments-link" onClick={this.showCommentsBox}><Fa icon="comment"/>{'Add comment'}</a>
			</Card>
			);
	}

	/**
	 * Show the comments allowing user to include a new comment. Called when user clicks
	 * on the link when there is no comment to display
	 * @return {[type]} [description]
	 */
	showCommentsBox() {
		this.setState({ showComments: true, disabled: true });
		const self = this;
		setTimeout(() => {
			if (self.refs.input) {
				self.refs.input.focus();
			}
		}, 200);
	}

	/**
	 * Return a function to be called when user clicks on the edit link
	 * @param  {[type]} item [description]
	 * @return {[type]}      [description]
	 */
	editClick(item) {
		const self = this;
		return () => {
			self.setState({ edtitem: item });
			setTimeout(() => this.refs.edtinput.focus(), 100);
		};
	}

	/**
	 * Return a function to be called when user clicks on the remove link
	 * @param  {[type]} item [description]
	 * @return {[type]}      [description]
	 */
	removeClick(item) {
		const self = this;
		return () => self.setState({ showRemoveConf: true, item: item, showComments: false });
	}

	/**
	 * Called when user confirms (or reject) the deleting of a comment
	 * @param  {string} evt The option selected by the user - yes or no
	 */
	closeRemoveConfDlg(evt) {
		if (evt === 'yes') {
			this.props.onEvent('remove', this.state.item);
		}
		this.setState({ showRemoveConf: null, item: null });
	}

	/**
	 * Called when user clicks on the close link
	 * @return {[type]} [description]
	 */
	modalClose() {
		this.setState({ edtitem: null });
	}

	/**
	 * Called when user confirms the changed text in the dialog box
	 * @return {[type]} [description]
	 */
	editConfirm() {
		const item = this.state.edtitem;
		this.props.onEvent('edit', item, this.refs.edtinput.getText());
		this.modalClose();
	}

	/**
	 * Called when the text of the input box is changed. Enable or disable the add button
	 */
	textChange() {
		this.setState({ disabled: !this.refs.input.getText().trim() });
	}

	/**
	 * Called when the text of the input in edit dialog is changed. Enable or disable the save button
	 */
	editTextChange() {
		this.setState({ edtdisabled: !this.refs.edtinput.getText().trim() });
	}

	render() {
		const comments = this.props.values;
		const hasComments = comments && comments.length > 0;

		// if there is no comment to display, initially just the 'add comments' link
		// is displayed
		if (!hasComments && !this.state.showComments) {
			return this.emptyRender();
		}

		return (
			<Card className="comment-box">
				<div>
					{
						comments && comments.map(it =>
							<div key={it.id} className="media">
								<div className="media-left">
									<Profile size="small" type="user"/>
								</div>
								<div className="media-body">
									<div className="pull-right">
									{it.id.indexOf('fakeid') < 0 && it.id.indexOf('error') < 0 &&
										<span>
											<a className="lnk-muted" onClick={this.editClick(it)}><Fa icon="pencil"/>{__('action.edit')}</a>
											<OverlayTrigger placement="top" overlay={<Tooltip id="actdel">{__('action.delete')}</Tooltip>}>
												<a className="lnk-muted" onClick={this.removeClick(it)}><Fa icon="remove"/></a>
											</OverlayTrigger>
										</span>
									}
									{it.id.indexOf('fakeid') >= 0 &&
										<span className="lnk-muted">
											{'Saving...'}
										</span>
									}
									{it.id.indexOf('error') >= 0 &&
										<span className="bs-error">
											{'Error - Comment not saved'}
										</span>
									}
									</div>
									<div className="text-muted"><b>{it.user.name}</b>{__('global.wrotein')}<b>{moment(it.date).format('lll')}</b></div>
									{it.comment.split('\n').map((item, index) =>
										<span key={index}>
											{item}
											<br/>
										</span>
										)
									}
								</div>
							</div>
							)
					}
					<div className="media">
						<div className="media-left">
							<Profile size="small" type="user" />
						</div>
						<div className="media-body">
							<div className="form-group no-margin-bottom">
								<AutoheightInput ref="input"
									onChange={this.textChange} />
								<Button bsStyle="primary"
									disabled={this.state.disabled}
									onClick={this.addComment}
									bsSize="small"
									style={{ marginTop: '6px' }}>
									{__('action.add')}
								</Button>
							</div>
						</div>
					</div>
					<Modal show={!!this.state.edtitem} onHide={this.modalClose}>
						<Modal.Header closeButton>
							<Modal.Title>{'Edit comment'}</Modal.Title>
						</Modal.Header>
						<Modal.Body>
						{
							this.state.edtitem &&

							<div className="form-group">
								<AutoheightInput defaultValue={this.state.edtitem.comment}
									onChange={this.editTextChange}
									ref="edtinput" />
							</div>
						}
						<ButtonToolbar>
							<Button disabled={this.state.edtdisabled}
								bsStyle="primary"
								onClick={this.editConfirm}>{__('action.save')}</Button>
							<Button bsStyle="link" onClick={this.modalClose}>{__('action.cancel')}</Button>
						</ButtonToolbar>
						</Modal.Body>
					</Modal>
					<MessageDlg show={this.state.showRemoveConf}
						title={__('action.delete')}
						message={__('form.confirm_remove')}
						type="YesNo"
						onClose={this.closeRemoveConfDlg} />
				</div>
			</Card>
			);
	}
}

CommentsBox.propTypes = {
	values: React.PropTypes.array,
	/**
	 * Possible events: add, edit, remove
	 */
	onEvent: React.PropTypes.func
};
