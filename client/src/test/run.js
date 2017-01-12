var Session = require('./commons/session');


var HOST = 'http://localhost:8080';

/**
 * Init the test execution
 */
Session.setHost(HOST);

require('./commons/init');
require('./admin/adminunit-test');
require('./commons/models');
