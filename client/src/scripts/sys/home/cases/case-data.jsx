import React from 'react';
import { Alert } from 'react-bootstrap';
import { Card } from '../../../components';
import Form from '../../../forms/form';
import CardWithComments from './card-with-comments';

import Contacts from './contacts';
import CaseAdvReacts from './case-adv-reacts';
import CaseComments from './case-comments';


const data = {
	layout: [
	{
		type: 'date',
		label: 'Date entered in SL treatment TB register',
		property: 'registrationDate',
		required: true,
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('TbCase.registrationCode'),
		property: 'registrationCode',
		max: 100,
		size: { sm: 4 }
	},
	{
		type: 'date',
		label: __('TbCase.registrationDate.TB'),
		property: 'registrationDate2',
		size: { sm: 4 }
	},
	{
		type: 'subtitle',
		label: __('cases.patientdata'),
		size: { sm: 12 }
	},
	{
		type: 'string',
		property: 'patient.name',
		label: __('Patient.name'),
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'select',
		property: 'patient.gender',
		label: __('Gender'),
		required: true,
		options: [
			{ id: 'MALE', name: __('Gender.MALE') },
			{ id: 'FEMALE', name: __('Gender.FEMALE') }
		],
		size: { sm: 4 }
	},
	{
		type: 'date',
		property: 'patient.birthDate',
		label: __('Patient.birthDate'),
		size: { sm: 4, newLine: true }
	},
	{
		type: 'number',
		property: 'age',
		label: __('TbCase.age'),
		required: true,
		size: { sm: 2 }
	},
	{
		type: 'select',
		property: 'nationality',
		label: __('Nationality'),
		options: [
			{ id: 'NATIVE', name: 'Native' },
			{ id: 'FOREIGN', name: 'Foreign' }
		],
		size: { sm: 4 }
	},
	{
		type: 'string',
		property: 'patient.motherName',
		label: __('Patient.motherName'),
		size: { sm: 6 }
	},

	{
		type: 'subtitle',
		label: __('cases.details.addressnotif'),
		size: { sm: 12 }
	},
	{
		type: 'string',
		property: 'notifAddress.address',
		required: true,
		label: __('Address.address'),
		size: { sm: 6 }
	},
	{
		type: 'string',
		property: 'notifAddress.complement',
		label: __('Address.complement'),
		size: { sm: 6, newLine: true }
	},
	{
		type: 'adminUnit',
		property: 'notifAddress.adminUnit',
		size: { sm: 6, newLine: true }
	},
	{
		type: 'string',
		property: 'notifAddress.zipCode',
		label: __('Address.zipCode'),
		max: 20,
		size: { sm: 4, newLine: true }
	},
	{
		type: 'string',
		property: 'phoneNumber',
		label: __('TbCase.phoneNumber'),
		max: 20,
		size: { sm: 4, newLine: true }
	},
	{
		type: 'string',
		property: 'mobileNumber',
		label: __('global.mobile'),
		max: 20,
		size: { sm: 4, smOffset: 2 }
	},

	{
		type: 'subtitle',
		label: __('cases.details.case'),
		size: { sm: 12 }
	},
	{
		type: 'unit',
		property: 'notificationUnit',
		label: __('TbCase.notificationUnit'),
		size: { sm: 6 }
	},
	{
		type: 'date',
		label: __('TbCase.diagnosisDate'),
		property: 'diagnosisDate',
		required: true,
		size: { sm: 4, newLine: true }
	},
	{
		type: 'select',
		label: __('DrugResistanceType'),
		property: 'drugResistanceType',
		options: [
			{ id: 'EXTENSIVEDRUG_RESISTANCE', name: __('DrugResistanceType.EXTENSIVEDRUG_RESISTANCE') },
			{ id: 'MONO_RESISTANCE', name: __('DrugResistanceType.MONO_RESISTANCE') },
			{ id: 'MONO_RESISTANCE_RIF', name: __('DrugResistanceType.MONO_RESISTANCE_RIF') },
			{ id: 'MULTIDRUG_RESISTANCE', name: __('DrugResistanceType.MULTIDRUG_RESISTANCE') },
			{ id: 'POLY_RESISTANCE', name: __('DrugResistanceType.POLY_RESISTANCE') },
			{ id: 'POLY_RESISTANCE_RIF', name: __('DrugResistanceType.POLY_RESISTANCE_RIF') },
			{ id: 'RIF_RESISTANCE', name: __('DrugResistanceType.RIF_RESISTANCE') }
		],
		size: { sm: 6, newLine: true }
	},
	{
		type: 'select',
		label: 'Registration group',
		property: 'registrationGroup',
		options: [
			{ id: 'NEW', name: 'New' },
			{ id: 'RELAPSE', name: 'Relapse' },
			{ id: 'AFTER_DEFAULT', name: 'After loss follow-up' },
			{ id: 'FAILURE_FT', name: __('PatientType.FAILURE_FT') },
			{ id: 'FAILURE_RT', name: __('PatientType.FAILURE_RT') },
			{ id: 'OTHER', name: __('PatientType.OTHER') }
		],
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('InfectionSite'),
		property: 'infectionSite',
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('TbCase.pulmonaryType'),
		property: 'pulmonaryType',
		visible: doc => doc.infectionSite === 'PULMONARY' || doc.infectionSite === 'BOTH',
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('TbCase.extrapulmonaryType'),
		property: 'pulmonaryType',
		visible: doc => doc.infectionSite === 'EXTRAPULMONARY' || doc.infectionSite === 'BOTH',
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('TbCase.extrapulmonaryType2'),
		property: 'pulmonaryType',
		visible: doc => doc.infectionSite === 'EXTRAPULMONARY' || doc.infectionSite === 'BOTH',
		size: { sm: 6 }
	}]
};

export default class CaseData extends React.Component {

	render() {
		const tbcase = this.props.tbcase;

		return (
			<div>
				<CaseComments tbcase={tbcase} group="data">
					<Card padding="combine">
						<Form schema={data} doc={tbcase} readOnly/>
					</Card>
				</CaseComments>

				<Contacts tbcase={this.props.tbcase} />

				<CaseAdvReacts tbcase={this.props.tbcase} />

				<CardWithComments title={__('TbField.COMORBIDITY')} tbcase={tbcase} group="comorbidities">
					<Alert bsStyle="warning">{'No record found'}</Alert>
				</CardWithComments>
			</div>
			);
	}
}


CaseData.propTypes = {
	tbcase: React.PropTypes.object
};
