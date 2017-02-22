import React from 'react';
import VariableControl from './variable-control';
import { LinkTooltip } from '../../../components';


export default class VariablesBox extends React.Component {

    constructor(props) {
        super(props);
        this.varChange = this.varChange.bind(this);
        this.varRemove = this.varRemove.bind(this);
    }

    /**
     * Remove a variable by its index position
     */
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

    /**
     * Serach for a variable object by its ID
     */
    variableById(varId) {
        const groups = this.props.variables;
        for (var i = 0; i < groups.length; i++) {
            const grp = groups[i];
            const va = grp.variables.find(v => v.id === varId);
            if (va) {
                return va;
            }
        }
        return null;
    }

    /**
     * Called when user selects a variable in the variable control
     */
    varChange(index) {
        return variable => {
            const vars = this.props.values ? this.props.values.slice(0) : [];

            const i = vars.findIndex(id => id === variable.id);
            if (i >= 0) {
                return;
            }

            // is a new variable ?
            if (index === -1) {
                vars.push(variable.id);
            } else {
                vars[index] = variable.id;
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
                        value={this.variableById(variable)}
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
