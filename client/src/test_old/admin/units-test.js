
var assert = require('assert'),
    u = require('../common/uniquename'),
    crud = require('../common/crud')('unit'),
    clone = require('clone');


describe('units', function() {
    this.timeout(50000);

    // the unit being tested
    var unit,
        admunits,
        model;


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
        for (var i = 1; i <= 6; i++) {
            var lab = {
                type: 'LAB',
                name: u('Laboratory 1'),
                active: true,
                receiveFrommanufacturer: true,
                performCulture: i <= 5,
                performDst: i <= 3,
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
                for (var i = 1; i <= 6; i++) {
                    var data = {
                        type: 'TBUNIT',
                        name: u('Pharmacy'),
                        active: true,
                        receiveFrommanufacturer: true,
                        tbFacility: i <= 4,
                        mdrFacility: i >= 4,
                        ntmFacility: i <= 2,
                        notificationUnit: i <= 5,
                        patientDispensing: i >= 5,
                        numDaysOrder: 90,
                        address: {
                            address: 'Street test',
                            zipCode: '6001-023',
                            adminUnitId: admunits[0].children[i - 1].data.id
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
                assert(res.address.adminUnit);
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
                assert.equal(res.address.adminUnit.p0.id, unit.address.adminUnitId);
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
            // 1 Pharmacy + 6 health facilities + 6 laboratories
            assert.equal(res.count, 13);
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
                assert.equal(Object.keys(item).length, 5);
                assert(item.type);
                assert(item.id);
                assert(item.name);
                assert('adminUnit' in item);
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
                assert(item.address.adminUnit);
                assert(item.address.adminUnit.p0);
                assert(item.shipAddress);
                assert('receiveFromManufacturer' in item);
                assert('performCulture' in item);
                assert('performMicroscopy' in item);
                assert('performDst' in item);
                assert('performXpert' in item);
            });
        });
    });

     /** Test updating just a set of fields */
     it('# Updating specific fields', function() {
        var unit = model[1];
        var newname;
        var customId = 'MY ID';

        return crud.findOne(unit.id)
        .then(doc => {
            assert(doc);

            unit = doc;
            newname = unit.name + ' v2';

            return crud.update(unit.id, { name: newname, customId: customId});
        })
        .then(res => {
            assert(res);
            assert.equal(res, unit.id);

            return crud.findOne(unit.id);
        })
        .then(doc => {
            assert.equal(doc.name, newname);
            assert.equal(doc.type, unit.type);
            assert.equal(doc.customId, customId);
            assert(doc.address);
            assert.equal(doc.address.address, unit.address.address);
            assert.equal(doc.address.zipCode, unit.address.zipCode);
        });
     });

     /** Testing disable unit and its effect on the listing */
     it('# Disable/enable', () => {
        var count;
        var id;

        var qry = {
            type: 'LAB'
        };

        // count number of laboratories
        return crud.findMany(qry)
        .then(res => {
            assert(res.count);
            assert(res.count > 1);
            count = res.count;

            id = res.list[0].id;
            // disable one of the laboratories
            return crud.update(id, {active: false});
        })
        .then(() => {
            // check the count again
            return crud.findMany(qry);
        })
        .then(res => {
            // make sure one of the items are not available anymore
            assert.equal(res.count, count - 1);

            // active the item again
            return crud.update(id, { active: true});
        });
     });
});
