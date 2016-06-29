var request = require('supertest');

var host,
    adminAuthToken;

class ClientSession {

    constructor(options) {
        this.options = options || {};
    }

    /**
     * Send a GET request to the server
     * @param  {[type]} url [description]
     * @return {[type]}     [description]
     */
    get(url) {
        var req = agent().get(url);

        prepareReq(req, this.options);

        return requestInPromise(req);
    }

    /**
     * Send a POST request to the server
     * @param  {[type]} url  [description]
     * @param  {[type]} data [description]
     * @return {[type]}      [description]
     */
    post(url, data) {
        var req = agent()
            .post(url)
            .send(data || {})
            .expect('Content-Type', /json/);

        prepareReq(req, this.options);

        return requestInPromise(req);
    }

    /**
     * Send a DELETE request to the server
     * @param  {[type]} url [description]
     * @return {[type]}     [description]
     */
    delete(url) {
        var req = agent()
            .delete(url)
            .expect('Content-Type', /json/);

        prepareReq(req, this.options);

        return requestInPromise(req);
    }
}


/**
 * Create administrator session
 * @return {ClientSession} Instance of the ClientSession class
 */
exports.createAdminSession = function() {
    return new ClientSession({ administrator: true });
};

/**
 * Create an anonymous session, i.e, no authentication information will be sent to the server
 * @return {ClientSession} Instance of the ClientSession
 */
exports.createAnonymousSession = function() {
    return new ClientSession();
};

/**
 * Create an instance of the ClientSession
 * @param  {[type]} options [description]
 * @return {[type]}         [description]
 */
exports.create = function(options) {
    return new ClientSession(options);
};

exports.setAdminAuthToken = function(authtoken) {
    adminAuthToken = authtoken;
};

var agent = exports.agent = function() {
    if (!host) {
        throw new Error('Host was not set');
    }
    return request.agent(host);
};

exports.setHost = function(_host) {
    host = _host;
};


function requestInPromise(req) {
    return new Promise((resolve, reject) => {
        req.end((err, res) => {
            if (err) {
                console.log(err);
                reject(err);
            }
            else {
                resolve(res);
            }
        });
    });
}


function prepareReq(req, options) {
    if (options.authToken) {
        req.set('X-Auth-Token', this.options.authToken);
    }

    if (options.administrator) {
        req.set('X-Auth-Token', adminAuthToken);
    }
}
