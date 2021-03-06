
/**
 * Generate mock data for displaying purposes
 * @return {[type]} [description]
 */


const mockLists = [
    {
        id: 'advReactions',
        options: [
            { id: 'adv1', name: 'Abdominal Pain' },
            { id: 'adv2', name: 'Anorexia' },
            { id: 'adv3', name: 'Cardiac Arrythmias' },
            { id: 'adv4', name: 'Change in Skin Texture' }
        ]
    },
    {
        id: 'contactType',
        options: [
            { id: 'household', name: 'Household' },
            { id: 'institutional', name: 'Institutional (asylum, shelter, orphanage, etc.)' },
            { id: 'nosocomial', name: 'Nosocomial' }
        ]
    },
    {
        id: 'contactConduct',
        options: [
            { id: 'conduct1', name: 'Guidance/clarification' },
            { id: 'conduct2', name: 'Start TB treatment' },
            { id: 'conduct3', name: 'Start Chemoprophylaxis' },
            { id: 'conduct4', name: 'Other' }
        ]
    },
    {
        id: 'registrationGroup',
        options: [
            { id: 'NEW', name: 'New' },
            { id: 'RELAPSE', name: 'Relapse' },
            { id: 'AFTER_DEFAULT', name: 'After loss to follow-up' },
            { id: 'FAILURE_FT', name: 'After failure of first treatment with first-line drugs' },
            { id: 'FAILURE_RT', name: 'After failure of retreatment with first-line drugs' },
            { id: 'PREVIOUSLY_TREATED', name: 'Previously treated' },
            { id: 'TREATMENT_AFTER_FAILURE', name: 'Treatment after failure' },
            { id: 'TREATMENT_AFTER_LOSS_FOLLOW_UP', name: 'Treatment after loss to follow-up' },
            { id: 'OTHER_PREVIOUSLY_TREATED', name: 'Other previously treated' },
            { id: 'UNKNOWN_PREVIOUS_TB_TREAT', name: 'Unknown previous TB treatment history' },
            { id: 'OTHER', name: 'Other' }
        ]
    },
    {
        id: 'prevTbTreatOutcome',
        options: [
            { id: 'prevOutcome01', name: __('PrevTBTreatmentOutcome.COMPLETED') },
            { id: 'prevOutcome02', name: __('PrevTBTreatmentOutcome.CURED') },
            { id: 'prevOutcome03', name: __('PrevTBTreatmentOutcome.DEFAULTED') },
            { id: 'prevOutcome04', name: __('PrevTBTreatmentOutcome.DIAGNOSTIC_CHANGED') },
            { id: 'prevOutcome05', name: __('PrevTBTreatmentOutcome.FAILURE') },
            { id: 'prevOutcome07', name: __('PrevTBTreatmentOutcome.ONGOING') },
            { id: 'prevOutcome08', name: __('PrevTBTreatmentOutcome.OTHER') },
            { id: 'prevOutcome09', name: __('PrevTBTreatmentOutcome.SCHEME_CHANGED') },
            { id: 'prevOutcome10', name: __('PrevTBTreatmentOutcome.SHIFT_CATIV') },
            { id: 'prevOutcome10', name: __('PrevTBTreatmentOutcome.TRANSFERRED_OUT') },
            { id: 'prevOutcome11', name: __('PrevTBTreatmentOutcome.UNKNOWN') }
        ]
    }
];

export function getOptionName(listId, optionId) {
    if (!listId || !optionId) {
        return null;
    }

    const list = mockLists.find(i => i.id === listId);

    if (list === undefined || list === null) {
        return null;
    }

    const option = list.options.find(i => i.id === optionId);

    return option ? option.name : optionId;
}

export function getOptionList(listId) {
    if (!listId) {
        return null;
    }

    const list = mockLists.find(i => i.id === listId);

    return list ? list.options : null;
}
