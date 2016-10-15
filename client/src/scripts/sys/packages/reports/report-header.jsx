import React from 'react';
import { Row, Col, Button, ButtonGroup } from 'react-bootstrap';
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
    }

    close() {
        console.log('close');
    }

    saveClick() {
        this.props.report.save()
            .then(() => this.forceUpdate());
    }

    filtersChanged(filterValues) {
        console.log(filterValues);
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
                                <Fa icon="cog"/>
                                {__('form.options')}
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
                <Button bsStyle="success">
                    {'Generate'}
                </Button>
            </div>
        );

        return (
            <Card header={header} className="rep-editor">
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
