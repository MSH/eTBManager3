import Indicator from './indicator';
import { server } from '../../../commons/server';


export default class Report {

    constructor(schema) {
        this.schema = schema ? schema :
            // report template
            {
                title: 'Report title (click to change)',
                dashboard: false
            };

        this.indicators = [];
        this.addIndicator();
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
