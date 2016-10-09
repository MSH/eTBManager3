import React from 'react';
import VariableControl from './variable-control';
import { LinkTooltip } from '../../../components';


export default class VariablesBox extends React.Component {

    constructor(props) {
        super(props);
        this.varChange = this.varChange.bind(this);
        this.varRemove = this.varRemove.bind(this);
    }

    varRemove(index) {
        if (index === -1) {
            return null;
        }

        return () => {
            const vars = this.props.values.slice(0);
            vars.splice(index, 1);
            this.props.onChange(vars);
        };
    }

    varChange(index) {
        return variable => {
            const vars = this.props.values ? this.props.values.slice(0) : [];

            const i = vars.findIndex(v => v.id === variable.id);
            if (i !== index) {
                return;
            }

            // is a new variable ?
            if (index === -1) {
                vars.push(variable);
            } else {
                vars[index] = variable;
            }
            this.props.onChange(vars);
        };
    }

    renderVar(variable, index) {
        return (
            <div className="vars-box" key={index}>
                <div className="var-del">
                    <LinkTooltip
                        toolTip={__('form.filters.remove')}
                        onClick={this.varRemove(index)}
                        icon="minus"
                        className="remove-link" />
                </div>
                <div className="var-control">
                    <VariableControl
                        variables={this.props.variables}
                        value={variable}
                        onChange={this.varChange(index)}
                        />
                </div>
            </div>
        );
    }

    render() {
        const vars = this.props.values;

        return (
            <div>
            {
                vars && vars.map((variable, index) => this.renderVar(variable, index))
            }
            {
                this.renderVar(null, -1)
            }
            </div>
        );
    }
}

VariablesBox.propTypes = {
    variables: React.PropTypes.array.isRequired,
    values: React.PropTypes.array,
    onChange: React.PropTypes.func.isRequired
};
