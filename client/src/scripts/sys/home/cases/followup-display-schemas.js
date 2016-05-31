import { app } from '../../../core/app';

/**
 * Return schemas to display followup results
 * @return {[type]} [description]
 */

const medexam = {
	layout: [
	{
		type: 'select',
		label: __('MedAppointmentType'),
		property: 'appointmentType',
		options: app.getState().app.lists.MedAppointmentType,
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('MedicalExamination.responsible'),
		property: 'responsible',
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
		size: { sm: 4 }
	},
	{
		type: 'number',
		label: __('MedicalExamination.height'),
		property: '{height} cm',
		size: { sm: 4 }
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
		visible: value => value.otherSampleType !== null && value.otherSampleType !== '',
		label: __('SpecimenType'),
		property: '{sampleType} - {otherSampleType}',
		size: { sm: 4 }
	},
	{
		type: 'string',
		visible: value => value.otherSampleType === null || value.otherSampleType === '',
		label: __('SpecimenType'),
		property: 'sampleType',
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('SampleType.OTHER'),
		property: 'otherSampleType',
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
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
		type: 'string',
		label: __('Laboratory'),
		property: 'laboratory', // TODOMS: use 'laboratory.name' when not using mockData anymore.
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('ExamStatus'),
		property: 'status',
		options: app.getState().app.lists.ExamStatus,
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
		size: { sm: 12 }
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
		type: 'string',
		label: __('Laboratory'),
		property: 'laboratory', // TODOMS: use 'laboratory.name' when not using mockData anymore.
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('ExamStatus'),
		property: 'status',
		options: app.getState().app.lists.ExamStatus,
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
		label: __('cases.exams.media'),
		property: 'method',
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 8 }
	}]
};

const xpert = {
	layout: [
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('Laboratory'),
		property: 'laboratory', // TODOMS: use 'laboratory.name' when not using mockData anymore.
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('ExamStatus'),
		property: 'status',
		options: app.getState().app.lists.ExamStatus,
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

const dstresultschema = {
	layout: [
		{
			property: 'substance', // TODOMS: use 'substance.name' when not using mockData anymore.
			type: 'string',
			label: __('Medicine.substances'),
			size: { md: 6 }
		},
		{
			property: 'result',
			type: 'select',
			label: __('cases.details.result'),
			options: app.getState().app.lists.DstResult,
			size: { md: 6 }
		}
	]
	};

const dstROcolumns = [
			{
				content: 'substance',
				size: { sm: 6 }
			},
			{
				content: 'result',
				size: { sm: 6 }
			}
		];

const dst = {
	layout: [
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('Laboratory'),
		property: 'laboratory', // TODOMS: use 'laboratory.name' when not using mockData anymore.
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('ExamStatus'),
		property: 'status',
		options: app.getState().app.lists.ExamStatus,
		size: { sm: 4 }
	},
	{
		type: 'date',
		label: __('cases.exams.dateRelease'),
		property: 'dateRelease',
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('cases.exams.method'),
		property: 'method',
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 4 }
	},
	{
		property: 'results',
		type: 'tableForm',
		fschema: dstresultschema,
		readOnlyColumns: dstROcolumns,
		label: __('cases.details.result'),
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

export function getDisplaySchema(followupType) {
	switch (followupType.toLowerCase()) {
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
