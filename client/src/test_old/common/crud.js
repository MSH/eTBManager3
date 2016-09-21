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
     * @param  {object} req the request to create the entity
     * @return {object}     options on how to behave
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
            return agent.post('/api/tbl/' + tbl, req, prepareAgentOptions(opt))
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
                    assert(data.success, 'Error on the return of create');
                    assert(data.result, 'No result returned from create');
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
    this.findOne = function(id, opt) {
        return agent.get('/api/tbl/' + tbl + '/' + id, prepareAgentOptions(opt))
            .then(function(res) {
                var data = res.body;
                if (opt && opt.expectNotFound) {
                    return;
                }

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

                assert(data, 'No data returned from update operation');
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
    this.delete = function(id, opt) {
        var self = this;
        if (_.isArray(id)) {
            var crud = this;
            var lst = id.map(function(item) {
                return crud.delete(item.id);
            });
            return Promise.all(lst);
        }
        else {
            if (_.isObject(id)) {
                id = id.id;
            }

            if (!id) {
                return;
            }

            return agent.delete('/api/tbl/' + tbl + '/' + id, prepareAgentOptions(opt))
                .then(function(res) {
                    var data = res.body;

                    if (opt && opt.skipValidation) {
                        return data;
                    }
                    assert(data);
                    assert(data.success);
                    assert(data.result);

                    // check if entity was really deleted?
                    if (opt && opt.testDeleted) {
                        return self.findOne(id, {expectNotFound: true});
                    }
                    return data.result;
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
                if  (data.errors) {
                    console.log(data.errors);
                }
                assert(!data.errors);
                assert(data.count, 'No record found');
                if (!qry.countOnly) {
                    assert(data.list);
                }
                return data;
            });
    };
}

function prepareAgentOptions(opt) {
    if (!opt) {
        return;
    }

    var res = {};
    if (opt.expectNotFound) {
        res.expect = 404;
    }
    return res;
}