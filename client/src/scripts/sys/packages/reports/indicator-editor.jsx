import React from 'react';
import { Card } from '../../../components';
import Chart from './chart';


// generate integer random numbers
const rand = num => Math.round(Math.random() * num);

export default class IndicatorEditor extends React.Component {

    randomSeries() {
        const count = rand(10) + 1;

        const res = [];
        for (var k = 0; k < count; k++) {
            res.push({
                name: 'Item ' + (k + 1),
                value: rand(50)
            });
        }

        let i = rand(count);
        i = i >= res.length ? i - 1 : i;

        res[i].sliced = true;
        res[i].selected = true;

        return [
            {
                name: 'Diagnosis type',
                values: res
            }
        ];
    }

    render() {
        const ind = this.props.indicator;
        const series = this.randomSeries();
        const types = [
            'pie', 'bar', 'spline', 'line', 'column', 'area', 'areaspline'
        ];
        const index = ind.index % types.length;
        const type = types[index];

        console.log(type, index, ind);

        return (
            <Card title={'Diagnosis type x Age range'} closeBtn>
                <Chart series={series} type={type} />
            </Card>
        );
    }
}

IndicatorEditor.propTypes = {
    indicator: React.PropTypes.object.isRequired,
    onChange: React.PropTypes.func
};
