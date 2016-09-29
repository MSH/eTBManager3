import React from 'react';
import { Row, Col, Button, ButtonGroup } from 'react-bootstrap';
import { Card, Fa } from '../../../components';
import FiltersSelector from '../filters/filters-selector';
import { server } from '../../../commons/server';


/**
 * Display the report header for editing
 */
export default class ReportHeader extends React.Component {

    constructor(props) {
        super(props);
        this._onChangeFilterValue = this._onChangeFilterValue.bind(this);
    }

    componentWillMount() {
        const self = this;
        server.post('/api/cases/search/init')
        .then(res => self.setState({ filters: res.filters }));

        this.state = { };
    }

    close() {
        console.log('close');
    }

    _onChangeFilterValue(filterValues) {
        this.setState({ filterValues: filterValues });
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
                    filters={this.state.filters}
                    filterValues={this.state.filterValues}
                    onChange={this._onChangeFilterValue}
                    footer={btnSubmit}
                    />
            </Card>
        );
    }
}

ReportHeader.propTypes = {
    report: React.PropTypes.object.isRequired
};
