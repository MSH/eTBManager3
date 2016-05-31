import { app } from '../../../core/app';

/**
 * Return schemas to display followup results
 * @return {[type]} [description]
 */
// TODOMS: nenhum group dentro de outro group funcionou. exemplo: rif result em xpert.

const medexam = {
	layout: [
	{
		type: 'date',
		label: __('cases.details.date'),
		property: 'date',
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('MedAppointmentType'),
		property: 'appointmentType',
		options: app.getState().app.lists.MedAppointmentType,
		size: { sm: 6 }
	},
	{
		type: 'string',
		label: __('MedicalExamination.responsible'),
		property: 'responsible',
		size: { sm: 12 }
	},
	{
		type: 'select',
		label: __('MedicalExamination.usingPrescMedicines'),
		property: 'usingPrescMedicines',
		options: app.getState().app.lists.YesNoType,
		required: true,
		size: { sm: 12 }
	},
	{
		type: 'group',
		visible: value => value.usingPrescMedicines === 'NO',
		layout: [
			{
				type: 'string',
				label: __('MedicalExamination.reasonNotUsingPrescMedicines'),
				property: 'reasonNotUsingPrescMedicines',
				required: true,
				size: { sm: 12 }
			}
		]
	},
	{
		type: 'number',
		property: 'weight',
		label: __('MedicalExamination.weight'),
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'number',
		label: __('MedicalExamination.height'),
		property: 'height',
		size: { sm: 6 }
	},
	{
		type: 'text',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 12 }
	}]
};

const microscopy = {
	layout: [
	{
		type: 'date',
		label: __('cases.exams.date'),
		property: 'dateCollected',
		required: true,
		size: { sm: 12 }
	},
	{
		type: 'select',
		label: __('SpecimenType'),
		property: 'sampleType',
		options: app.getState().app.lists.SampleType,
		size: { sm: 6 }
	},
	{
		type: 'group',
		visible: value => value.sampleType === 'OTHER',
		layout: [
			{
				type: 'string',
				label: __('SampleType.OTHER'),
				property: 'otherSampleType',
				size: { sm: 6 }
			}
		]
	},
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		size: { sm: 6, newLine: true }
	},
	{
		type: 'select',
		label: __('VisualAppearance'),
		property: 'visualAppearance',
		options: app.getState().app.lists.VisualAppearance,
		size: { sm: 6 }
	},
	{
		type: 'unit',
		label: __('Laboratory'),
		property: 'laboratory',
		unitType: 'LAB',
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'date',
		label: __('cases.exams.dateRelease'),
		property: 'dateRelease',
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		options: app.getState().app.lists.MicroscopyResult,
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'group',
		visible: value => value.result === 'POSITIVE',
		layout: [
			{
				type: 'select',
				label: __('cases.exams.afb'),
				property: 'numberOfAFB',
				options: { from: 1, to: 9 },
				size: { sm: 6 }
			}
		]
	},
	{
		type: 'text',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 12 }
	}]
};

const culture = {
	layout: [
	{
		type: 'date',
		label: __('cases.exams.date'),
		property: 'dateCollected',
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		size: { sm: 6 }
	},
	{
		type: 'unit',
		label: __('Laboratory'),
		property: 'laboratory',
		unitType: 'LAB',
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'date',
		label: __('cases.exams.dateRelease'),
		property: 'dateRelease',
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		options: app.getState().app.lists.CultureResult,
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'group',
		visible: value => value.result === 'POSITIVE',
		layout: [
			{
				property: 'numberOfColonies',
				type: 'select',
				label: __('ExamCulture.numberOfColonies'),
				options: { from: 1, to: 9 },
				size: { sm: 6 }
			}
		]
	},
	{
		type: 'string',
		label: __('cases.exams.media'),
		property: 'method',
		size: { sm: 12, newLine: true }
	},
	{
		type: 'text',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 12 }
	}]
};

const xpert = {
	layout: [
	{
		type: 'date',
		label: __('cases.exams.date'),
		property: 'dateCollected',
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		size: { sm: 6 }
	},
	{
		type: 'unit',
		label: __('Laboratory'),
		property: 'laboratory',
		unitType: 'LAB',
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'date',
		label: __('cases.exams.dateRelease'),
		property: 'dateRelease',
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		options: app.getState().app.lists.XpertResult,
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'group',
		visible: value => value.result === 'TB_DETECTED',
		layout: [
			{
				type: 'select',
				label: __('XpertResult.rifresult'),
				property: 'rifResult',
				options: app.getState().app.lists.XpertRifResult,
				required: true,
				size: { sm: 6 }
			}
		]
	},
	{
		type: 'text',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 12 }
	}]
};

const dstresultschema = {
	layout: [
		{
			property: 'substance',
			type: 'select',
			label: __('Medicine.substances'),
			options: 'substances',
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

const dst = {
	layout: [
	{
		type: 'date',
		label: __('cases.exams.date'),
		property: 'dateCollected',
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		size: { sm: 6 }
	},
	{
		type: 'unit',
		label: __('Laboratory'),
		property: 'laboratory',
		required: true,
		unitType: 'LAB',
		size: { sm: 12 }
	},
	{
		type: 'date',
		label: __('cases.exams.dateRelease'),
		property: 'dateRelease',
		size: { sm: 6 }
	},
	{
		type: 'string',
		label: __('cases.exams.method'),
		property: 'method',
		size: { sm: 6 }
	},
	{
		property: 'results',
		type: 'tableForm',
		fschema: dstresultschema,
		label: __('cases.details.result'),
		min: 1,
		size: { sm: 12 }
	},
	{
		type: 'text',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 12 }
	}]
};

const hiv = {
	layout: [
	{
		type: 'date',
		label: __('cases.details.date'),
		property: 'date',
		required: true,
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('Laboratory'),
		property: 'laboratory',
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		options: app.getState().app.lists.HIVResult,
		required: true,
		size: { sm: 4 }
	},
	{
		type: 'group',
		visible: value => value.result === 'POSITIVE',
		layout: [
			{
				type: 'date',
				label: __('cases.examhiv.art'),
				property: 'startedARTdate',
				size: { sm: 6 }
			},
			{
				type: 'date',
				label: __('cases.examhiv.cpt'),
				property: 'startedCPTdate',
				size: { sm: 6 }
			}
		]
	},
	{
		type: 'text',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 12 }
	}]
};

const xray = {
	layout: [
	{
		type: 'date',
		label: __('cases.details.date'),
		property: 'date',
		required: true,
		size: { sm: 4 }
	},
	{
		type: 'string',
		label: __('TbField.XRAYPRESENTATION'),
		property: 'presentation',
		required: true,
		size: { sm: 4 }
	},
	{
		type: 'select',
		label: __('XRayEvolution'),
		property: 'evolution',
		options: app.getState().app.lists.XRayEvolution,
		size: { sm: 4 }
	},
	{
		type: 'text',
		label: __('global.comments'),
		property: 'comments',
		size: { sm: 12 }
	}]
};

export function getEditSchema(followupType) {
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
