import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { LinkTooltip } from '../../../components';

export default class VariablesSelector extends React.Component {

    constructor(props) {
        super(props);
        this.addColumnVar = this.addColumnVar.bind(this);
        this.addRowVar = this.addRowVar.bind(this);
    }

    addColumnVar() {
        console.log('add var to column');
    }

    addRowVar() {
        console.log('add var to row');
    }

    render() {
        return (
            <div style={{ backgroundColor: '#f8f8f8', padding: '8px', margin: '8px -8px' }}>
            <Row>
                <Col md={6}>
                    <div className="text-muted text-uppercase">
                        {__('global.table.columns')}
                    </div>
                    <div className="mtop">
                        <LinkTooltip ref="btnadd" toolTip={__('indicators.vars.add')} icon="plus"
                            onClick={this.addColumnVar} />
                    </div>
                </Col>
                <Col md={6}>
                    <div className="text-muted text-uppercase">
                        {__('global.table.rows')}
                    </div>
                    <div className="mtop">
                        <LinkTooltip ref="btnadd" toolTip={__('indicators.vars.add')} icon="plus"
                            onClick={this.addRowVar} />
                    </div>
                </Col>
            </Row>
            </div>
        );
    }
}

VariablesSelector.propTypes = {
    variables: React.PropTypes.array.isRequired,
    values: React.PropTypes.array,
    onChange: React.PropTypes.func.isRequired
};
