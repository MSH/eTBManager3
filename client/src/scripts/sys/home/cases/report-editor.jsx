import React from 'react';
import { Button, Row, Col } from 'react-bootstrap';
import { arrangeGrid } from '../../../commons/grid-utils';

import ReportHeader from './reports/report-header';
import IndicatorEditor from './reports/indicator-editor';


export default class ReportEditor extends React.Component {

    constructor(props) {
        super(props);
        this.addIndicator = this.addIndicator.bind(this);
    }

    componentWillMount() {
        const report = {
            title: 'Report title (click to change)',
            indicators: [
                {
                    title: 'Indicator title (click to change)',
                    size: 12
                }
            ]
        };

        this.setState({ report: report });
    }

    /**
     * Add an indicator to the report
     */
    addIndicator() {
        const rep = this.state.report;
        rep.indicators.push({
            title: 'Indicator title (click to change)',
            size: 6
        });

        this.setState({ report: rep });
    }

    renderIndicators(indicators) {
        if (!indicators || indicators.length === 0) {
            return null;
        }

        // generate the list of indicators and its size to be arranged in a grid
        const lst = indicators.map(ind => ({
            content: <IndicatorEditor indicator={ind} />,
            size: { sm: ind.size }
        }));

        return arrangeGrid(lst);
    }

    render() {
        const report = this.state.report;

        return (
            <div className="report">
                <ReportHeader report={report}/>
                {
                    this.renderIndicators(report.indicators)
                }
                <Row>
                    <Col sm={12}>
                        <Button bsStyle="success" onClick={this.addIndicator}>
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
