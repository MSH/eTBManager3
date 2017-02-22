/**
 * A helper class to expose crud functionalities to an entity
 */

import { server } from './server';
import { app } from '../core/app';
import { DOC_CREATE, DOC_UPDATE, DOC_DELETE } from '../core/actions';

const API_PREFIX = '/api/tbl/';


/**
 * Standard library for CRUD (create-read-update-delete) + query commands
 */
export default class CRUD {

    constructor(tbl) {
        this.table = tbl;
        this.fetchFormRequest = null;
    }

    /**
     * Create a new entity based on the given request parameter
     * @param  {[type]} req [description]
     * @return {[type]}     [description]
     */
    create(req) {
        return server.post(API_PREFIX + this.table, req)
        .then(res => {
            if (res.errors) {
                return Promise.reject(res.errors);
            }

            // inform application about new document
            app.dispatch(DOC_CREATE, { type: this.table, doc: req, id: res.result });

            return res.result;
        });
    }

    /**
     * Fetch the default form to be used for the entity
     * @param {Object} data the list of arguments to be sent in the post
     * @param {boolean} readOnly if true, it will return the read-only form
     * @return {Promise} a promise object that will be resolved when data is fetched
     */
    fetchForm(args, readOnly) {
        const data = this.fetchFormRequest ? this.fetchFormRequest(args) : args;
        return server.post(API_PREFIX + this.table + '/form' +
            (readOnly ? '?ro' : ''), data);
    }

    /**
     * Find a single entity by the given id
     * @param  {string} id The ID of the entity
     * @param  {bool} form if true, will fetch form information
     * @return {Promise}   Promise that will be resolved when server posts answer
     */
    get(id, form) {
        return server.get(API_PREFIX + this.table + '/' + id + (form ? '&form' : ''));
    }

    /**
     * Return data about an entity in a format ready to be edited
     * @param  {String} id The ID of the entity
     * @return {Promise}    Promise that will be resolved when server returns
     */
    getEdit(id) {
        return server.get(API_PREFIX + this.table + '/form/' + id);
    }

    /**
     * Update the data of the entity
     * @param  {string} id   The ID of the entity
     * @param  {object} data Request object with changes
     * @return {Promise}   Promise that will be resolved when server posts answer
     */
    update(id, data) {
        return server.post(API_PREFIX + this.table + '/' + id, data)
        .then(res => {
            if (!res.success) {
                return Promise.reject(res.errors);
            }

            // inform application about document updated
            app.dispatch(DOC_UPDATE, { type: this.table, doc: data, id: id });

            return res.result;
        });
    }

    /**
     * Delete the entity based on the given ID
     * @param  {[type]} id [description]
     * @return {Promise}   Promise that will be resolved when server posts answer
     */
    delete(id) {
        return server.delete(API_PREFIX + this.table + '/' + id)
        .then(res => {
            // inform application about deleted document
            app.dispatch(DOC_DELETE, { type: this.table, id: id });

            return res;
        });
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
