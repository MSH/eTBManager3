/**
 * Module to support standard CRUD operations with easy
 */


var assert = require('assert');
var Session = require('./session');
var _ = require('underscore');

module.exports = function(tbl) {
    return new Crud(tbl);
};

class Crud {

    constructor(table, session) {
        this.table = table;
        this.session = session ? session : Session.createAdminSession();
    }

    /**
     * Create a new entity
     * @param  {object} req the request to create the entity
     * @return {object}     options on how to behave
     */
    create(req, opt) {
        if (_.isArray(req)) {
            var self = this;
            var lst = req.map(function(item) {
                return self.create(item);
            });
            return Promise.all(lst);
        }

        return this.session.post('/api/tbl/' + this.table, req, prepareAgentOptions(opt))
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

    /**
     * Find one entity by its ID
     * @param  {[type]} id [description]
     * @return {[type]}    [description]
     */
    findOne(id, opt) {
        return this.session.get('/api/tbl/' + this.table + '/' + id, prepareAgentOptions(opt))
            .then(function(res) {
                var data = res.body;
                if (opt && opt.expectNotFound) {
                    return null;
                }

                assert(data);
                return data;
            });
    }


    /**
     * Update an entity by its id and the data to be updated
     * @param  {[type]} id   [description]
     * @param  {[type]} data [description]
     * @return {[type]}      [description]
     */
    update(id, data, opt) {
        return this.session.post('/api/tbl/' + this.table + '/' + id, data)
            .then(function(res) {
                var result = res.body;

                assert(result, 'No data returned from update operation');
                if (opt && opt.skipValidation) {
                    return result;
                }

                if (!result.success) {
                    console.log(result.errors);
                }
                assert(result.success);
                assert(result.result);
                return result.result;
            });
    }

    /**
     * Delete an entity by its ID
     * @param  {[type]} id [description]
     * @return {[type]}    [description]
     */
    delete(id, opt) {
        var self = this;
        if (_.isArray(id)) {
            var lst = id.map(function(item) {
                return self.delete(item.id);
            });
            return Promise.all(lst);
        }

        if (_.isObject(id)) {
            id = id.id;
        }

        if (!id) {
            throw new Error('No ID informed to be delete');
        }

        return this.session.delete('/api/tbl/' + this.table + '/' + id, prepareAgentOptions(opt))
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
                    return self.findOne(id, { expectNotFound: true });
                }
                return data.result;
            });
    }

    /**
     * Find for many items
     * @param  {[type]} qry [description]
     * @return {[type]}     [description]
     */
    findMany(qry) {
        return this.session.post('/api/tbl/' + this.table + '/query', qry)
            .then(function(res) {
                var data = res.body;
                assert(data);
                if (data.errors) {
                    console.log(data.errors);
                }
                assert(!data.errors);
                assert(data.count, 'No record found');
                if (!qry.countOnly) {
                    assert(data.list);
                }
                return data;
            });
    }
}

function prepareAgentOptions(opt) {
    if (!opt) {
        return null;
    }

    var res = {};
    if (opt.expectNotFound) {
        res.expect = 404;
    }
    return res;
}
