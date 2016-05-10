import React from 'react';
import CommentsBox from './comments-box';

export default class CaseComments extends React.Component {

	constructor(props) {
		super(props);
		this.onCommentEvent = this.onCommentEvent.bind(this);
	}


	onCommentEvent(evt, data, txt) {
		switch (evt) {
			case 'add': return this.addComment(data);
			case 'remove': return this.removeComment(data);
			case 'edit': return this.editComment(data, txt);
			default: throw new Error('Invalid ' + evt);
		}
	}

	addComment(txt) {
		this.props.tbcase.comments.push({
			id: '11-22-' + this.props.tbcase.comments.length,
			user: {
				id: '12345-6',
				name: 'Steve Harris'
			},
			group: this.props.group,
			comment: txt
		});
		this.forceUpdate();
	}

	removeComment(item) {
		const lst = this.props.tbcase.comments;
		const index = lst.indexOf(item);
		this.props.tbcase.comments.splice(index, 1);
		this.forceUpdate();
	}

	editComment(item, text) {
		item.comment = text;
		this.forceUpdate();
	}


	render() {
		const group = this.props.group;
		const comments = this.props.tbcase.comments ?
			this.props.tbcase.comments.filter(item => item.group === group) :
			null;

		return (
			<div>
				{
					this.props.children
				}
				<CommentsBox values={comments} onEvent={this.onCommentEvent}/>
			</div>
			);
	}

}

CaseComments.propTypes = {
	tbcase: React.PropTypes.object,
	// name of the group in the list of comments
	group: React.PropTypes.string,
	children: React.PropTypes.node
};
