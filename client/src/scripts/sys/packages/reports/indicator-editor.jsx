import React from 'react';
import { Card } from '../../../components';
import Chart from './chart';
import FiltersSelector from '../filters/filters-selector';
import VariablesSelector from './variables-selector';


// generate integer random numbers
const rand = num => Math.round(Math.random() * num);

export default class IndicatorEditor extends React.Component {

    constructor(props) {
        super(props);
        this.filtersChange = this.filtersChange.bind(this);
        this.variablesChange = this.variablesChange.bind(this);
    }

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


    filtersChange(filterValues) {
        const ind = this.props.indicator;
        ind.filters = filterValues;
        this.props.onChange(ind);
    }

    variablesChange(colVars, rowVars) {
        const ind = this.props.indicator;
        ind.columnVars = colVars;
        ind.rowVars = rowVars;
        this.props.onChange(ind);
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
                <FiltersSelector filters={this.props.filters}
                    filterValues={ind.filters}
                    onChange={this.filtersChange} />
                <VariablesSelector
                    variables={this.props.variables}
                    onChange={this.variablesChange} />
                <Chart series={series} type={type} />
            </Card>
        );
    }
}

IndicatorEditor.propTypes = {
    indicator: React.PropTypes.object.isRequired,
    filters: React.PropTypes.array.isRequired,
    variables: React.PropTypes.array.isRequired,
    onChange: React.PropTypes.func.isRequired
};
