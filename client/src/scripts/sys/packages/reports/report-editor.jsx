import React from 'react';
import { Button, Row, Col } from 'react-bootstrap';
import { WaitIcon } from '../../../components';
import { server } from '../../../commons/server';

import ReportHeader from './report-header';
import IndicatorEditor from './indicator-editor';
import Indicator from './indicator';


export default class ReportEditor extends React.Component {

    constructor(props) {
        super(props);
        this.addIndicator = this.addIndicator.bind(this);
        this.filtersChange = this.filtersChange.bind(this);
        this.indicatorChange = this.indicatorChange.bind(this);
    }

    componentWillMount() {
        const self = this;
        server.post('/api/cases/indicator/init')
        .then(res => self.setState({ filters: res.filters, variables: res.variables }));

        const report = {
            title: 'Report title (click to change)',
            indicators: []
        };

        this.setState({ report: report });
    }

    /**
     * Add an indicator to the report
     */
    addIndicator() {
        const ind = new Indicator({
            title: 'Indicator title (click to change)',
            size: 6,
            chart: 'pie',
            display: 0
        });

        let inds = this.state.indicators;

        inds = inds ? inds.slice(0).push(ind) : [ind];

        this.setState({ indicators: inds });
    }

    /**
     * Called when the report filters change
     */
    filtersChange(filterValues) {
        const rep = Object.assign({ },
            this.state.report,
            {
                filterValues: filterValues
            });

        this.setState({ report: rep });
    }

    /**
     * Called when a property of an indicator changes
     */
    indicatorChange(ind) {
        const rep = this.state.report;
        const index = rep.indicators.indexOf(ind);

        rep.indicators[index] = Object.assign({}, ind);
        this.forceUpdate();
    }

    /**
     * Render the given indicator
     */
    renderIndicators() {
        const indicators = this.state.indicators;

        if (!indicators || indicators.length === 0) {
            return null;
        }

        return indicators.map((ind, index) => (
            <Row key={index}>
                <Col sm={12}>
                    <IndicatorEditor
                        indicator={ind}
                        filters={this.state.filters}
                        variables={this.state.variables}
                        onChange={this.indicatorChange}
                        />
                </Col>
            </Row>
        ));
    }

    render() {
        const report = this.state.report;
        const filters = this.state.filters;

        if (!filters) {
            return <WaitIcon />;
        }

        return (
            <div className="report">
                <ReportHeader report={report}
                    filters={filters}
                    filterValues={report.filterValues}
                    onChangeFilters={this.filtersChange}
                    />
                {
                    this.renderIndicators()
                }
                <Row>
                    <Col sm={12}>
                        <Button onClick={this.addIndicator}>
                            {__('indicators.add')}
                        </Button>
                    </Col>
                </Row>
            </div>
        );
    }
}

ReportEditor.propTypes = {
    scope: React.PropTypes.oneOf(['WORKSPACE', 'ADMINUNIT', 'UNIT']).isRequired,
    scopeId: React.PropTypes.string
};
