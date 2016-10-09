import ReactHighchart from 'react-highcharts';

const Highcharts = ReactHighchart.Highcharts;

Highcharts.theme = {
    colors: [
        '#66903c',
        '#0f3c27',
        '#5e6c77',
        '#bababc',
        '#e0dde1',
        '#b2ca7e',
        // Other palette
        '#006906',
        '#00850a',
        '#81b824',
        '#c5d710',
        '#ebe911'
    ]
};

// Apply the theme
Highcharts.setOptions(Highcharts.theme);
