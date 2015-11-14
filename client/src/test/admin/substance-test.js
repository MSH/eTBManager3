'use strict';

var u = require('../common/uniquename'),
    crud = require('../common/crud')('substance'),
    crudTest = require('../common/crud-test');


var model = exports.model = [
	{
		name: u('Isoniazid'),
		shortName: u('H'),
		line: 'FIRST_LINE',
		displayOrder: 0,
		dstResultForm: true,
		prevTreatmentForm: true
	},
	{
		name: u('Rifampicin'),
		shortName: u('R'),
		line: 'FIRST_LINE',
		displayOrder: 1,
		dstResultForm: true,
		prevTreatmentForm: true
	},
	{
		name: u('Streptomicin'),
		shortName: u('S'),
		line: 'FIRST_LINE',
		displayOrder: 2,
		dstResultForm: true,
		prevTreatmentForm: true
	}
];


describe('substance', () => {

	var sub = {
		name: u('Ethambutol'),
		shortName: u('E'),
		line: 'FIRST_LINE',
		displayOrder: 3,
		dstResultForm: true,
		prevTreatmentForm: true
	};



	/** Standard test for CRUD operations */
	crudTest({
		/** the API name */
		name: 'substance',
		/** The document to test */
		doc: sub,
		/** the model to create */
		model: model,
		/** List of fields by profile */
		profiles: {
			item: ['id', 'name'],
			default: ['id', 'name', 'shortName', 'line', 'prevTreatmentForm', 'dstResultForm', 'displayOrder', 'active']
		},
		/** List of required fields to test */
		requiredFields: ['name', 'shortName'],
		/** List of unique fields to test */
		uniqueFields: ['name', 'shortName'],
		/** what to test during update */
		update: {
			fields: ['name', 'shortName']
		}
	});
});


exports.cleanup = function() {
	console.log('deleting substances...');
	return crud.delete(exports.model);
};
