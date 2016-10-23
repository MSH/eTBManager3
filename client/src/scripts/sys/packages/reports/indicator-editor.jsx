import React from 'react';
import { Row, Col, DropdownButton, Button, MenuItem } from 'react-bootstrap';
import { Card, Fa, WaitIcon, InlineEditor } from '../../../components';
import Chart from './chart';
import TableView from './table-view';

import FiltersSelector from '../filters/filters-selector';
import VariablesSelector from './variables-selector';


/**
 * Display a card for editing of an indicator
 */
export default class IndicatorEditor extends React.Component {

    constructor(props) {
        super(props);
        this.filtersChange = this.filtersChange.bind(this);
        this.variablesChange = this.variablesChange.bind(this);
        this.changeSize = this.changeSize.bind(this);
        this.changeDisplay = this.changeDisplay.bind(this);
        this.changeChart = this.changeChart.bind(this);
        this.refreshClick = this.refreshClick.bind(this);
        this.titleChanged = this.titleChanged.bind(this);

        // initialize an empty state
        this.state = { };
    }

    /**
     * Called when a filter is included, modified or removed
     */
    filtersChange(filterValues) {
        const ind = this.props.indicator;
        ind.schema.filters = filterValues;
        this.props.onChange(ind);
    }

    /**
     * Called when a variable is modified
     */
    variablesChange(colVars, rowVars) {
        const ind = this.props.indicator;
        ind.schema.columnVariables = colVars;
        ind.schema.rowVariables = rowVars;
        this.props.onChange(ind);
    }

    refreshClick() {
        const ind = this.props.indicator;

        const self = this;

        ind.refresh()
            .then(() => self.setState({ fetching: false }));

        this.setState({ fetching: true });
    }

    changeSize(key) {
        this.props.indicator.schema.size = key;
        this.forceUpdate();
    }

    changeDisplay(key) {
        this.props.indicator.schema.display = key;
        this.forceUpdate();
    }

    changeChart(key) {
        this.props.indicator.schema.chart = key;
        this.forceUpdate();
    }

    /**
     * Render the indicator toolbar displayed above the chart/table
     */
    renderToolbar() {
        const ind = this.props.indicator;

        const sizes = [];
        for (var i = 1; i <= 12; i++) {
            sizes.push(i);
        }
        const displays = [
            'All', 'Chart only', 'Table only'
        ];
        const charts = ['pie', 'bar', 'line', 'column', 'area', 'areaspline', 'spline'];

        return (
            <Row>
                <Col sm={8}>
                    <span className="ctrl-space">
                        <label className="text-muted">{__('indicators.display') + ':'}</label>
                        <DropdownButton id="btnDisp" bsSize="small"
                            title={displays[ind.schema.display]}
                            onSelect={this.changeDisplay}>
                        {
                            displays.map((s, index) =>
                                <MenuItem key={index} eventKey={index}>{s}</MenuItem>)
                        }
                        </DropdownButton>
                    </span>

                    <span className="ctrl-space">
                        <label className="text-muted">{__('indicators.size') + ':'}</label>
                        <DropdownButton id="btnSize" bsSize="small"
                            title={ind.schema.size}
                            onSelect={this.changeSize}>
                        {
                            sizes.map(n =>
                                <MenuItem key={n} eventKey={n}>{n}</MenuItem>)
                        }
                        </DropdownButton>
                    </span>

                    <label className="text-muted">{__('indicators.chart') + ':'}</label>
                    <DropdownButton id="btnChart" bsSize="small"
                        title={ind.schema.chart}
                        onSelect={this.changeChart}>
                    {
                        charts.map((s, index) =>
                            <MenuItem key={index} eventKey={s}>{s}</MenuItem>)
                    }
                    </DropdownButton>
                </Col>
                <Col sm={4}>
                    <Button bsStyle="default" bsSize="small" className="pull-right"
                        onClick={this.refreshClick}>
                        <Fa icon="refresh" />{__('action.refresh')}
                    </Button>
                </Col>
            </Row>
        );
    }


    titleChanged(value) {
        this.props.indicator.schema.title = value;
        this.forceUpdate();
    }

    render() {
        const ind = this.props.indicator;
        const schema = ind.schema;
        const series = ind.selectedSeries();
        const fetching = this.state.fetching;

        const title = (
            <InlineEditor value={ind.schema.title}
                className="title"
                onChange={this.titleChanged} />
        );

        return (
            <Card header={title} closeBtn>
                <FiltersSelector filters={this.props.filters}
                    filterValues={schema.filters}
                    onChange={this.filtersChange} />
                <VariablesSelector
                    variables={this.props.variables}
                    indicator={schema}
                    onChange={this.variablesChange} />
                {
                    this.renderToolbar()
                }
                {
                    series && !fetching &&
                    <div>
                        <Chart series={series} type={schema.chart} />
                        <TableView indicator={ind} />
                    </div>
                }
                {
                    fetching && <WaitIcon type="card" />
                }
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
