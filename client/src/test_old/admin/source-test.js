
var u = require('../common/uniquename'),
    crud = require('../common/crud')('source'),
    crudTest = require('../common/crud-test');


var model = exports.model = [
	{
		name: u('Ministry of Health'),
		shortName: u('MOH')
	},
	{
		name: u('Global Drug Facility'),
		shortName: u('GDF')
	},
	{
		name: u('Green Light Commitee'),
		shortName: u('GLC')
	}
];


describe('source', () => {

	var source = {
		name: u('TEST'),
		shortName: u('TEST')
	};


	/** Standard test for CRUD operations */
	crudTest({
		/** the API name */
		name: 'source',
		/** The document to test */
		doc: source,
		/** the model to create */
		model: model,
		/** what to test during update */
		update: {
			fields: ['name']
		},
		/** List of required fields to test */
		requiredFields: ['name', 'shortName'],
		/** List of unique fields to test */
		uniqueFields: ['name', 'shortName']
	});

});


exports.cleanup = function() {
	console.log('deleting sources...');
	return crud.delete(exports.model.id);
};