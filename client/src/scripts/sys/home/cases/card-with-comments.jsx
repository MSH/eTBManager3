import React from 'react';
import { Card } from '../../../components';
import CommentsBox from './comments-box';


export default class CardWithComments extends React.Component {

	constructor(props) {
		super(props);
		this.onAddComment = this.onAddComment.bind(this);
	}


	onAddComment(txt) {
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


	render() {
		const group = this.props.group;
		const comments = this.props.tbcase.comments ?
			this.props.tbcase.comments.filter(item => item.group === group) :
			null;

		return (
			<div>
				<Card title={this.props.title} padding="combine">
					{
						this.props.children
					}
				</Card>
				<CommentsBox values={comments} onAdd={this.onAddComment}/>
			</div>
			);
	}
}

CardWithComments.propTypes = {
	title: React.PropTypes.node,
	tbcase: React.PropTypes.object,
	// name of the group in the list of comments
	group: React.PropTypes.string,
	children: React.PropTypes.node
};
