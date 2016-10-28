import React from 'react';
import { Button, Row, Col } from 'react-bootstrap';
import { WaitIcon } from '../../../components';
import { server } from '../../../commons/server';

import ReportHeader from './report-header';
import IndicatorEditor from './indicator-editor';
import Report from './report';


export default class ReportEditor extends React.Component {

    constructor(props) {
        super(props);
        this.addIndicator = this.addIndicator.bind(this);
    }

    componentWillMount() {
        const self = this;
        server.post('/api/cases/report/init')
        .then(res => self.setState({ filters: res.filters, variables: res.variables }));

        const id = this.props.route.queryParam('rep');

        let rep;

        if (id) {
            // load the report
            Report.load(id, this.props.scope, this.props.scopeId)
            .then(report => self.setState({ report: report }));

            // temporarily, while loading the report
            rep = null;
        } else {
            // create a new report from the editor
            rep = new Report(null, this.props.scope, this.props.scopeId);
            rep.addIndicator();
        }
        this.setState({ report: rep });
    }

    /**
     * Add an indicator to the report
     */
    addIndicator() {
        this.state.report.addIndicator();
        this.forceUpdate();
    }

    /**
     * Render the given indicator
     */
    renderIndicators() {
        const rep = this.state.report;

        return rep.indicators.map((ind, index) => (
            <Row key={index}>
                <Col sm={12}>
                    <IndicatorEditor
                        indicator={ind}
                        filters={this.state.filters}
                        variables={this.state.variables}
                        />
                </Col>
            </Row>
        ));
    }

    render() {
        const report = this.state.report;
        const filters = this.state.filters;

        if (!filters || !report) {
            return <WaitIcon />;
        }

        return (
            <div className="report">
                <ReportHeader report={report}
                    filters={filters}
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
    scopeId: React.PropTypes.string,
    route: React.PropTypes.object
};
