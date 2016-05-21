import { app } from '../../../core/app';

/**
 * Return schemas to display followup results
 * @return {[type]} [description]
 */
// TODOMS: colocar os laboratories para funcionar
// TODOMS: listar os resultados de dst
// TODOMS: microscopy: other sample type, como coloco aqui aproveitando o mesmo campo?
// TODOMS: definir oq sera feito do campo exam status

const medexam = {
	layout: [
	{
		type: 'string',
		label: __('MedicalExamination.responsible'),
		property: 'responsible',
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: 'positionResponsible',
		property: 'positionResponsible',
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('MedAppointmentType'),
		property: 'appointmentType',
		options: app.getState().app.lists.MedAppointmentType,
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('MedicalExamination.usingPrescMedicines'),
		property: 'usingPrescMedicines',
		options: app.getState().app.lists.YesNoType,
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('MedicalExamination.reasonNotUsingPrescMedicines'),
		property: 'reasonNotUsingPrescMedicines',
		size: { sm: 4 }
	},
	{
		type: 'number',
		property: '{weight} Kg',
		label: __('MedicalExamination.weight'),
		size: { sm: 2 }
	},
	{
		type: 'number',
		label: __('MedicalExamination.height'),
		property: '{height} cm',
		size: { sm: 2 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 12 }
	}]
};

const microscopy = {
	layout: [
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('Laboratory'),
		property: 'laboratory',
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('SpecimenType'),
		property: 'sampleType',
		options: app.getState().app.lists.SampleType,
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('VisualAppearance'),
		property: 'visualAppearance',
		options: app.getState().app.lists.VisualAppearance,
		size: { sm: 4 }
	},
	{
		type: 'date',
		label: __('cases.exams.dateRelease'),
		property: 'dateRelease',
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		options: app.getState().app.lists.MicroscopyResult,
		size: { sm: 4 }
	},
	{
		type: 'number',
		label: __('cases.exams.afb'),
		property: 'numberOfAFB',
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 4 }
	}]
};

const culture = {
	layout: [
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('Laboratory'),
		property: 'laboratory',
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('cases.exams.media'),
		property: 'method',
		size: { sm: 4 }
	},
	{
		type: 'date',
		label: __('cases.exams.dateRelease'),
		property: 'dateRelease',
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		options: app.getState().app.lists.CultureResult,
		size: { sm: 4 }
	},
	{
		type: 'number',
		label: __('ExamCulture.numberOfColonies'),
		property: 'numberOfColonies',
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 12 }
	}]
};

const xpert = {
	layout: [
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('Laboratory'),
		property: 'laboratory',
		size: { sm: 6 }
	},
	{
		type: 'date',
		label: __('cases.exams.dateRelease'),
		property: 'dateRelease',
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		options: app.getState().app.lists.XpertResult,
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('XpertResult.rifresult'),
		property: 'rifResult',
		options: app.getState().app.lists.XpertRifResult,
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 12 }
	}]
};

const dst = {
	layout: [
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		size: { sm: 3 }
	},
	{
		type: 'select',
		label: __('Laboratory'),
		property: 'laboratory',
		size: { sm: 3 }
	},
	{
		type: 'date',
		label: __('cases.exams.dateRelease'),
		property: 'dateRelease',
		size: { sm: 3 }
	},
	{
		type: 'string',
		label: __('cases.exams.method'),
		property: 'method',
		size: { sm: 3 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		size: { sm: 12 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 12 }
	}]
};

const hiv = {
	defaultProperties: {
		startedART: doc => doc.startedARTdate === null ? 'No' : 'Yes',
		startedCPT: doc => doc.startedCPTdate === null ? 'No' : 'Yes'
	},
	layout: [
	{
		type: 'string',
		label: __('Laboratory'),
		property: 'laboratory',
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		options: app.getState().app.lists.HIVResult,
		size: { sm: 6 }
	},
	{
		type: 'string',
		label: __('cases.examhiv.art'),
		property: 'startedART',
		size: { sm: 3 }
	},
	{
		type: 'date',
		label: __('cases.examhiv.art'),
		property: 'startedARTdate',
		size: { sm: 3 }
	},
	{
		type: 'string',
		label: __('cases.examhiv.cpt'),
		property: 'startedCPT',
		size: { sm: 3 }
	},
	{
		type: 'date',
		label: __('cases.examhiv.cpt'),
		property: 'startedCPTdate',
		size: { sm: 3 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 12 }
	}]
};

const xray = {
	layout: [
	{
		type: 'string',
		label: __('TbField.XRAYPRESENTATION'),
		property: 'presentation',
		size: { sm: 2 }
	},
	{
		type: 'select',
		label: __('XRayEvolution'),
		property: 'evolution',
		options: app.getState().app.lists.XRayEvolution,
		size: { sm: 2 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 8 }
	}]
};

export function getSchema(followupType) {
	switch (followupType) {
		case 'medexam':
			return medexam;
		case 'microscopy':
			return microscopy;
		case 'culture':
			return culture;
		case 'xpert':
			return xpert;
		case 'dst':
			return dst;
		case 'hiv':
			return hiv;
		case 'xray':
			return xray;
		default:
			return null;
	}
}
