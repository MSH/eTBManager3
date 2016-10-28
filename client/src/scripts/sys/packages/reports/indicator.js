
import { server } from '../../../commons/server';
import { app } from '../../../core/app';


/**
 * Handle indicator functionalities
 */
export default class Indicator {

    /**
     * Constructor, receiving the indicator schema and the result data from server
     * @param schema The indicator schema, containing variables and filters
     * @param data The indicator data
     */
    constructor(schema, data, scope, scopeId) {
        this.schema = schema;
        this.data = data;
        this.scope = scope;
        this.scopeId = scopeId;
    }

    /**
     * Refresh the indicator data
     */
    refresh() {
        const ind = this.schema;

        // TO DO: [Ricardo] just a temporary protection to avoid errors if no variable is defined
        if (!ind.columnVariables || ind.columnVariables.length === 0 ||
            !ind.rowVariables || ind.rowVariables.length === 0) {
            return Promise.resolve(false);
        }

        const self = this;

        this.refreshing = true;
        this._raiseEvent();

        return server.post('/api/cases/report/ind/exec', {
            filters: ind.filters,
            columnVariables: ind.columnVariables,
            rowVariables: ind.rowVariables,
            scope: this.scope,
            scopeId: this.scopeId
        })
        .then(res => {
            self.data = res.indicator;
            self.refreshing = false;
            self._raiseEvent();
            return res.data;
        });
    }

    /**
     * Return the values of the column by its column index
     */
    columnValues(colindex) {
        const res = [];
        const rows = this.data.rows;
        const level = rows.descriptors.length - 1;

        this.data.values.forEach((colvals, index) => {
            const val = colvals[colindex];
            const key = rows.keys[index];
            const id = key[key.length - 1];
            if (id !== 'total') {
                const title = rows.descriptors[level][key[key.length - 1]];
                res.push({ name: title, value: val });
            }
        });

        return res;
    }

    /**
     * Return the values of the row by its row index
     */
    rowValues(rowindex) {
        const res = [];
        const vals = this.data.values[rowindex];
        const cols = this.data.columns;

        for (var c = 0; c < vals.length; c++) {
            const key = cols.keys[c];
            const title = cols.descriptors[key[key.length - 1]];
            res.push({ name: title, value: vals[c] });
        }
        return res;
    }

    /**
     * Return the selected values
     */
    selectedValues() {
        const ind = this.schema;
        const data = this.data;

        const selIndex = ind.selIndex ? ind.selIndex : data.columns.keys.length - 1;

        if (ind.selection === 'row') {
            return this.rowValues(selIndex);
        }

        return this.columnValues(selIndex);
    }

    /**
     * Return the series ready to be rendered by a chart
     */
    selectedSeries() {
        if (!this.data) {
            return null;
        }
        const values = this.selectedValues();

        return [{
            name: 'Series name',
            values: values
        }];
    }

    /**
     * Return the columns to display tables
     */
    tableColumns() {
        const cols = this.data.columns;
        const levels = cols.descriptors.length;
        const res = [];
        for (var i = 0; i < levels; i++) {
            const lst = [];
            res.push(lst);

            let k1 = null;
            let span = 0;
            for (var c = 0; c < cols.keys.length; c++) {
                const k2 = cols.keys[c][i];
                if (k2 === k1) {
                    span++;
                } else {
                    if (k1) {
                        const title = cols.descriptors[i][k1];
                        lst.push({ id: k1, title: title, span: span });
                    }
                    k1 = k2;
                    span = 1;
                }
            }
        }
        return res;
    }

    /**
     * Return the rows for a table rendering
     */
    tableRows() {
        const rows = this.data.rows;
        const res = [];
        rows.keys.forEach(k => {
            const level = k.length - 1;
            const id = k[level];
            const txt = rows.descriptors[level][id];
            res.push(txt);
        });

        return res;
    }

    _raiseEvent() {
        app.dispatch(Indicator.UPDATE_EVT, this);
    }
}

/**
 * Event sent to application to indicate the indicator has changed
 */
Indicator.UPDATE_EVT = 'ind-update';
