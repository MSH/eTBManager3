'use strict';


var agent = require('./common/agent'),
    assert = require('assert');


/**
 * Called before execution of the tests. Prepare the environment
 */
before(function() {

    // get information about the system
    return agent.get('/api/sys/info')
        .then(function(res) {
            assert(res.body);

            var data = res.body;
            assert(data.state);
            assert(data.languages);
            assert(data.system);

            var sys = data.system;
            assert(sys.implementationVersion);
            assert(sys.buildTime);
            assert(sys.buildNumber);

            assert.notEqual(data.ulaActive, undefined);
            assert.notEqual(data.allowRegPage, undefined);

            return data.state;
        })
        // initialize workspace
        .then(function(state) {
            if (state !== 'NEW') {
                return state;
            }

            console.log('Registering workspace...');

            var req = {
                workspaceName: 'Test',
                adminPassword: 'pwd123',
                adminEmail: 'rmemoria@gmail.com'
            };

            // register the workspace
            return agent.post('/api/init/workspace', req)
                .then(function(res) {
                    assert(res.body);
                    console.log('  workspace -> ' + res.body);
                    return state;
                });
        })
        // login into the system
        .then( function() {
            var req = {
                username: 'admin',
                password: 'pwd123'
            };

            return agent.post('/api/auth/login', req)
                .then( function (res) {
                    var data = res.body;

                    assert.equal(data.success, true);
                    assert(data.authToken);
                    console.log('  authToken -> ' + data.authToken);

                    agent.authToken = data.authToken;
                });
        });

});


