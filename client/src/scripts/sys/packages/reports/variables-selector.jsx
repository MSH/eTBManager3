import React from 'react';
import { Row, Col } from 'react-bootstrap';
import VariablesBox from './variables-box';

export default class VariablesSelector extends React.Component {

    constructor(props) {
        super(props);
        this.changeColumnVars = this.changeColumnVars.bind(this);
        this.changeRowVars = this.changeRowVars.bind(this);
    }

    changeColumnVars(vars) {
        this.props.onChange(vars, this.props.indicator.rowVariables);
    }

    changeRowVars(vars) {
        this.props.onChange(this.props.indicator.columnVariables, vars);
    }

    render() {
        const ind = this.props.indicator;

        return (
            <div className="var-selector">
            <Row>
                <Col md={6}>
                    <div className="text-muted text-uppercase">
                        {__('global.table.columns')}
                    </div>
                    <div className="mtop">
                        <VariablesBox
                            variables={this.props.variables}
                            values={ind.columnVariables}
                            onChange={this.changeColumnVars}
                            />
                    </div>
                </Col>
                <Col md={6}>
                    <div className="text-muted text-uppercase">
                        {__('global.table.rows')}
                    </div>
                    <div className="mtop">
                        <VariablesBox
                            variables={this.props.variables}
                            values={ind.rowVariables}
                            onChange={this.changeRowVars}
                            />
                    </div>
                </Col>
            </Row>
            </div>
        );
    }
}

VariablesSelector.propTypes = {
    variables: React.PropTypes.array.isRequired,
    indicator: React.PropTypes.object.isRequired,
    onChange: React.PropTypes.func.isRequired
};
