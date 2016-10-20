import { app } from '../../core/app';

/**
 * Return schemas to display followup results
 * @return {[type]} [description]
 */
const displaySchemas = {
    MEDICAL_EXAMINATION: {
        controls: [
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
                property: 'usingPrescMedicines',
                type: 'yesNo',
                label: __('MedicalExamination.usingPrescMedicines'),
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
                property: 'weight',
                label: __('MedicalExamination.weight'),
                size: { sm: 4 }
            },
            {
                type: 'number',
                label: __('MedicalExamination.height'),
                property: 'height',
                size: { sm: 4 }
            },
            {
                type: 'string',
                label: __('global.comments'),
                property: 'comments',
                size: { sm: 12 }
            }]
    },

    EXAM_MICROSCOPY: {
        controls: [
            {
                type: 'select',
                label: __('SpecimenType'),
                property: 'sampleType',
                options: app.getState().app.lists.SampleType,
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
                property: 'laboratory.name',
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

    EXAM_CULTURE: {
        controls: [
            {
                type: 'string',
                label: __('PatientSample.sampleNumber'),
                property: 'sampleNumber',
                size: { sm: 4 }
            },
            {
                type: 'string',
                label: __('Laboratory'),
                property: 'laboratory.name',
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

    EXAM_XPERT: {
        controls: [
            {
                type: 'string',
                label: __('PatientSample.sampleNumber'),
                property: 'sampleNumber',
                size: { sm: 4 }
            },
            {
                type: 'string',
                label: __('Laboratory'),
                property: 'laboratory.name',
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
                type: 'string',
                label: __('global.comments'),
                property: 'comments',
                size: { sm: 12 }
            }]
    },

    EXAM_DST: {
        controls: [
            {
                type: 'string',
                label: __('PatientSample.sampleNumber'),
                property: 'sampleNumber',
                size: { sm: 4 }
            },
            {
                type: 'string',
                label: __('Laboratory'),
                property: 'laboratory.name',
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
                property: 'resultAm',
                type: 'select',
                label: __('cases.examdst.resultAm'),
                options: app.getState().app.lists.DstResult,
                size: { md: 2 }
            },
            {
                property: 'resultCfz',
                type: 'select',
                label: __('cases.examdst.resultCfz'),
                options: app.getState().app.lists.DstResult,
                size: { md: 2 }
            },
            {
                property: 'resultCm',
                type: 'select',
                label: __('cases.examdst.resultCm'),
                options: app.getState().app.lists.DstResult,
                size: { md: 2 }
            },
            {
                property: 'resultCs',
                type: 'select',
                label: __('cases.examdst.resultCs'),
                options: app.getState().app.lists.DstResult,
                size: { md: 2 }
            },
            {
                property: 'resultE',
                type: 'select',
                label: __('cases.examdst.resultE'),
                options: app.getState().app.lists.DstResult,
                size: { md: 2 }
            },
            {
                property: 'resultEto',
                type: 'select',
                label: __('cases.examdst.resultEto'),
                options: app.getState().app.lists.DstResult,
                size: { md: 2 }
            },
            {
                property: 'resultH',
                type: 'select',
                label: __('cases.examdst.resultH'),
                options: app.getState().app.lists.DstResult,
                size: { md: 2 }
            },
            {
                property: 'resultLfx',
                type: 'select',
                label: __('cases.examdst.resultLfx'),
                options: app.getState().app.lists.DstResult,
                size: { md: 2 }
            },
            {
                property: 'resultOfx',
                type: 'select',
                label: __('cases.examdst.resultOfx'),
                options: app.getState().app.lists.DstResult,
                size: { md: 2 }
            },
            {
                property: 'resultR',
                type: 'select',
                label: __('cases.examdst.resultR'),
                options: app.getState().app.lists.DstResult,
                size: { md: 2 }
            },
            {
                property: 'resultS',
                type: 'select',
                label: __('cases.examdst.resultS'),
                options: app.getState().app.lists.DstResult,
                size: { md: 2 }
            },
            {
                property: 'resultZ',
                type: 'select',
                label: __('cases.examdst.resultZ'),
                options: app.getState().app.lists.DstResult,
                size: { md: 2 }
            }]
    },

    EXAM_HIV: {
        defaultProperties: {
            startedART: doc => doc.startedARTdate === null ? 'No' : 'Yes',
            startedCPT: doc => doc.startedCPTdate === null ? 'No' : 'Yes'
        },
        controls: [
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

    EXAM_XRAY: {
        controls: [
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
    MEDICAL_EXAMINATION: {
        controls: [
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
                property: 'usingPrescMedicines',
                type: 'yesNo',
                label: __('MedicalExamination.usingPrescMedicines'),
                size: { sm: 12 },
                required: true
            },
            {
                type: 'string',
                label: __('MedicalExamination.reasonNotUsingPrescMedicines'),
                property: 'reasonNotUsingPrescMedicines',
                required: true,
                size: { sm: 12 },
                visible: value => value.usingPrescMedicines === false
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

    EXAM_MICROSCOPY: {
        controls: [
            {
                type: 'date',
                label: __('cases.exams.date'),
                property: 'date',
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
                type: 'string',
                label: __('SampleType.OTHER'),
                property: 'otherSampleType',
                size: { sm: 6 },
                visible: value => value.sampleType === 'OTHER'
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
                property: 'laboratoryId',
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
                controls: [
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

    EXAM_CULTURE: {
        controls: [
            {
                type: 'date',
                label: __('cases.exams.date'),
                property: 'date',
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
                property: 'laboratoryId',
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
                property: 'numberOfColonies',
                type: 'select',
                label: __('ExamCulture.numberOfColonies'),
                options: { from: 1, to: 9 },
                size: { sm: 6 },
                visible: value => value.result === 'POSITIVE'
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

    EXAM_XPERT: {
        controls: [
            {
                type: 'date',
                label: __('cases.exams.date'),
                property: 'date',
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
                property: 'laboratoryId',
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
                size: { sm: 12 }
            },
            {
                type: 'text',
                label: __('global.comments'),
                property: 'comments',
                size: { sm: 12 }
            }]
    },

    EXAM_DST: {
        controls: [
            {
                type: 'date',
                label: __('cases.exams.date'),
                property: 'date',
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
                property: 'laboratoryId',
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
                property: 'resultAm',
                type: 'select',
                label: __('cases.examdst.resultAm'),
                options: app.getState().app.lists.DstResult,
                required: true,
                defaultValue: 'NOTDONE',
                size: { md: 4 }
            },
            {
                property: 'resultCfz',
                type: 'select',
                label: __('cases.examdst.resultCfz'),
                options: app.getState().app.lists.DstResult,
                required: true,
                defaultValue: 'NOTDONE',
                size: { md: 4 }
            },
            {
                property: 'resultCm',
                type: 'select',
                label: __('cases.examdst.resultCm'),
                options: app.getState().app.lists.DstResult,
                required: true,
                defaultValue: 'NOTDONE',
                size: { md: 4 }
            },
            {
                property: 'resultCs',
                type: 'select',
                label: __('cases.examdst.resultCs'),
                options: app.getState().app.lists.DstResult,
                required: true,
                defaultValue: 'NOTDONE',
                size: { md: 4 }
            },
            {
                property: 'resultE',
                type: 'select',
                label: __('cases.examdst.resultE'),
                options: app.getState().app.lists.DstResult,
                required: true,
                defaultValue: 'NOTDONE',
                size: { md: 4 }
            },
            {
                property: 'resultEto',
                type: 'select',
                label: __('cases.examdst.resultEto'),
                options: app.getState().app.lists.DstResult,
                required: true,
                defaultValue: 'NOTDONE',
                size: { md: 4 }
            },
            {
                property: 'resultH',
                type: 'select',
                label: __('cases.examdst.resultH'),
                options: app.getState().app.lists.DstResult,
                required: true,
                defaultValue: 'NOTDONE',
                size: { md: 4 }
            },
            {
                property: 'resultLfx',
                type: 'select',
                label: __('cases.examdst.resultLfx'),
                options: app.getState().app.lists.DstResult,
                required: true,
                defaultValue: 'NOTDONE',
                size: { md: 4 }
            },
            {
                property: 'resultOfx',
                type: 'select',
                label: __('cases.examdst.resultOfx'),
                options: app.getState().app.lists.DstResult,
                required: true,
                defaultValue: 'NOTDONE',
                size: { md: 4 }
            },
            {
                property: 'resultR',
                type: 'select',
                label: __('cases.examdst.resultR'),
                options: app.getState().app.lists.DstResult,
                required: true,
                defaultValue: 'NOTDONE',
                size: { md: 4 }
            },
            {
                property: 'resultS',
                type: 'select',
                label: __('cases.examdst.resultS'),
                options: app.getState().app.lists.DstResult,
                required: true,
                defaultValue: 'NOTDONE',
                size: { md: 4 }
            },
            {
                property: 'resultZ',
                type: 'select',
                label: __('cases.examdst.resultZ'),
                options: app.getState().app.lists.DstResult,
                required: true,
                defaultValue: 'NOTDONE',
                size: { md: 4 }
            },
            {
                type: 'text',
                label: __('global.comments'),
                property: 'comments',
                size: { sm: 12 }
            }]
    },

    EXAM_HIV: {
        controls: [
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
                controls: [
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

    EXAM_XRAY: {
        controls: [
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
    { id: 'MEDICAL_EXAMINATION', name: __('FollowUpType.MEDICAL_EXAMINATION'), icon: 'stethoscope', permission: 'CASE_MED_EXAM', crud: 'medexam' },
    { id: 'EXAM_MICROSCOPY', name: __('FollowUpType.EXAM_MICROSCOPY'), icon: 'file-text', permission: 'EXAM_MICROSCOPY', crud: 'exammic' },
    { id: 'EXAM_CULTURE', name: __('FollowUpType.EXAM_CULTURE'), icon: 'file-text', permission: 'EXAM_CULTURE', crud: 'examcul' },
    { id: 'EXAM_XPERT', name: __('FollowUpType.EXAM_XPERT'), icon: 'file-text', permission: 'permision', crud: 'examxpert' },
    { id: 'EXAM_DST', name: __('FollowUpType.EXAM_DST'), icon: 'file-text', permission: 'EXAM_DST', crud: 'examdst' },
    { id: 'EXAM_XRAY', name: __('FollowUpType.EXAM_XRAY'), icon: 'file-text', permission: 'EXAM_XRAY', crud: 'examxray' },
    { id: 'EXAM_HIV', name: __('FollowUpType.EXAM_HIV'), icon: 'file-text', permission: 'EXAM_HIV', crud: 'examhiv' }
];

export function getFollowUpTypes() {
    return folloupTypes;
}

export function getFollowUpType(followupId) {
    return folloupTypes.find(item => item.id === followupId);
}

export function getDisplaySchema(followupId) {
    return displaySchemas[followupId];
}

export function getEditSchema(followupId) {
    return editSchemas[followupId];
}
