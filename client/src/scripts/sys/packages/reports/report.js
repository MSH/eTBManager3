import Indicator from './indicator';
import { server } from '../../../commons/server';

/**
 * Report class to handle business rules for case reporting generation
 */
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
            const rep = new Report(res.result, scope, scopeId);
            rep.id = id;
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
            title: __('reports.new.title'),
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
        const ind = new Indicator(this,
            schema ? schema : {
                title: __('indicators.new.title'),
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

        if (this.id) {
            return server.post('/api/cases/report/save/' + this.id, req);
        }

        const self = this;
        return server.post('/api/cases/report/save', req)
            .then(res => {
                self.id = res.result;
            });
    }

    /**
     * Delete the current report, setting this to the state of 'NEW', i.e,
     * if it is saved again, it will be created a new one
     */
    delete() {
        const self = this;

        return server.post('/api/cases/report/delete/' + this.id)
            .then(res => {
                delete self.id;
                return res;
            });
    }

    /**
     * Generate the indicators
     */
    generate() {
        let index = 0;
        const self = this;

        // local function to run it recursively
        const execInd = ind => ind
            .refresh()
            .then(() => {
                index++;
                if (index < self.indicators.length) {
                    execInd(self.indicators[index]);
                }
            });

        if (this.indicators.length > 0) {
            return execInd(this.indicators[index]);
        }

        return null;
    }
}
