import React from 'react';
import { Alert } from 'react-bootstrap';
import { Card, GridTable, Profile } from '../../../components';
import CommentsBox from './comments-box';

import { generateName } from '../../mock-data';

const comments = [
	{
		id: '123456-12',
		user: {
			id: '12312312',
			name: 'Bruce Dickinson'
		},
		date: new Date(),
		comment: 'is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum'
	},
	{
		id: '123456-11',
		user: {
			id: '12312312',
			name: 'Iron Maiden'
		},
		date: new Date(),
		comment: 'Contact Rubens Smith refused interview and moved out to another address'
	}
];


export default class Contacts extends React.Component {

	constructor(props) {
		super(props);

		this.onAddComment = this.onAddComment.bind(this);
		this.state = {};
	}

	componentWillMount() {
		const lst = [];
		for (var i = 0; i < 5; i++) {
			const res = generateName();
			lst.push({
				name: res.name,
				gender: res.gender,
				age: res.age
			});
		}
		this.setState({ contacts: lst });
	}

	cellRender(item) {
		return (
			<Card padding="small">
				<div>
					<Profile size="small" type={item.gender.toLowerCase()}
						title={item.name} subtitle={item.age} />
				</div>
			</Card>);
	}

	contactsRender(contacts) {
		return <GridTable values={contacts} onCellRender={this.cellRender} />;
	}

	onAddComment(txt) {
		const res = generateName();
		comments.push({
			id: '111-222-' + comments.length,
			user: {
				name: res.name,
				id: '1231231'
			},
			comment: txt
		});

		this.forceUpdate();
	}

	render() {
		return (
			<div>
				<Card title={__('cases.contacts')} padding="combine">
					{
						this.state.contacts ?
							this.contactsRender(this.state.contacts) :
							<Alert bsStyle="warning">{'No record found'}</Alert>
					}
				</Card>
				<CommentsBox values={comments} onAdd={this.onAddComment}/>
			</div>
			);
	}
}

Contacts.propTypes = {
	case: React.PropTypes.object
};
