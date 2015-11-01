'use strict';

var assert = require('assert'),
	uuid = require('node-uuid'),
	Promise = require('bluebird'),
	crud = require('../common/crud')('countrystructure');


describe('country-structure', function() {

	var model = [
		{
			name: 'Region ' + uuid.v1(),
			level: 1
		},
		{
			name: 'City ' + uuid.v1(),
			level: 2
		},
		{
			name: 'Municipality ' + uuid.v1(),
			level: 3
		}
	];


	/**
	 * Create the country structures
	 */
	it('# create', function() {
		return crud.create(model);
	});


	/**
	 * Update the country structure
	 */
	it('# update', function() {
		// get Municipality
		var cs = model[2],
			data = {
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
		var cs = model[2],
			data = {
				name: cs.name + ' v3',
				level: 6
			};

		return crud.update(cs.id, data, {skipValidation:true })
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
		// create a list of promisses
		var lst = model.map(function(item) {
			return crud.delete(item.id);
		});

		return Promise.all(lst);
	});

});
