import React from 'react';
import ReactHighcharts from 'react-highcharts';

const Highcharts = ReactHighcharts.Highcharts;

import './theme';

/**
 * Display a chart
 */
export default class Chart extends React.Component {

    /**
     * Create the configuration of the chart
     */
    config() {
        // transform the series to be compactible with Highcharts
        const type = this.props.type;

        let series;
        if (type === 'pie' || type === 'bar') {
            series = this.props.series.map(s => ({
                name: s.name,
                colorByPoint: true,
                data: s.values.map(v => ({
                    name: v.name,
                    y: v.value
                }))
            }));
        } else {
            series = this.props.series.map(s => ({
                name: s.name,
                data: s.values.map(v => v.value)
            }));
        }

        const chart = {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: this.props.type
            },
            title: {
                text: this.props.title
            },
            credits: {
                enabled: false
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
            series: series,
            xAxis: {
                categories: this.props.series[0].values.map(v => v.name)
            }
        };

        if (type === 'pie') {
            chart.tooltip = {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            };
        }

        return chart;
    }


    render() {
        const cfg = this.config();

        return (
            <ReactHighcharts config={cfg} neverReflow />
        );
    }
}

Chart.propTypes = {
    type: React.PropTypes.oneOf(['pie', 'bar', 'line', 'column', 'area', 'areaspline', 'spline']).isRequired,
    series: React.PropTypes.array.isRequired,
    title: React.PropTypes.string
};
