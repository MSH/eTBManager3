/**
 * Return schemas to display followup results
 * @return {[type]} [description]
 */
// TODOMS: remover todas as props que nao são relacionadas a exibição
// TODOMS: checar o tamanho dos campos string
// TODOMS: incluir as opções de result

const medexam = {
	layout: [
	{
		type: 'string',
		label: __('MedicalExamination.responsible'),
		property: 'responsible',
		max: 100,
		size: { sm: 6 }
	},
	{
		type: 'string',
		label: 'positionResponsible',
		property: 'positionResponsible',
		max: 100,
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('MedAppointmentType'),
		property: 'appointmentType',
		options: [
			{ id: 'SCHEDULLED', name: 'Schedulled' },
			{ id: 'EXTRA', name: 'Extra' }
		],
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('MedicalExamination.usingPrescMedicines'),
		property: 'usingPrescMedicines',
		options: [
			{ id: 'YES', name: 'Yes' },
			{ id: 'NO', name: 'No' } // TODOMSR: if no include reason field here
		],
		size: { sm: 6 }
	},
	{
		type: 'number',
		property: 'weight',
		label: __('MedicalExamination.weight'),
		required: true,
		size: { sm: 3 }
	},
	{
		type: 'number',
		label: __('MedicalExamination.height'),
		property: 'height',
		required: true,
		size: { sm: 3 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		max: 100,
		size: { sm: 6 }
	}]
};

const microscopy = {
	layout: [
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		max: 100,
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
		options: [
			{ id: 'SPUTUM', name: 'Sputum' },
			{ id: 'OTHER', name: 'Other' }
		],
		size: { sm: 6 }
	}, // TODOMSR: other sample type, como coloco aqui aproveitando o mesmo campo?
	{
		type: 'select',
		label: __('VisualAppearance'),
		property: 'visualAppearance',
		options: [
			{ id: 'BLOOD_STAINED', name: 'Blood stained' },
			{ id: 'MUCOPURULENT', name: 'Mucopurulent' },
			{ id: 'SALIVA', name: 'Saliva' }
		],
		size: { sm: 6 } // TODOMSR: as estruturas com name e id n aparecem os valores.
	},
	{
		type: 'date',
		label: __('cases.exams.dateRelease'),
		property: 'dateRelease',
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		size: { sm: 6 }
	},
	{
		type: 'number',
		label: __('cases.exams.afb'),
		property: 'numberOfAFB',
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		max: 100,
		size: { sm: 6 }
	}]
};

const culture = {
	layout: [
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		max: 100,
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
		max: 100,
		size: { sm: 4 }
	},
	{
		type: 'date',
		label: __('cases.exams.dateRelease'),
		property: 'dateRelease',
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		size: { sm: 6 }
	},
	{
		type: 'number',
		label: __('ExamCulture.numberOfColonies'),
		property: 'numberOfColonies',
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		max: 100,
		size: { sm: 6 }
	}]
};

const xpert = {
	layout: [
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		max: 100,
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
		required: true,
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		size: { sm: 6 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'rifResult',
		options: [
			{ id: 'RIF_DETECTED', name: 'Rif resistance detected' },
			{ id: 'RIF_NOT_DETECTED', name: 'Rif resistance not detected' },
			{ id: 'RIF_INDETERMINATE', name: 'Rif resistance indeterminate' }
		],
		size: { sm: 6 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		max: 100,
		size: { sm: 6 }
	}]
};

const dst = {
	layout: [
	{
		type: 'string',
		label: __('PatientSample.sampleNumber'),
		property: 'sampleNumber',
		max: 100,
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
		required: true,
		size: { sm: 3 }
	},
	{
		type: 'string',
		label: __('cases.exams.method'),
		property: 'method',
		max: 100,
		size: { sm: 3 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		size: { sm: 12 } // TODOMS: listar os resultados
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		max: 100,
		size: { sm: 12 }
	}]
};

const hiv = {
	layout: [
	{
		type: 'string',
		label: __('Laboratory'),
		property: 'laboratory',
		max: 100,
		size: { sm: 3 }
	},
	{
		type: 'select',
		label: __('cases.details.result'),
		property: 'result',
		size: { sm: 3 }
	},
	{
		type: 'date',
		label: __('cases.examhiv.art'),
		property: 'startedCPTdate',
		size: { sm: 3 } // TODOMS: alem de mostrar a data mostrar sim ou não
	},
	{
		type: 'date',
		label: __('cases.examhiv.cpt'),
		property: 'startedARTdate',
		size: { sm: 3 } // TODOMS: alem de mostrar a data mostrar sim ou não
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		max: 100,
		size: { sm: 12 }
	}]
};

const xray = {
	layout: [
	{
		type: 'string',
		label: __('TbField.XRAYPRESENTATION'),
		property: 'presentation',
		max: 100,
		size: { sm: 2 }
	},
	{
		type: 'select',
		label: __('XRayEvolution'),
		property: 'evolution',
		options: [
			{ id: 'IMPROVED', name: 'Improved' },
			{ id: 'PROGRESSED', name: 'Progressed' },
			{ id: 'STABLE', name: 'Stable' }
		],
		size: { sm: 2 }
	},
	{
		type: 'string',
		label: __('global.comments'),
		property: 'comments',
		max: 100,
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
