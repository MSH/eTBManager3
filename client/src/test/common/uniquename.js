'use strict';

var shortid = require('shortid');

/**
 * Generate a unique name appending a sequential prefix
 * @param  {[type]} name [description]
 * @return {[type]}      [description]
 */
module.exports = function(name) {
	return name + ' ' + shortid.generate();
};