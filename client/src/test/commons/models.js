var session = require('../commons/session').createAdminSession();
var assert = require('assert');

describe.only('Custom models', () => {

    it('get all', () => {
        return session.get('/api/models')
        .then(res => {
            const data = res.body;
            assert(data.length > 0);
        });
    });

    it('get model', () => {
        return session.get('/api/models/prevtbtreatment')
        .then(res => {
            const data = res.body;
            assert('version' in data);
            console.log(data);
        });
    });
});
