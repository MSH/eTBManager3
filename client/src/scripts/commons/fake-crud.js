/**
 * A helper class to expose query functionality
 * To be used when inserting crud-pagination on result lists
 */

import { server } from './server';

export default class FakeCRUD {

    constructor(path) {
        this.path = path;
    }

    /**
     * Query the database for a result list of the exposed service on the path
     * @param  {object} qry Object containing the query
     * @return {Promise}   Promise that will be resolved when server posts answer
     */
    query(qry) {
        return server.post(this.path, qry);
    }
}
