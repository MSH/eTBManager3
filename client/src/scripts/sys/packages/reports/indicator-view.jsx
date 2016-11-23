import React from 'react';
import { Card } from '../../../components';
import Chart from './chart';
import TableView from './table-view'; 

/**
 * Display an indicator inside a card view
 */
export default class IndicatorView extends React.Component {

    render() {
        const ind = this.props.indicator;
        const schema = ind.schema;
        const series = ind.selectedSeries();
        console.log(ind, series);

        return (
            <Card title={schema.title}>
                <Chart series={series} type={schema.chart} />
                <TableView indicator={ind} />
            </Card>
        );
    }
}

IndicatorView.propTypes = {
    indicator: React.PropTypes.object.isRequired
};
