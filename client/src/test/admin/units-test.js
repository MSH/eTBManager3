'use strict';

var assert = require('assert'),
	shortid = require('shortid'),
	crud = require('../common/crud');


function u(name) {
	return name + ' ' + shortid.generate();
}

describe('units', function() {
//	this.timeout(500000);

	var cs = [
		{ name: u('Region'), level: 1},
		{ name: u('City'), level: 2}
	];

	var aus = [
		{ name: u('Virginia')},
		{ name: u('Arlington')}
	];

	var model = [
		{
			type: 'TBUNIT',
			name: 'Unit ' + shortid.generate(),
			active: true,
			receiveFrommanufacturer: true,
			tbUnit: true,
			mdrUnit: true,
			ntmUnit: false,
			notificationUnit: true,
			patientDispensing: false,
			numDaysOrder: 90,
			address: {
				address: 'Street test',
				zipCode: '6001-023'
			}
		}
	];

	var crudUnit = crud('unit'),
		crudCs = crud('countrystructure'),
		crudAu = crud('adminunit');



	it('# create', function() {
		// create the country structures
		return crudCs.create(cs)
			.then(function(res) {
				assert.equal(res.length, 2);
				aus[0].csId = res[0];
				aus[1].csId = res[1];

				// create the parent admin unit
				return crudAu.create(aus[0]);
			})
			.then(function (res) {
				assert(res);
				aus[1].parentId = res;

				// create the child admin unit
				return crudAu.create(aus[1]);
			})
			.then(function (res) {
				var auid = res;

				model[0].address.adminUnitId = auid;
				// create the units
				return crudUnit.create(model);
			})
			.then(function(res) {
				assert(res);
				assert.equal(res, model[0].id);
			});
	});


	it('# find one', function() {
		return crudUnit.findOne(model[0].id)
			.then(function(res) {
				assert(res.id);
				assert(res.name);
				assert(res.address);
				assert(res.address.adminUnitId);
				assert(res.address.adminUnitName);
				// specific properties of the TB unit
				assert.equal('tbUnit' in res, true);
				assert.equal('mdrUnit' in res, true);
				assert.equal('ntmUnit' in res, true);
				// laboratory porperties that doesn't belong to obj
				assert.equal('performCulture' in res, false);
				assert.equal('performDst' in res, false);
				assert.equal('performXpert' in res, false);
				assert.equal('performMicroscopy' in res, false);
			});
	});

	/**
	 * Delete all information created
	 */
	it('# clean up', function() {
		return crudCs.delete(cs);
	});
});