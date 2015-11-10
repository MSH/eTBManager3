'use strict';

var assert = require('assert'),
	u = require('../common/uniquename'),
	crud = require('../common/crud')('unit'),
	clone = require('clone');



describe('units', function() {
	this.timeout(500000);

	// the unit being tested
	var unit;

	var	admunits;

	var model;


	/**
	 * Create the model
	 */
	it('# create', function() {
		admunits = require('./adminunit-test').model;

		model = exports.model = [
			// PHARMACY. The other units will be created automatically
			{
				type: 'TBUNIT',
				name: u('Pharmacy'),
				active: true,
				receiveFrommanufacturer: true,
				tbFacility: false,
				mdrFacility: false,
				ntmFacility: false,
				notificationUnit: false,
				patientDispensing: false,
				numDaysOrder: 90,
				address: {
					address: 'Street test',
					zipCode: '6001-023',
					adminUnitId: admunits[0].data.id
				}
			}
		];

		// create laboratories
		for (var i = 1; i <= 10; i++) {
			var lab = {
				type: 'LAB',
				name: u('Laboratory 1'),
				active: true,
				receiveFrommanufacturer: true,
				performCulture: i <= 8,
				performDst: i <=3,
				performXpert: i >= 3,
				performMicroscopy: true,
				address: {
					address: 'Street test',
					zipCode: '6001-023',
					adminUnitId: admunits[0].children[i - 1].data.id
					}
			};
			model.push(lab);
		}

		// creating model
		return crud.create(model)
			.then(function(res) {
				// create other units that receive medicines from the pharmacy
				assert(res);

				// create health facilities
				exports.healthUnits = [];
				var proms = [];
				for (var i = 1; i <= 10; i++) {
					var data = {
						type: 'TBUNIT',
						name: u('Pharmacy'),
						active: true,
						receiveFrommanufacturer: true,
						tbFacility: i <= 7,
						mdrFacility: i >=7,
						ntmFacility: i <= 2,
						notificationUnit: i <= 5,
						patientDispensing: i >= 5,
						numDaysOrder: 90,
						address: {
							address: 'Street test',
							zipCode: '6001-023',
							adminUnitId: admunits[0].children[i].data.id
						},
						supplierId: model[0].id    // set the supplier as being the pharmacy
					};

					model.push(data);
					exports.healthUnits.push(data);
					proms.push(crud.create(data));
				}

				return Promise.all(proms);
			})
			.then(function() {
				// create unit for testing
				unit = clone(model[0]);
				unit.name = u('Pharmacy test');
				return crud.create(unit);
			})
			.then(function() {
				assert(unit.id);
			});
	});


	it('# find one TB unit', function() {
		return crud.findOne(unit.id)
			.then(function(res) {
				assert(res.id);
				assert(res.name);
				assert(res.address);
				assert(res.address.adminUnitId);
				assert(res.address.adminUnitName);
				// specific properties of the TB unit
				assert.equal('tbFacility' in res, true);
				assert.equal('mdrFacility' in res, true);
				assert.equal('ntmFacility' in res, true);
				// laboratory porperties that doesn't belong to obj
				assert.equal('performCulture' in res, false);
				assert.equal('performDst' in res, false);
				assert.equal('performXpert' in res, false);
				assert.equal('performMicroscopy' in res, false);

				assert.equal(res.name, unit.name);
				assert.equal(res.id, unit.id);
				assert.equal(res.address.adminUnitId, unit.address.adminUnitId);
			});
	});


	/**
	 * Delete all information created
	 */
	it('# delete', function() {
		if (unit) {
			return crud.delete(unit.id);
		}
	});

	it('# query all from admin unit', function() {
		var qry = {
			adminUnitId: admunits[0].data.id,
			includeSubunits: true
		};

		return crud.findMany(qry)
		.then(function(res) {
			// 1 Pharmacy + 10 health facilities + 10 laboratories
			assert.equal(res.count, 21);
		});
	});


	/**
	 * Check different profile data
	 */
	it('# profiles', function() {
		var qry = {
			profile: 'item',
			rpp: 2,
			page: 0
		};

		return crud.findMany(qry)
		.then(function(res) {
			assert.equal(res.list.length, 2);
			res.list.forEach(function(item) {
				assert.equal(Object.keys(item).length, 3);
				assert(item.type);
				assert(item.id);
				assert(item.name);
			});

			qry.profile = 'default';
			return crud.findMany(qry);
		})
		.then(function(res) {
			// check default return
			assert.equal(res.list.length, 2);
			res.list.forEach(function(item) {
				assert.equal(Object.keys(item).length, 6);
				assert(item.type);
				assert(item.id);
				assert(item.name);
				assert('adminUnitId' in item);
				assert('adminUnitName' in item);
			});

			qry.profile = 'detailed';
			qry.type = 'LAB';
			return crud.findMany(qry);
		})
		.then(function(res) {
			// check default return
			assert.equal(res.list.length, 2);
			res.list.forEach(function(item) {
				assert(item.type);
				assert(item.id);
				assert(item.name);
				assert('customId' in item);
				assert('active' in item);
				assert(item.address);
				assert(item.address.adminUnitId);
				assert(item.address.adminUnitName);
				assert(item.shipAddress);
				assert('receiveFromManufacturer' in item);
				assert('performCulture' in item);
				assert('performMicroscopy' in item);
				assert('performDst' in item);
				assert('performXpert' in item);
			});
		});
	});

	 // disable units
	 it('# Test disabled', function() {
	 	 var unit = model[1];

	 	 return crud.update(unit.id, { name: unit.name + ' v1'})
         .then(res => {
                 assert(res.id);
                 assert.equal(res.id, unit.id)
             });
	 });
});