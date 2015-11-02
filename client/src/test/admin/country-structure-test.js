'use strict';

var assert = require('assert'),
	u = require('../common/uniquename'),
	crud = require('../common/crud')('countrystructure');



describe('country-structure', function() {

	var model = exports.model = [
		{
			name: u('Region'),
			level: 1
		},
		{
			name: u('City'),
			level: 2
		},
		{
			name: u('Municipality'),
			level: 3
		}
	];

	var cs = {
		name: u('Test'),
		level: 3
	};


	/**
	 * Create the country structures
	 */
	it('# create', function() {
		return crud.create(model)
			.then(function() {
				return crud.create(cs);
			});
	});


	/**
	 * Update the country structure
	 */
	it('# update', function() {
		// get Municipality
		var data = {
				name: cs.name + ' v2',
				level: 2
			};

		return crud.update(cs.id, data)
		.then(function() {
			return crud.findOne(cs.id);
		})
		.then(function(res) {
			assert.equal(res.id, cs.id);
			assert.equal(res.name, data.name);
			assert.equal(res.level, data.level);
		});
	});


	/**
	 * Try to update a country structure with an invalid level
	 */
	it('# invalid level', function() {
		// get Municipality
		var data = {
				name: cs.name + ' v3',
				level: 6
			};

		return crud.update(cs.id, data, {skipValidation: true })
		.then(function(res) {
			assert(res.errors);
			assert.equal(res.errors.length, 1);
			assert.equal(res.errors[0].field, 'level');
		});
	});


	/**
	 * Search for itens
	 */
	it('# find many', function() {
		return crud.findMany({level: 2})
		.then(function(res) {
			assert(res.list.length >= 2);
		});
	});


	/**
	 * Delete country structure
	 */
	it('# delete', function() {
		return crud.delete(cs.id, { testDeleted: true });
	});

});

/**
 * Delete all country structure, and consequently, delete in cascade all test data
 */
exports.cleanup = function() {
	return crud.delete(exports.model);
};