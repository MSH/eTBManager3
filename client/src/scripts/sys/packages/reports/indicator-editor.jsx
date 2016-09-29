import React from 'react';
import { Card } from '../../../components';
import ReactHighcharts from 'react-highcharts';

import './theme';

// generate integer random numbers
const rand = num => Math.round(Math.random() * num);

export default class IndicatorEditor extends React.Component {

    randomValues() {
        const count = rand(10) + 1;

        const res = [];
        for (var k = 0; k < count; k++) {
            res.push({
                name: 'Item ' + (k + 1),
                y: rand(50)
            });
        }

        let i = rand(count);
        i = i >= res.length ? i - 1 : i;

        res[i].sliced = true;
        res[i].selected = true;

        return res;
    }

    render() {
        const ind = this.props.indicator;
        const Highcharts = ReactHighcharts.Highcharts;

        const config = {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: 'Graph random values'
            },
            credits: {
                enabled: false
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    }
                }
            },
            series: [{
                name: 'Brands',
                colorByPoint: true,
                data: this.randomValues()
            }]
        };

        return (
            <Card title={ind.title} closeBtn>
                <ReactHighcharts config={config} neverReflow />
            </Card>
        );
    }
}

IndicatorEditor.propTypes = {
    indicator: React.PropTypes.object.isRequired,
    onChange: React.PropTypes.func
};
