import React from 'react';
import { Card } from '../../../components';
import Form from '../../../forms/form';


const mockTreat = {
	category: {
		id: 'FIRST_LINE',
		name: 'Initial regimen with first line drugs'
	},
	iniRegimen: {
		id: '123123123',
		name: 'Category IV standard regimen'
	},
	regimen: {
		id: '123123123-2',
		name: 'Category IV-2 standard regimen'
	},
	period: {
		ini: '2015-01-01',
		end: '2016-01-01'
	},
	progress: 38
};

export default class CaseTreatment extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			sc1: {
				layout: [
					{
						property: 'regimen.name',
						type: 'string',
						label: 'Regimen',
						visible: doc => doc.regimen.id === doc.iniRegimen.id,
						size: { md: 12 }
					},
					{
						type: 'text',
						property: '{regimen.name}\nStarted as {iniRegimen.name}',
						label: 'Regimen',
						visible: doc => doc.regimen.id !== doc.iniRegimen.id,
						size: { md: 12 }
					}
				]
			}
		};
	}

	render() {

		return (
			<div>
				<Card title={__('cases.details.treatment')}>
					<Form doc={mockTreat} schema={this.state.sc1} readOnly />
				</Card>
				<Card title={__('cases.details.treatment.medintake')}>
				</Card>
			</div>
			);
	}
}

