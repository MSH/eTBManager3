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
		type: 'string',
		label: 'positionResponsible',
		property: 'positionResponsible',
		size: { sm: 12 }
	},
	{
		type: 'select',
		label: __('MedicalExamination.usingPrescMedicines'),
		property: 'usingPrescMedicines',
		options: app.getState().app.lists.YesNoType,
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
				size: { sm: 12 }
			}
		]
	},
	{
		type: 'number',
		property: '{weight} Kg',
		label: __('MedicalExamination.weight'),
		size: { sm: 6 }
	},
	{
		type: 'number',
		label: __('MedicalExamination.height'),
		property: '{height} cm',
		size: { sm: 6 }
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
		type: 'date',
		label: __('cases.exams.date'),
		property: 'dateCollected',
		size: { sm: 6 }
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
				size: { sm: 12 }
			}
		]
	},
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('VisualAppearance'),
		property: 'visualAppearance',
		options: app.getState().app.lists.VisualAppearance,
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('Laboratory'),
		property: 'laboratory',
		options: 'laboratories',
		size: { sm: 12 }
	},
	{
		type: 'select',
		label: __('ExamStatus'),
		property: 'status',
		options: app.getState().app.lists.ExamStatus,
		size: { sm: 12 }
	},
	{
		type: 'group',
		visible: value => value.status === 'PERFORMED',
		layout: [
			{
				type: 'date',
				label: __('cases.exams.dateRelease'),
				property: 'dateRelease',
				size: { sm: 12 }
			},
			{
				type: 'select',
				label: __('cases.details.result'),
				property: 'result',
				options: app.getState().app.lists.MicroscopyResult,
				size: { sm: 12 }
			},
			{
				type: 'group',
				visible: value => value.result === 'POSITIVE',
				layout: [
					{
						type: 'number',
						label: __('cases.exams.afb'),
						property: 'numberOfAFB',
						size: { sm: 12 }
					}
				]
			}
		]
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
		type: 'date',
		label: __('cases.exams.date'),
		property: 'dateCollected',
		size: { sm: 6 }
	},
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
		options: 'laboratories',
		size: { sm: 12 }
	},
	{
		type: 'select',
		label: __('ExamStatus'),
		property: 'status',
		options: app.getState().app.lists.ExamStatus,
		size: { sm: 12 }
	},
	{
		type: 'group',
		visible: value => value.status === 'PERFORMED',
		layout: [
			{
				type: 'date',
				label: __('cases.exams.dateRelease'),
				property: 'dateRelease',
				size: { sm: 12 }
			},
			{
				type: 'select',
				label: __('cases.details.result'),
				property: 'result',
				options: app.getState().app.lists.CultureResult,
				size: { sm: 12 }
			},
			{
				type: 'group',
				visible: value => value.result === 'POSITIVE',
				layout: [
					{
						type: 'number',
						label: __('ExamCulture.numberOfColonies'),
						property: 'numberOfColonies',
						size: { sm: 12 }
					}
				]
			},
			{
				type: 'string',
				label: __('cases.exams.media'),
				property: 'method',
				size: { sm: 12 }
			}
		]
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
		type: 'date',
		label: __('cases.exams.date'),
		property: 'dateCollected',
		size: { sm: 6 }
	},
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
		options: 'laboratories',
		size: { sm: 12 }
	},
	{
		type: 'select',
		label: __('ExamStatus'),
		property: 'status',
		options: app.getState().app.lists.ExamStatus,
		size: { sm: 12 }
	},

	{
		type: 'group',
		visible: value => value.status === 'PERFORMED',
		layout: [
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
						size: { sm: 12 }
					}
				]
			}
		]
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
		size: { sm: 6 }
	},
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
		options: 'laboratories',
		size: { sm: 12 }
	},
	{
		type: 'select',
		label: __('ExamStatus'),
		property: 'status',
		options: app.getState().app.lists.ExamStatus,
		size: { sm: 12 }
	},
	{
		type: 'group',
		visible: value => value.status === 'PERFORMED',
		layout: [
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
				type: 'string',
				label: __('global.comments'),
				property: 'comments',
				size: { sm: 12 }
			},
			{
				property: 'results',
				type: 'tableForm',
				fschema: dstresultschema,
				label: __('cases.details.result'),
				size: { sm: 12 }
			}
		]
	}]
};

const hiv = {
	defaultProperties: {
		startedART: doc => doc.startedARTdate === null ? 'No' : 'Yes',
		startedCPT: doc => doc.startedCPTdate === null ? 'No' : 'Yes'
	},
	layout: [
	{
		type: 'date',
		label: __('cases.details.date'),
		property: 'date',
		size: { sm: 6 }
	},
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
		size: { sm: 12 }
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
		type: 'string',
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
		size: { sm: 12 }
	},
	{
		type: 'string',
		label: __('TbField.XRAYPRESENTATION'),
		property: 'presentation',
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('XRayEvolution'),
		property: 'evolution',
		options: app.getState().app.lists.XRayEvolution,
		size: { sm: 6 }
	},
	{
		type: 'string',
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
