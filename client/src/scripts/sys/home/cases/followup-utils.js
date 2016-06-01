import { app } from '../../../core/app';

/**
 * Return schemas to display followup results
 * @return {[type]} [description]
 */
const displaySchemas = {
	medexam: {
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
	},

	microscopy: {
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
	},

	culture: {
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
	},

	xpert: {
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
	},

	dst: {
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
			fschema: {
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
			},
			label: __('cases.details.result'),
			size: { sm: 12 }
		}]
	},

	hiv: {
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
	},

	xray: {
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
	}
};

const editSchemas = {
	medexam: {
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
	},

	microscopy: {
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
	},

	culture: {
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
	},

	xpert: {
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
	},

	dst: {
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
			fschema: {
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
			},
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
	},

	hiv: {
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
	},

	xray: {
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
	}
};

const folloupTypes = [
	{ id: 'MEDEXAM', name: __('FollowUpType.MEDEXAM'), dateField: 'date', icon: 'stethoscope' },
	{ id: 'MICROSCOPY', name: __('FollowUpType.MICROSCOPY'), dateField: 'dateCollected', icon: 'file-text' },
	{ id: 'CULTURE', name: __('FollowUpType.CULTURE'), dateField: 'dateCollected', icon: 'file-text' },
	{ id: 'XPERT', name: __('FollowUpType.XPERT'), dateField: 'dateCollected', icon: 'file-text' },
	{ id: 'DST', name: __('FollowUpType.DST'), dateField: 'dateCollected', icon: 'file-text' },
	{ id: 'XRAY', name: __('FollowUpType.XRAY'), dateField: 'date', icon: 'file-text' },
	{ id: 'HIV', name: __('FollowUpType.HIV'), dateField: 'date', icon: 'file-text' }
];

export function getFollowUpTypes() {
	return folloupTypes;
}

export function getFollowUpType(followupId) {
	return folloupTypes.find(item => item.id === followupId);
}

export function getDisplaySchema(followupId) {
	return displaySchemas[followupId.toLowerCase()];
}

export function getEditSchema(followupId) {
	return editSchemas[followupId.toLowerCase()];
}
