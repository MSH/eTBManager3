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
				assert(data.id);
				assert(data.name);
				assert.equal(data.name, name);
				assert(data.level, 1);
			});
	});


	it('# delete', function() {
		return agent.post('/api/tbl/countrystructure/del/' + csId)
			.then( function(res) {
				var data = res.body;
				assert(data);
				assert(data.success);
				assert(data.result);
			});
	});
});
