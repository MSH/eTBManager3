import React from 'react';
import Form from '../../../forms/form';
import { Card, Profile } from '../../../components';

import moment from 'moment';
import { getSchema } from './followup-display-schemas';

export default class FollowupDisplay extends React.Component {

	render() {
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

		const subtitle = (<div>
							{
								moment(doc[datefield]).format('L')
							}
							{
								' - TODOMS: put month of treatment'
							}
						</div>);

		const header = (
					<Profile
						title={followup.type.name}
						bottomline
						subtitle={subtitle}
						type={followup.type.id === 'MEDEXAM' ? 'medexam' : 'exam'} />);

		return (
			<Card header={header}>
				<Form schema={schema} doc={doc} readOnly/>
			</Card>);
	}
}

FollowupDisplay.propTypes = {
	followup: React.PropTypes.object.isRequired
};
