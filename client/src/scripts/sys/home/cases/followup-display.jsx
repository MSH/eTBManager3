import React from 'react';
import Form from '../../../forms/form';
import { Card, Profile } from '../../../components';
import { Button } from 'react-bootstrap';

import moment from 'moment';
import { getSchema } from './followup-display-schemas';

export default class FollowupDisplay extends React.Component {

	renderHeader1() {
		const followup = this.props.followup;

		const schema = getSchema(followup.type.id.toLowerCase());
		const doc = followup.data;

		if (!schema || !doc) {
			return null;
		}

		var datefield = 'dateCollected';
		if (followup.type.id === 'MEDEXAM' || followup.type.id === 'HIV' || followup.type.id === 'XRAY') {
			datefield = 'date';
		}

		var month = ' - ';
		month = month + followup.type.monthOfTreatment;

		const subtitle = (<div>
							{
								moment(doc[datefield]).format('ll')
							}
							{
								followup.type.monthOfTreatment && <span>{month}</span>
							}
						</div>);

		// IMPROVE THIS
		const buttonstyle = { float: 'right', marginTop: '-80px' };

		const header = (<div>
							<Profile
								title={followup.type.name}
								bottomline
								subtitle={subtitle}
								type={followup.type.id === 'MEDEXAM' ? 'medexam' : 'exam'} />

							<div style={buttonstyle}>
								<Button bsStyle="primary">{'Edit'}</Button>
							</div>
						</div>);

		return header;
	}

	render() {
		const followup = this.props.followup;

		const schema = getSchema(followup.type.id.toLowerCase());
		const doc = followup.data;

		if (!schema || !doc) {
			return null;
		}

		return (
			<Card header={this.renderHeader1()}>
				<Form schema={schema} doc={doc} readOnly/>
			</Card>);
	}
}

FollowupDisplay.propTypes = {
	followup: React.PropTypes.object.isRequired
};
