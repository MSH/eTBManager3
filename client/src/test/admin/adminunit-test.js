'use strict';

var agent = require('../agent'),
	assert = require('assert');


describe('admin-unit', function() {

	var csId;
	var auId;
	var csName = 'My Region';
	var cityName = 'Rio de Janeiro';
	var newCityName = 'New York';

	it('# create', function() {
		var data = {
			name: csName,
			level: 1
		};

		// create country structure
		return agent.post('/api/tbl/countrystructure', data)
			.then(function(res) {
				var data = res.body;
				assert(data);
				assert(data.success);
				csId = data.result;
			})
			// create administrative unit
			.then( function() {
				var data = {
					name: cityName,
					csId: csId
				};

				return agent.post('/api/tbl/adminunit', data);
			})
			// result of admin unit created
			.then( function(res) {
				var data = res.body;
				assert(data);
				assert(data.success);
				auId = data.result;
			});
	});


	it('# update', function() {
		var data = {
			name: newCityName,
			csId: csId
		};

		return agent.post('/api/tbl/adminunit/' + auId, data)
			.then( function(res) {
				var data = res.body;
				assert(data);
				assert(data.success);
			});
	});


	it('# get', function() {
		return agent.get('/api/tbl/adminunit/' + auId)
			.then( function(res) {
				var data = res.body;
				assert(data.id);
				assert(data.name);
				assert.equal(data.name, newCityName);
				assert.equal(data.csId, csId);
				assert.equal(data.csName, csName);
			});
	});


	it('# delete', function() {
		return agent.delete('/api/tbl/adminunit/' + auId)
			.then( function(res) {
				var data = res.body;
				assert(data);
				assert(data.success);
				// clean up of country structure
				return agent.delete('/api/tbl/countrystructure/' + csId);
			});
	});
});