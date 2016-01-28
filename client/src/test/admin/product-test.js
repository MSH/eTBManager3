'use strict';

var assert = require('assert'),
    u = require('../common/uniquename'),
    crud = require('../common/crud')('product'),
    crudTest = require('../common/crud-test');


var medicines = exports.medicines = [
	{
		type: 'MEDICINE',
		name: u('Terizidon 250ml capsule'),
		shortName: u('T 250ml'),
		category: 'INJECTABLE',
		line: 'FIRST_LINE'
	},
	{
		type: 'MEDICINE',
		name: u('Isoniazid 250ml'),
		shortName: u('H 250ml'),
		category: 'INJECTABLE',
		line: 'FIRST_LINE'
	},
	{
		type: 'MEDICINE',
		name: u('Ethambutol 250ml capsule'),
		shortName: u('Eth 250ml'),
		category: 'INJECTABLE',
		line: 'SECOND_LINE'
	}
];

var products = exports.products = [
	{
		name: u('Destilable water'),
		shortName: u('Water')
	},
	{
		name: u('Cat I patient kit'),
		shortName: u('CAT I KIT')
	},
	{
		name: u('Cat II patient kit'),
		shortName: u('CAT II KIT')
	},
	{
		name: u('Cat III patient kit'),
		shortName: u('CAT III KIT')
	}
];


/**
 * TEST MEDICINES
 */
describe('medicines', () => {

	var medicine = {
		type: 'MEDICINE',
		name: u('Amikacin 500ml'),
		shortName: u('Am 500ml'),
		category: 'INJECTABLE',
		line: 'FIRST_LINE'
	};

	crudTest({
		/** the API name */
		name: 'product',
		/** The document to test */
		doc: medicine,
		/** the model to create */
		model: medicines,
		/** List of fields by profile */
		profiles: {
			item: ['id', 'name', 'type'],
			default: ['id', 'type', 'name', 'shortName', 'active', 'line', 'category'],
			detailed: ['id', 'type', 'name', 'shortName', 'active', 'line', 'category', 'substances']
		},
		/** The base attributes to be included in every query */
		baseQuery: {
			type: 'MEDICINE'
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


/**
 * TEST PRODUCTS
 */
describe('product', () => {

	var prod = {
		type: 'PRODUCT',
		name: u('Water 1500ml'),
		shortName: u('Water 1500ml')
	};


	/** Standard test for CRUD operations */
	crudTest({
		/** the API name */
		name: 'product',
		/** The document to test */
		doc: prod,
		/** the model to create */
		model: products,
		/** List of fields by profile */
		profiles: {
			item: ['id', 'name', 'type'],
			default: ['id', 'type', 'name', 'shortName', 'active']
		},
		/** The base attributes to be included in every query */
		baseQuery: {
			type: 'PRODUCT'
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
	console.log('deleting products...');
	return crud.delete(exports.medicines)
		.then( () => crud.delete(exports.products));
};
