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
        .then(res => new Report(res.result));
    }

    /**
     * Default constructor, passing the report schema (filters, variables, etc)
     */
    constructor(schema) {
        this.schema = schema ? schema :
            // report template
            {
                title: 'Report title (click to change)',
                dashboard: false
            };

        this.indicators = [];
        // check if there are indicators to construct
        if (this.schema.indicators) {
            this.schema.indicators.forEach(sc =>
                this.indicators.push(new Indicator(sc, sc.data))
            );
        }
    }

    /**
     * Add a new indicator to the report
     */
    addIndicator() {
        const ind = new Indicator({
            title: 'Indicator title (click to change)',
            size: 6,
            chart: 'pie',
            display: 0
        });

        this.indicators.push(ind);
    }

    /**
     * Save the report
     */
    save() {
        const req = Object.assign({}, this.schema);
        const lst = this.indicators.map(ind => ind.schema);
        req.indicators = lst;

        return server.post('/api/cases/report/save', req)
        .then(res => console.log(res));
    }
}
