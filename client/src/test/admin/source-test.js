'use strict';

var assert = require('assert'),
    u = require('../common/uniquename'),
    crud = require('../common/crud')('source');

var model = exports.model = {
	name: u('Ministry of Health'),
	shortName: u('MOH')
};

describe('source', () => {

	var source = {
		name: u('TEST'),
		shortName: u('TEST')
	};

	/**
	 * Create a source
	 */
    it('# create', () => {
    	var data = [
    		model, source
    	];
    	return crud.create(data);
    });

    /**
     * Update the data of the source and check if it was returned
     */
    it('# update', () => {
    	var data = {
    		name: u('New name')
    	};

    	return crud.update(source.id, data)
    	.then(() => {
    		return crud.findOne(source.id);
    	})
    	.then(doc => {
    		assert(doc.name, data.name);
    		assert(doc.shortName, source.shortName);
    	});
    });

    /**
     * Query a list of sources
     */
    it('# query', () => {
	    return crud.findMany({})
	    .then(res => {
	    	assert(res.count >= 2);
	    	assert(res.list.length >= 2);
	    });
    });


    /**
     * Test required fields
     */
    it('# required field', () => {
    	// shortName is a required field
    	var data = {
    		name: u('ANY NAME')
    	};

    	return crud.create(data, {skipValidation: true})
    	.then(res => {
    		assert.equal(res.success, false);
    		assert(res.errors);
    		assert.equal(res.errors.length, 1);

    		var err = res.errors[0];
    		assert.equal(err.field, 'shortName');
    		assert.equal(err.group, 'NotNull');
    	});
    });

    /**
     * Check unique name
     */
    it('# unique name', () => {
    	var data = {
    		name: model.name,
    		shortName: model.shortName
    	};

    	return crud.create(data, {skipValidation: true})
    	.then(res => {
    		assert.equal(res.success, false);
    		assert(res.errors);
    		assert.equal(res.errors.length, 2);

    		var err = res.errors;
    		assert(err[0].field === 'name' || err[0].field === 'shortName');
    		assert(err[1].field === 'name' || err[1].field === 'shortName');

    		assert.equal(err[0].group, 'NotUnique');
    		assert.equal(err[1].group, 'NotUnique');

    		assert(err[0].msg);
    		assert(err[1].msg);
    	});
    });

    /**
     * Delete a source
     */
    it('# delete', () => {
    	return crud.delete(source.id);
   	});
});


exports.cleanup = function() {
	console.log('deleting sources...');
	return crud.delete(exports.model.id);
};