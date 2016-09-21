'use strict';

var assert = require('assert'),
    u = require('../common/uniquename'),
    crud = require('../common/crud')('countrystructure'),
    crudTest = require('../common/crud-test');


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
        },
        {
            name: u('Region2'),
            level: 1
        },
        {
            name: u('City2'),
            level: 2
        },
        {
            name: u('Municipality2'),
            level: 3
        }
    ];

    var cs = {
        name: u('Test'),
        level: 3
    };


    /** Standard test for CRUD operations */
    crudTest({
        /** the API name */
        name: 'countrystructure',
        /** The document to test */
        doc: cs,
        /** the model to create */
        model: model,
        /** List of required fields to test */
        requiredFields: ['name', 'level'],
        /** List of unique fields to test */
        uniqueFields: ['name,level'],
        /** what to test during update */
        update: {
            set: {
                name: cs.name + ' v2',
                level: 2
            }
        },
        delete: {
            beforeDelete: extraTests
        }
    });


    function extraTests() {
        /**
         * Try to update a country structure with an invalid level
         */
        it('# invalid level', function() {
            // get Municipality
            var data = {
                    name: cs.name + ' v3',
                    level: 6
                };

            return crud.update(cs.id, data, { skipValidation: true })
            .then(function(res) {
                assert(res.errors);
                var fields = Object.keys(res.errors);
                assert.equal(fields.length, 1);
                assert.equal(fields[0], 'level');
            });
        });


        /**
         * Search for itens
         */
        it('# find many from level', function() {
            return crud.findMany({ level: 2 })
            .then(function(res) {
                assert(res.list.length >= 2);
            });
        });

    }


});


/**
 * Delete all country structure, and consequently, delete in cascade all test data
 */
exports.cleanup = function() {
    console.log('deleting country structures...');
    return crud.delete(exports.model);
};