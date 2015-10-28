'use strict';

var agent = require('../agent'),
	assert = require('assert');


describe('country-structure', function() {

	var csId;
	var name = 'THE REGION';

	it('# create', function() {
		var data = {
			name: 'My Region',
			level: 1
		};

		return agent.post('/api/tbl/countrystructure', data)
			.then(function(res) {
				var data = res.body;
				assert(data);
				assert(data.success);
				assert(data.result);
				csId = data.result;
			});
	});


	it('# update', function() {
		var data = {
			name: name,
			level: 1
		};

		return agent.post('/api/tbl/countrystructure/' + csId, data)
			.then( function(res) {
				var data = res.body;
				assert(data);
				assert(data.success);
				assert(data.result);
			});
	});


	it('# get', function() {
		return agent.get('/api/tbl/countrystructure/' + csId)
			.then( function(res) {
				var data = res.body;
				assert(data.success);
				var obj = data.result;
				assert(obj.id);
				assert(obj.name);
				assert.equal(obj.name, name);
				assert(obj.level, 1);
			});
	});

	it('# get (error)', function() {
		return agent.get('/api/tbl/countrystructure/c0a80168-50b0-13c7-8150-b0578d970105')
			.then( function(res) {
				var data = res.body;
				assert(data);
				assert.equal(data.success, false);
			});
	});


	it('# delete', function() {
		return agent.delete('/api/tbl/countrystructure/' + csId)
			.then( function(res) {
				var data = res.body;
				assert(data);
				assert(data.success);
				assert(data.result);
			});
	});
});
