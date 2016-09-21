
import React from 'react';
import ReactDOM from 'react-dom';
import { FormGroup, FormControl, ControlLabel, HelpBlock } from 'react-bootstrap';
import FormUtils from '../form-utils';
import { stringValidator, numberValidator } from '../impl/validators';

/**
 * Used in the Form library. Provide input data of string and number types
 */
export default class InputControl extends React.Component {

    static typeName() {
        return ['string', 'number', 'int', 'float'];
    }

    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
        this.focus = this.focus.bind(this);
    }

    validate() {
        const schema = this.props.schema;
        const value = ReactDOM.findDOMNode(this.refs.input).value;

        return schema.type === 'string' ?
            stringValidator(schema, value) :
            numberValidator(schema, value);
    }


    /**
     * Set the component focus
     * @return {[type]} [description]
     */
    focus() {
        ReactDOM.findDOMNode(this.refs.input).focus();
        return true;
    }

    /**
     * Check if type being handled in a number
     * @return {Boolean} True if type is supposed to be a number
     */
    isNumericType() {
        return ['number', 'int', 'float'].indexOf(this.props.schema.type) >= 0;
    }

    /**
     * Called when user changes the value in the control
     * @return {[type]} [description]
     */
    onChange(evt) {
        const sc = this.props.schema;
        let value = evt.target.value;

        // if it is an empty string, so return null
        if (!value) {
            value = null;
        }
        else if (this.isNumericType() && !isNaN(value)) {
            value = Number(value);
        }

        this.props.onChange({ schema: sc, value: value });
    }


    render() {
        const sc = this.props.schema;
        if (sc.readOnly) {
            return FormUtils.readOnlyRender(this.props.value, sc.label);
        }

        const errors = this.props.errors;

        const ctype = sc.password ? 'password' : 'text';

        const label = FormUtils.labelRender(sc.label, sc.required);
        const val = this.props.value ? this.props.value : '';

        return (
            <FormGroup validationState={errors ? 'error' : null}>
                {
                    label &&
                    <ControlLabel>{label}</ControlLabel>
                }
                <FormControl ref="input"
                    type={ctype}
                    value={val}
                    onChange={this.onChange}
                    />
                {
                    errors && <HelpBlock>{errors}</HelpBlock>
                }
            </FormGroup>
            );
    }

}

InputControl.propTypes = {
    value: React.PropTypes.any,
    schema: React.PropTypes.object,
    onChange: React.PropTypes.func,
    errors: React.PropTypes.any,
    resources: React.PropTypes.any
};
