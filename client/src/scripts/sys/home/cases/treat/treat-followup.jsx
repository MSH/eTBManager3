import React from 'react';
import { Card } from '../../../../components';

/**
 * Display the treatment followup card
 */
export default class TreatFollowup extends React.Component {

	render() {
		console.log('TreatFollowup ', this.props);

		return (
			<Card title={__('cases.details.treatment.medintake')} />
			);
	}
}
