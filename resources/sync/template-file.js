// Template file of synchronization data
export default
{
    // the version to use as reference when asking for changes
    version: 12345,
    // data of the workspace to restore in the client
    workspace: {
        id: '123123123',
        name: 'Workspace name',
        patientNameComposition: 'xxx',
        caseValidationTB: 'xxx',
        caseValidationDRTB: 'xxx',
        caseValidationNTM: 'xxx',
        suspectCaseNumber: 'xxx',
        confirmedCaseNumber: 'xxx',
        monthsToAlertExpiredMedicines: 9,
        minStockOnHand: 3,
        maxStockOnHand: 12
    },
    // the configuration of the server, to be replicated to the client
    config: {
        serverURL: 'string',
        adminMail: 'string',
        updateSite: 'string',
        ulaActive: true
    },
    // an array of objects containing the table and its data to execute
    tables: [
        {
            // the name of the table to execute the operations
            table: 'The name of the table',
            // the action to execute in the client: UPDATE or INSERT
            action: 'INSERT or UPDATE',
            // array of objects. Each object is a record. Each object property is a field
            records: [
                { field1: 'val1', field2: 'val2', field3: 'val3' }
            ],
            // a list of record IDs that were deleted in the server side
            deleted: [ 'id1', 'id2', 'id3' ]
        }
    ]
};
