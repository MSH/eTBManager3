import React from 'react';
import { Button } from 'react-bootstrap';
import { Card, Profile, Fa } from '../../../components';

import './comments-box.less';


export default class CommentsCard extends React.Component {

	constructor(props) {
		super(props);
		this.inputHeight = this.inputHeight.bind(this);
		this.addComment = this.addComment.bind(this);
		this.showCommentsBox = this.showCommentsBox.bind(this);
		this.state = { };
	}

	componentDidMount() {
		this.inputHeight();
	}

	/**
	 * Update the input height based on the content
	 * @return {[type]} [description]
	 */
	inputHeight() {
		if (!this.refs.input) {
			return;
		}
		const el = this.refs.input;
		el.style.height = 'auto';
		el.style.height = el.scrollHeight + 'px';
	}

	/**
	 * Called when user adds a new comment
	 */
	addComment() {
		const txt = this.refs.input.value;
		this.props.onAdd(txt);
		this.refs.input.value = '';
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
		this.setState({ showComments: true });
		const self = this;
		setTimeout(() => {
			if (self.refs.input) {
				self.refs.input.focus();
			}
		}, 200);
	}

	render() {
		const comments = this.props.values;

		// if there is no comment to display, initially just the 'add comments' link
		// is displayed
		if (!comments && !this.state.showComments) {
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
								<div className="text-muted"><b>{it.user.name}</b>{' wrote in '}<b>{'dec 20th, 2015'}</b></div>
								{it.comment}
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
							<textarea ref="input" rows="1"
								className="form-control"
								style={{ height: 'auto', overflowY: 'hidden' }}
								onInput={this.inputHeight}/>
							<Button bsStyle="primary"
								onClick={this.addComment}
								bsSize="small"
								style={{ marginTop: '6px' }}>
								{__('action.add')}
							</Button>
						</div>
					</div>
				</div>
				</div>
			</Card>
			);
	}
}

CommentsCard.propTypes = {
	values: React.PropTypes.array,
	onAdd: React.PropTypes.func
};
