/**
 * A helper class to expose crud functionalities to an entity
 */

import { server } from './server';

const API_PREFIX = '/api/tbl/';


export default class CRUD {

	constructor(tbl) {
		this.table = tbl;
	}

	/**
	 * Create a new entity based on the given request parameter
	 * @param  {[type]} req [description]
	 * @return {[type]}     [description]
	 */
	create(req) {
		return server.post(API_PREFIX + this.table, req);
	}

	/**
	 * Find a single entity by the given id
	 * @param  {string} id The ID of the entity
	 * @return {Promise}   Promise that will be resolved when server posts answer
	 */
	get(id) {
		return server.get(API_PREFIX + this.table + '/' + id);
	}

	/**
	 * Update the data of the entity
	 * @param  {string} id   The ID of the entity
	 * @param  {object} data Request object with changes
	 * @return {Promise}   Promise that will be resolved when server posts answer
	 */
	update(id, data) {
		return server.post(API_PREFIX + this.table, data);
	}

	/**
	 * Delete the entity based on the given ID
	 * @param  {[type]} id [description]
	 * @return {Promise}   Promise that will be resolved when server posts answer
	 */
	delete(id) {
		return server.delete(API_PREFIX + this.table + '/' + id);
	}

	/**
	 * Query the database for a list of entity based on the query
	 * @param  {object} qry Object containing the query
	 * @return {Promise}   Promise that will be resolved when server posts answer
	 */
	query(qry) {
		return server.post('/api/tbl/' + this.table + '/query', qry);
	}
}
