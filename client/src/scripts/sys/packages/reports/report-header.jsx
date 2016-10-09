import React from 'react';
import { Row, Col, Button, ButtonGroup } from 'react-bootstrap';
import { Card, Fa } from '../../../components';
import FiltersSelector from '../filters/filters-selector';


/**
 * Display the report header for editing
 */
export default class ReportHeader extends React.Component {

    constructor(props) {
        super(props);
    }

    close() {
        console.log('close');
    }

    render() {
        const header = (
            <Row>
                <Col sm={8}>
                    <div className="title">
                    {'Report title'}
                    </div>
                </Col>
                <Col sm={4} xs={12}>
                        <ButtonGroup bsSize="sm" justified>
                            <Button href="#">
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
                    filterValues={this.props.filterValues}
                    onChange={this.props.onChangeFilters}
                    footer={btnSubmit}
                    />
            </Card>
        );
    }
}

ReportHeader.propTypes = {
    report: React.PropTypes.object.isRequired,
    filters: React.PropTypes.array.isRequired,
    filterValues: React.PropTypes.object,
    onChangeFilters: React.PropTypes.func
};
