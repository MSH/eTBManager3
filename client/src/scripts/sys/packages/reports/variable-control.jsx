import React from 'react';
import { PopupControl } from '../../../components';
import GroupPopup from '../filters/group-popup';

/**
 * A simple control to display the content of the variable
 */
export default class VariableControl extends React.Component {

    constructor(props) {
        super(props);
        this.varSelected = this.varSelected.bind(this);
    }

    varSelected(variable) {
        console.log('var = ', variable);
        this.props.onChange(variable);
    }

    render() {
        const content = this.props.value ? this.props.value.name : '-';
        const popupContent = (
            <GroupPopup
                groups={this.props.variables}
                childrenProperty="variables"
                onSelect={this.varSelected}
                />
        );

        return (
            <PopupControl
                refs="popup"
                content={content}
                popupContent={popupContent}/>
        );
    }
}

VariableControl.propTypes = {
    variables: React.PropTypes.array.isRequired,
    value: React.PropTypes.object,
    onChange: React.PropTypes.func.isRequired
};
