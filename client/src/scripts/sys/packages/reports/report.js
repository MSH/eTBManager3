import Indicator from './indicator';
import { server } from '../../../commons/server';


export default class Report {

    /**
     * Load a report with its indicator data
     */
    static load(id, scope, scopeId) {
        const req = {
            scope: scope,
            scopeId: scopeId,
            reportId: id
        };

        return server.post('/api/cases/report/exec', req)
        .then(res => {
            const rep = new Report(res.result);
            res.result.indicators.forEach(ind => rep.addIndicator(ind.schema, ind.data));
            return rep;
        });
    }

    /**
     * Default constructor, passing the report schema (filters, variables, etc)
     */
    constructor(schema, scope, scopeId) {
        this.schema = schema ? schema :
            // report template
            {
                title: 'Report title (click to change)',
                dashboard: false
            };

        this.scope = scope ? scope : 'WORKSPACE';
        this.scopeId = scopeId ? scopeId : null;

        this.indicators = [];
    }

    /**
     * Add a new indicator to the report
     */
    addIndicator(schema, data) {
        const ind = new Indicator(schema ? schema : {
            title: 'Indicator title (click to change)',
            size: 6,
            chart: 'pie',
            display: 0
        }, data, this.scope, this.scopeId);

        this.indicators.push(ind);
    }

    /**
     * Save the report
     */
    save() {
        const req = Object.assign({}, this.schema);
        const lst = this.indicators.map(ind => ind.schema);
        req.indicators = lst;

        return server.post('/api/cases/report/save', req);
    }
}
