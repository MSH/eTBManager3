import React from 'react';
import { Button, ButtonToolbar, Grid, Col, Row } from 'react-bootstrap';
import { Card, AsyncButton } from '../../../components';
import Form from '../../../forms/form';
import { app } from '../../../core/app';


const notifForm = {
	layout: [
	{
		label: 'Date entered in SL treatment TB register',
		property: 'registrationDate',
		type: 'date',
		required: true,
		size: { sm: 4 }
	},
	{
		label: 'BMU TB register number',
		property: 'registrationCode',
		type: 'string',
		max: 100,
		size: { sm: 4 }
	},
	{
		label: 'Date entered in BMU TB register',
		property: 'registrationDate2',
		type: 'date',
		size: { sm: 4 }
	},
	{
		el: 'subtitle',
		label: 'Patient data',
		size: { sm: 12 }
	},
	{
		property: 'name',
		label: 'Patient name',
		type: 'string',
		required: true,
		size: { sm: 6 }
	},
	{
		property: 'gender',
		label: __('Gender'),
		required: true,
		type: 'select',
		options: [
			{ id: 'MALE', name: __('Gender.MALE') },
			{ id: 'FEMALE', name: __('Gender.FEMALE') }
		],
		size: { sm: 4 }
	},
	{
		property: 'birthDate',
		label: __('Patient.birthDate'),
		type: 'date',
		size: { sm: 4, newLine: true }
	},
	{
		property: 'age',
		label: __('TbCase.age'),
		required: true,
		type: 'number',
		size: { sm: 2 }
	},
	{
		property: 'nationality',
		label: __('Nationality'),
		type: 'select',
		options: [
			{ id: 'NATIVE', name: 'Native' },
			{ id: 'FOREIGN', name: 'Foreign' }
		],
		size: { sm: 4 }
	},
	{
		property: 'motherName',
		label: __('Patient.motherName'),
		type: 'string',
		size: { sm: 6 }
	},

	{
		el: 'subtitle',
		label: __('cases.details.addressnotif'),
		size: { sm: 12 }
	},
	{
		property: 'notifAddress.address',
		type: 'string',
		required: true,
		label: __('Address.address'),
		size: { sm: 6 }
	},
	{
		property: 'notifAddress.complement',
		type: 'string',
		label: __('Address.complement'),
		size: { sm: 6, newLine: true }
	},
	{
		property: 'notifAddress.adminUnit',
		type: 'adminUnit',
		size: { sm: 6, newLine: true }
	},
	{
		property: 'notifAddress.zipCode',
		label: __('Address.zipCode'),
		type: 'string',
		max: 20,
		size: { sm: 4, newLine: true }
	},
	{
		property: 'phoneNumber',
		label: __('TbCase.phoneNumber'),
		type: 'string',
		max: 20,
		size: { sm: 4, newLine: true }
	},
	{
		property: 'mobileNumber',
		label: __('TbCase.mobileNumber'),
		type: 'string',
		max: 20,
		size: { sm: 4, smOffset: 2 }
	},

	{
		el: 'subtitle',
		label: __('cases.details.case'),
		size: { sm: 12 }
	},
	{
		property: 'notificationUnit',
		label: __('TbCase.notificationUnit'),
		type: 'unit',
		size: { sm: 6 }
	},
	{
		label: __('TbCase.diagnosisDate'),
		property: 'diagnosisDate',
		type: 'date',
		required: true,
		size: { sm: 4, newLine: true }
	},
	{
		label: __('DrugResistanceType'),
		property: 'drugResistanceType',
		type: 'select',
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
		label: 'Registration group',
		property: 'registrationGroup',
		type: 'select',
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
		label: __('InfectionSite'),
		property: 'infectionSite',
		type: 'select',
		options: [
			{ id: 'PULMONARY', name: __('InfectionSite.PULMONARY') },
			{ id: 'EXTRAPULMONARY', name: __('InfectionSite.EXTRAPULMONARY') },
			{ id: 'BOTH', name: __('InfectionSite.BOTH') }
		],
		size: { sm: 6 }
	},
	{
		label: __('TbField.PULMONARY_TYPES'),
		property: 'pulmonaryType',
		visible: doc => doc.infectionSite === 'PULMONARY' || doc.infectionSite === 'BOTH',
		type: 'select',
		options: [
			{ id: 'UNILATERAL_INF', name: 'Unilateral Infiltrate' },
			{ id: 'UNILATERAL_CAV', name: 'Unilateral Cavitary' },
			{ id: 'BILATERAL_INF', name: 'Bilateral infiltrate' },
			{ id: 'BILATERAL_CAV', name: 'Bilateral Cavitary' },
			{ id: 'DESTRUCTIOn', name: 'Destruction' },
			{ id: 'NORMAL', name: 'Normal' },
			{ id: 'OTHER', name: 'Other' }
		],
		size: { sm: 6 }
	},
	{
		label: __('TbField.EXTRAPULMONARY_TYPES'),
		property: 'pulmonaryType',
		visible: doc => doc.infectionSite === 'EXTRAPULMONARY' || doc.infectionSite === 'BOTH',
		type: 'select',
		options: [
			{ id: 'PLEURA', name: 'Pleura' },
			{ id: 'LYMPH', name: 'Lymph Nodes' },
			{ id: 'ABDOMEN', name: 'Abdomen' },
			{ id: 'GENITOURINARY', name: 'Genitourinary Tract' },
			{ id: 'SKIN', name: 'Skin' },
			{ id: 'JOINTS', name: 'Joints and Bones' },
			{ id: 'MENINGES', name: 'Meninges' },
			{ id: 'INTRA_THORACIC', name: 'Tuberculous Intra-thoracic Lymphadenopathy' },
			{ id: 'PLEURAL', name: 'Tuberculous Pleural Effusion' },
			{ id: 'OTHER', name: 'Other' }
		],
		size: { sm: 6 }
	},
	{
		label: __('TbField.EXTRAPULMONARY_TYPES') + ' (2)',
		property: 'pulmonaryType',
		visible: doc => doc.infectionSite === 'EXTRAPULMONARY' || doc.infectionSite === 'BOTH',
		type: 'select',
		options: [
			{ id: 'PLEURA', name: 'Pleura' },
			{ id: 'LYMPH', name: 'Lymph Nodes' },
			{ id: 'ABDOMEN', name: 'Abdomen' },
			{ id: 'GENITOURINARY', name: 'Genitourinary Tract' },
			{ id: 'SKIN', name: 'Skin' },
			{ id: 'JOINTS', name: 'Joints and Bones' },
			{ id: 'MENINGES', name: 'Meninges' },
			{ id: 'INTRA_THORACIC', name: 'Tuberculous Intra-thoracic Lymphadenopathy' },
			{ id: 'PLEURAL', name: 'Tuberculous Pleural Effusion' },
			{ id: 'OTHER', name: 'Other' }
		],
		size: { sm: 6 }
	}
	]
};

/**
 * Component that displays and handle notification form
 */
export default class NotifForm extends React.Component {

	componentWillMount() {
		this.setState({ schema: notifForm });
	}

	cancelClick() {
		app.goto('/sys/home/unit/cases');
	}

	render() {
		const schema = this.state && this.state.schema;
		if (!schema) {
			return null;
		}

		return (
			<Grid fluid className="mtop-2x">
				<Row>
					<Col mdOffset={2} md={9}>
						<Card title="New notification - TB Case">
							<Form schema={schema} doc={this.props.patient} />
							<ButtonToolbar>
								<AsyncButton bsStyle="primary">{__('action.save')}</AsyncButton>
								<Button bsStyle="link" onClick={this.cancelClick}>{__('action.cancel')}</Button>
							</ButtonToolbar>
						</Card>
					</Col>
				</Row>
			</Grid>
			);
	}
}

NotifForm.propTypes = {
	patient: React.PropTypes.object
};
