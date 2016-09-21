/**
 * Execute a standard CRUD test
 */

'use strict';

var assert = require('assert'),
    Promise = require('bluebird'),
    u = require('./uniquename');


module.exports = function execute(opt) {

    assert(opt.name, 'opt.name -> No name to use in API calls');
    assert(opt.doc, 'opt.doc -> No document to test');

    var crud = require('../common/crud')(opt.name);

    var doc = opt.doc;

    // create test
    it('# create', () => {
        var prom;
        if (opt.model) {
            var lst = [...opt.model, doc];
            prom = crud.create(lst);
        }
        else {
            prom = crud.create(doc);
        }

        return prom.then(res => {
            if (opt.afterCreate) {
                return opt.afterCreate(res);
            }
        });
    });


    it('# find one', () => {
        return crud.findOne(doc.id)
        .then(res => {
            assert.equal(res.id, doc.id);
            for (var key in doc) {
                assert(res[key], doc[key]);
            }
        });
    });


    if (opt.requiredFields) {
        it('# required fields', () => {
            return crud.create({}, {skipValidation: true})
            .then(res => {
                assert(res.errors);
                assert.equal(Object.keys(res.errors).length, opt.requiredFields.length);
                for (var field in res.errors) {
                    var err = res.errors[field];
                    assert.equal(err.code, 'NotNull');
                    assert(opt.requiredFields.indexOf(field) >= 0);
                }
            });
        });
    }


    if (opt.uniqueFields) {
        if (!opt.model) {
            console.log('skip unique fields. model is required');
            return;
        }

        it('# unique', () => {
            var proms = opt.uniqueFields.map(group => {
                var doc2 = opt.model[0];
                var fields = group.split(',');

                // create object with another existing record
                var data = {};
                for (var k in doc) {
                    data[k] = fields.indexOf(k) >= 0? doc2[k]: doc[k];
                }

                return crud.update(doc.id, data, { skipValidation: true})
                .then(res => {
                    assert(res.errors, 'No errors found from unique test');
                    var keys = Object.keys(res.errors);
                    assert.equal(keys.length, 1);
                    assert.equal(res.errors[keys[0]].code, 'NotUnique');
                });
            });

            return Promise.all(proms);
        });
    }


    // update test
    if (opt.update) {
        it('# update', () => {
            // set of fields to be updated and its value
            var doc = opt.doc;

            if (opt.update.set) {
                var set = opt.update.set;

                return crud.update(doc.id, set)
                .then(() => {
                    return crud.findOne(doc.id);
                })
                .then(res => {
                    assert.equal(res.id, doc.id);
                    for (var key in set) {
                        assert.equal(res[key], set[key]);
                    }
                });
            }

            // set of fields to be automatically updated
            if (opt.update.fields) {
                // generate object to be updated
                var data = {};
                opt.update.fields.forEach(field => {
                    var s = doc[field];
                    s = s.substring(0, s.length - 10);
                    data[field] = u(s);
                });

                return crud.update(doc.id, data)
                .then(() => crud.findOne(doc.id))
                .then(res => {
                    assert.equal(res.id, doc.id);
                    for (var key in data) {
                        assert.equal(res[key], data[key]);
                    }
                });
            }
        });
    }



    // minimal number of records to test paging
    it('# paging', () => {
        var count = 0;

        return crud.findMany({ countOnly: true})
        .then(res => {
            if (opt.model) {
                assert(res.count > opt.model.length, 'findMany returned less records than in the model');
            }
            else {
                assert(res.count > 0, 'No record found from findMany call');
            }

            count = res.count;

            if (count <= 2) {
                console.log('skip paging test... Not enough records: ' + count);
                return;
            }

            var qry = {
                rpp: 2,
                page: 0
            };

            return crud.findMany(qry)
            .then(res => {
                assert.equal(res.count, count);
                assert.equal(res.list.length, 2);

                qry.page = 1;
                return crud.findMany(qry);
            })
            .then(res => {
                assert.equal(res.count, count);
                assert(res.list.length >= 1, 'findMany returned no record from next page');
            });
        });
    });


    // test if descengin and ascending is working properly
    it('# order by', () => {
        var qry = {
            rpp: 5,
            page: 0,
            descending: false
        },
        item, count;

        if (opt.orderBy) {
            qry.orderBy = opt.orderBy;
        }

        return crud.findMany(qry)
        .then(res => {
            assert(res.count > 0);
            count = res.count;

            item = res.list[0];

            // calculate the last page
            var lp = res.count / 5;
            if (res.count % 5 === 0) {
                lp--;
            }
            else {
                lp = Math.floor(lp);
            }

            qry.page = lp;
            qry.descending = true;

            return crud.findMany(qry);
        })
        .then(res => {
            assert.equal(res.count, count);
            var it2 = res.list[res.list.length - 1];
            if (!it2) {
                console.log('it2 is undefined');
                console.log('QUERY = ', qry);
                console.log('RESULT = ', res);
            }
            if (!item) {
                console.log('item is undefined');
            }
            assert.equal(it2.id, item.id);
        });
    });


    // test the profiles
    if (opt.profiles) {
        it('# profiles', () => {
            var profs = Object.keys(opt.profiles);
            var prom = profs.map(prof => {

                var qry = {
                    profile: prof,
                    page: 0,
                    rpp: 5
                };

                if (opt.baseQuery) {
                    qry = Object.assign(qry, opt.baseQuery);
                    // for (var k in opt.baseQuery) {
                    //     qry[k] = opt.baseQuery[k];
                    // }
                }

                return crud.findMany(qry)
                .then(res => {
                    assert(res.list.length > 0, 'profile ' + prof + ': No record found');
                    res.list.forEach(it => {
                        opt.profiles[prof]
                            .forEach( p => assert(p in it, 'profile ' + prof + ': Property not found ' + p) );
                    });
                });
            });

            return Promise.all(prom);
        });
    }


    // delete test
    if (opt.doc) {
        var del = opt.delete;

        // any test suite to be called before delete?
        if (del && del.beforeDelete) {
            del.beforeDelete();
        }

        it('# delete', () => {
            return crud.delete(opt.doc.id, { testDeleted: true });
        });
    }
};