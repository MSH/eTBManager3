'use strict';

import Http from '../commons/http.js';


exports.init = function(path, done) {

    require.ensure('./routes.jsx', function(require) {
        Http.post('/api/sys/session', (err, res) => {
            if (err) {
                return done();
            }

            var Routes = require('./routes.jsx');
            done(Routes);
        });
    });
};