
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

    return option ? option.name : null;
}
