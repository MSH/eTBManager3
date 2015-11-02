/**
 * Module to support standard CRUD operations with easy
 */
'use strict';


var agent = require('./agent'),
	assert = require('assert'),
	_ = require('underscore'),
	Promise = require('bluebird');

module.exports = function(tbl) {
	return new CRUD(tbl);
};

function CRUD(tbl) {

	/**
	 * Create a new entity
	 * @param  {[type]} req [description]
	 * @return {[type]}     [description]
	 */
	this.create = function(req, opt) {
		if (_.isArray(req)) {
			var crud = this;
			var lst = req.map(function(item) {
				return crud.create(item);
			});
			return Promise.all(lst);
		}
		else {
			return agent.post('/api/tbl/' + tbl, req)
				.then(function(res) {
					var data = res.body;
					assert(data);

					// don't execute generic validation ?
					if (opt && opt.skipValidation) {
						return data;
					}

					if (!data.success) {
						console.log(data.errors);
					}
					assert(data.success);
					assert(data.result);
					req.id = data.result;
					return data.result;
				});
		}
	};

	/**
	 * Find one entity by its ID
	 * @param  {[type]} id [description]
	 * @return {[type]}    [description]
	 */
	this.findOne = function(id) {
		return agent.get('/api/tbl/' + tbl + '/' + id)
			.then(function(res) {
				var data = res.body;
				assert(data);
				assert(data.success);
				assert(data.result);
				return data.result;
			});
	};


	/**
	 * Update an entity by its id and the data to be updated
	 * @param  {[type]} id   [description]
	 * @param  {[type]} data [description]
	 * @return {[type]}      [description]
	 */
	this.update = function(id, data, opt) {
		return agent.post('/api/tbl/' + tbl + '/' + id, data)
			.then(function(res) {
				var data = res.body;

				assert(data);
				if (opt && opt.skipValidation) {
					return data;
				}

				if (!data.success) {
					console.log(data.errors);
				}
				assert(data.success);
				assert(data.result);
				return data.result;
			});
	};

	/**
	 * Delete an entity by its ID
	 * @param  {[type]} id [description]
	 * @return {[type]}    [description]
	 */
	this.delete = function(id) {
		if (_.isArray(id)) {
			var crud = this;
			var lst = id.map(function(item) {
				return crud.delete(item);
			});
			return Promise.all(lst);
		}
		else {
			if (_.isObject(id)) {
				id = id.id;
			}
			return agent.delete('/api/tbl/' + tbl + '/' + id)
				.then(function(res) {
					var data = res.body;
					assert(data);
					assert(data.success);
					assert(data.result);
				});
		}
	};

	/**
	 * Find for many items
	 * @param  {[type]} qry [description]
	 * @return {[type]}     [description]
	 */
	this.findMany = function(qry) {
		return agent.post('/api/tbl/' + tbl + '/query', qry)
			.then(function(res) {
				var data = res.body;
				assert(data);
				assert(data.list);
				assert(data.count);
				return data;
			});
	};
}