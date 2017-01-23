
import React from 'react';
import { FormGroup, HelpBlock, Checkbox } from 'react-bootstrap';


export default class BoolControl extends React.Component {

    static typeName() {
        return 'bool';
    }

    static defaultValue() {
        return false;
    }

    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
    }

    // focus() {
    //     this.refs.chk.focus();
    //     return true;
    // }

    onChange(evt) {
        const value = evt.target.checked;
        this.props.onChange({ schema: this.props.schema, value: value });
    }

    render() {
        const sc = this.props.schema;
        const errors = this.props.errors;
        const value = !!this.props.value;

        return (
            <FormGroup validationState={errors ? 'error' : null}>
                <Checkbox ref="chk"
                    checked={value}
                    onChange={this.onChange}>
                    {
                        sc.label
                    }
                </Checkbox>
                {
                    errors && <HelpBlock>{errors}</HelpBlock>
                }
            </FormGroup>
        );
    }
}


BoolControl.propTypes = {
    value: React.PropTypes.bool,
    schema: React.PropTypes.object,
    onChange: React.PropTypes.func,
    errors: React.PropTypes.any,
    resources: React.PropTypes.any
};
