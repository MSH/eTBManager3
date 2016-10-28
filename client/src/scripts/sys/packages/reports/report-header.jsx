import React from 'react';
import { Row, Col, Button, ButtonGroup, Checkbox } from 'react-bootstrap';
import { Card, Fa, InlineEditor } from '../../../components';
import FiltersSelector from '../filters/filters-selector';


/**
 * Display the report header for editing
 */
export default class ReportHeader extends React.Component {

    constructor(props) {
        super(props);
        this.titleChanged = this.titleChanged.bind(this);
        this.saveClick = this.saveClick.bind(this);
        this.filtersChanged = this.filtersChanged.bind(this);
        this.dashboardClick = this.dashboardClick.bind(this);
        this.generate = this.generate.bind(this);
    }

    dashboardClick(evt) {
        const value = evt.target.checked;
        this.props.report.schema.dashboard = value;
        this.forceUpdate();
    }

    generate() {
        const rep = this.props.report;
        const self = this;

        rep.generate()
            .then(() => self.forceUpdate());
    }

    close() {
        console.log('close');
    }

    saveClick() {
        this.props.report.save()
            .then(() => this.forceUpdate());
    }

    filtersChanged(filterValues) {
        this.props.report.schema.filters = filterValues;
        this.forceUpdate();
    }

    /**
     * Called when the title of the report is changed
     */
    titleChanged(title) {
        this.props.report.schema.title = title;
        this.forceUpdate();
    }

    render() {
        const report = this.props.report;

        const header = (
            <Row>
                <Col sm={8}>
                    <div>
                        <InlineEditor value={report.schema.title}
                            className="title"
                            onChange={this.titleChanged} />
                    </div>
                </Col>
                <Col sm={4} xs={12}>
                        <ButtonGroup bsSize="sm" justified>
                            <Button href="#" onClick={this.saveClick}>
                                <Fa icon="save"/>
                                {__('action.save')}
                            </Button>
                            <Button href="#">
                                <Fa icon="close" />
                                {__('action.close')}
                            </Button>
                        </ButtonGroup>
                </Col>
            </Row>
        );

        const btnSubmit = (
            <div className="pull-right">
                <Button bsStyle="success" onClick={this.generate}>
                    {__('action.generate')}
                </Button>
            </div>
        );

        return (
            <Card header={header} className="rep-editor">
                <Checkbox checked={report.schema.dashboard} onClick={this.dashboardClick}>
                    {__('indicators.showdashboard')}
                </Checkbox>
                <FiltersSelector
                    filters={this.props.filters}
                    filterValues={report.schema.filters}
                    onChange={this.filtersChanged}
                    footer={btnSubmit}
                    />
            </Card>
        );
    }
}

ReportHeader.propTypes = {
    report: React.PropTypes.object.isRequired,
    filters: React.PropTypes.array.isRequired
};
