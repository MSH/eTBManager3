import React from 'react';
import { Alert } from 'react-bootstrap';
import { Card, GridTable, Profile } from '../../../components';
import CardWithComments from './card-with-comments';


export default class Contacts extends React.Component {

	constructor(props) {
		super(props);

		this.state = {};
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

	render() {
		const tbcase = this.props.tbcase;

		return (
			<CardWithComments title={__('cases.contacts')}
				tbcase={tbcase} group="contacts">
			{
				tbcase.contacts ?
					this.contactsRender(tbcase.contacts) :
					<Alert bsStyle="warning">{'No record found'}</Alert>
			}
			</CardWithComments>
			);
	}
}

Contacts.propTypes = {
	tbcase: React.PropTypes.object
};
