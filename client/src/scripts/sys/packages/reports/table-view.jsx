import React from 'react';
import Indicator from './indicator';

import './table-view.less';

export default class TableView extends React.Component {

    render() {
        const ind = this.props.indicator;
        const data = ind.data;

        const colCount = data.columns.keys.length;
        const rowCount = data.rows.keys.length;

        // check if there is anything to render
        if (colCount === 0 || rowCount === 0) {
            return null;
        }

        // get the columns to render
        const colTitles = ind.tableColumns();
        const rowTitles = ind.tableRows();

        // calc the number of columns
        const colsCount = colTitles[colTitles.length - 1].length;

        return (
            <div className="table-responsive">
                <table className="table-indicator">
                <tbody>
                    <tr>
                        <th rowSpan={colTitles.length + 1}>
                        {
                            data.rows.variables.map(v => <div key={v.id}>{v.name}</div>)
                        }
                        </th>
                        <th colSpan={colsCount}>
                        {
                            data.columns.variables.map(v => v.name).join(', ')
                        }
                        </th>
                        <th rowSpan={colTitles.length + 1}>
                        {__('global.total')}
                        </th>
                    </tr>
                    {
                        colTitles.map((colrow, index) => (
                        <tr key={index}>
                        {
                            colrow.map(c => (
                                <th key={c.id} colSpan={c.span}>
                                {
                                    c.title
                                }
                                </th>
                            ))
                        }
                        </tr>
                        ))
                    }
                    {
                        data.values.map((vals, index) =>
                            <tr key={index}>
                                <th>
                                {rowTitles[index]}
                                </th>
                                {
                                    vals.map((val, ind2) => (
                                        <td key={ind2}>{val}</td>
                                    ))
                                }
                            </tr>
                        )
                    }
                </tbody>
                </table>
            </div>
        );
    }
}

TableView.propTypes = {
    indicator: React.PropTypes.instanceOf(Indicator).isRequired
};
