
var session = require('../commons/session').createAdminSession();
var assert = require('assert');
var crudTest = require('../commons/crud-test');
var u = require('../commons/uniquename');


describe('Administrative unit', function() {

    var adminUnit = {
        name: u('Region XYZ')
    };

    it('gather data', function() {
        return session.post('/api/tbl/countrystructure/query')
        .then(res => {
            assert(res.body);
            const cslist = res.body.list;
            assert(cslist);
            assert(cslist.length > 0);
            return cslist;
        })
        .then(cslist => {
            adminUnit.countryStructure = cslist.find(item => item.level === 1).id;
        });
    });

    /** Standard test for CRUD operations */
    crudTest({
        /** the API name */
        name: 'adminunit',
        /** The document to test */
        doc: adminUnit,
        /** what to test during update */
        update: {
            fields: ['name']
        }
        /** List of required fields to test */
//        requiredFields: ['name', 'csId']
    });

    // var id;

    // it('create', function() {
    //     return session.post('/api/tbl/countrystructure/query')
    //     .then(res => {
    //         assert(res.body);
    //         const cslist = res.body.list;
    //         assert(cslist);
    //         assert(cslist.length > 0);
    //         return cslist;
    //     })
    //     .then(cslist => {
    //         const data = {
    //             name: 'Region XYZ',
    //             csId: cslist[0].id
    //         };

    //         return session.post('/api/tbl/adminunit', data)
    //         .then(res2 => {
    //             id = res2.body.result;
    //             assert(id);
    //         });
    //     });
    // });


    // it('delete', function() {
    //     return session.delete('/api/tbl/adminunit/' + id)
    //     .then(res => console.log(res.body));
    // });
});
