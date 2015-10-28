'use strict';


var request = require('supertest'),
	Promise = require('bluebird');


/**
 * Agents used during tests - System and workspace
 */
var agent;




/**
 * Agent to get/post to the system URL
 * @type {Object}
 */
agent = module.exports = new StandardAgent('localhost:8080');



/**
 * Standard agent prototype to be used in tests
 * @param {string} url the application URL to test
 */
function StandardAgent(url) {
    this.agent = request.agent( url );
    this.authToken = undefined;

    /**
     * Sends a get to the server that will respond with a json data
     * @param  {[type]}   basepath  the base path of the request over the server
     * @param  {Function} callback 	function called when it's finished
     */
    this.get = function(basepath) {
        var req = this.agent
            .get(basepath)
            .expect(200)
            .expect('Content-Type', /json/);

        // include authentication token
        if (this.authToken) {
        	req.set('X-Auth-Token', this.authToken);
        }

        return new Promise( function (resolve, reject) {
        	req.end(function(err, res) {
        		if (err) {
        			reject(err);
        		}
        		else {
        			resolve(res);
        		}
        	});
        });
    };

    /**
     * Post a JSON request to the server using the given base path
     * @param  {string}   basepath [description]
     * @param  {object}   data     [description]
     * @param  {Function} callback [description]
     * @return {string}            The request object
     */
    this.post = function(basepath, data) {
        var req = this.agent
            .post(basepath)
            .send(data)
            .expect(200)
            .expect('Content-Type', /json/);

        // include authentication token
        if (this.authToken) {
        	req.set('X-Auth-Token', this.authToken);
        }

        return new Promise( function (resolve, reject) {
        	req.end(function (err, res) {
        		if (err) {
        			reject(err);
        		}
        		else {
        			resolve(res);
        		}
        	});
        });
    };

    this.delete = function(basepath) {
        var req = this.agent
            .delete(basepath)
            .expect(200)
            .expect('Content-Type', /json/);

        // include authentication token
        if (this.authToken) {
        	req.set('X-Auth-Token', this.authToken);
        }

        return new Promise( function (resolve, reject) {
        	req.end(function (err, res) {
        		if (err) {
        			reject(err);
        		}
        		else {
        			resolve(res);
        		}
        	});
        });
    };

    return this;
}

