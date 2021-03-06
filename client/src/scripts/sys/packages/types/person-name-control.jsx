import React from 'react';
import { Row, Col, FormControl, ControlLabel, FormGroup, HelpBlock } from 'react-bootstrap';
import { app } from '../../../core/app';
import su from '../../session-utils';
import FormUtils from '../../../forms/form-utils';
import { isEmpty } from '../../../commons/utils';
import msgs from '../../../commons/messages';

import './person-name-control.less';

export default class PersonNameControl extends React.Component {

    static typeName() {
        return 'personName';
    }

    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
        this.validate = this.validate.bind(this);
    }

    /**
     * Called everytime the user changes the name
     */
    onChange(evt) {
        if (this.props.onChange) {
            const fieldVal = evt.target.value;
            const id = evt.target.id;
            let val;

            if (id === 'fullName') {
                val = { name: fieldVal };
            } else {
                // update just the changed field
                const field = {};
                field[evt.target.id] = fieldVal;
                // create a new person name object
                val = Object.assign({}, this.props.value, field);
            }

            // if name is empty, send null
            val = Object.keys(val).find(p => !!val[p]) ? val : null;

            this.props.onChange({ schema: this.props.schema, value: val });
        }
    }

    calcFields() {
        const nameComp = app.getState().session.patientNameComposition;

        switch (nameComp) {
            case 'FIRSTSURNAME': return ['name', 'middleName'];
            case 'SURNAME_FIRSTNAME': return ['middleName', 'name'];
            case 'FIRST_MIDDLE_LASTNAME': return ['name', 'middleName', 'lastName'];
            case 'LAST_FIRST_MIDDLENAME': return ['lastName', 'name', 'middleName'];
            case 'LAST_FIRST_MIDDLENAME_WITHOUT_COMMAS': return ['lastName', 'name'];
            default: return ['name'];
        }
    }

    getInitValue(id) {
        if (id === 'fullName') {
            return this.props.value.name ? this.props.value.name : '';
        }

        return this.props.value[id] ? this.props.value[id] : '';
    }

    placeHolder(id) {
        switch (id) {
            case 'middleName': return __('Patient.middleName');
            case 'lastName': return __('Patient.lastName');
            case 'fullName': return __('Patient.fullName');
            default: return __('Patient.firstName');
        }
    }

    validate(snapshot, value) {
        if (snapshot.required && !isEmpty(value)) {

            const fields = this.calcFields();
            const val = fields.length === 1 ? value : value.name;

            if (isEmpty(val)) {
                return msgs.NotNull;
            }

        } else if (snapshot.required && isEmpty(value)) {

            return msgs.NotNull;

        }

        return null;
    }

    render() {
        const schema = this.props.schema;
        // field is just for displaying ?
        if (schema.readOnly) {
            const content = su.nameDisplay(this.props.value);
            return FormUtils.readOnlyRender(content, schema.label);
        }

        const fields = this.calcFields();

        const size = { sm: 12 / fields.length };

        const errors = this.props.errors;

        return (
            <FormGroup validationState={errors ? 'error' : null}>
            <div className="person-name">
            <Row>
                <Col sm={12}>
                    <ControlLabel>{FormUtils.labelRender(schema.label, schema.required)}</ControlLabel>
                </Col>
            </Row>
            <Row>
            {
                fields.map(id => (
                    <Col {...size} key={id} >
                        <FormControl id={id}
                            ref={id}
                            value={this.props.value ? this.getInitValue(id) : ''}
                            type="text"
                            placeholder={this.placeHolder(id)}
                            onChange={this.onChange} />
                        {
                            (id === 'name' || id === 'fullName') && errors && <HelpBlock>{errors}</HelpBlock>
                        }
                    </Col>
                ))
            }
            </Row>
            </div>
            </FormGroup>
        );
    }
}


PersonNameControl.propTypes = {
    value: React.PropTypes.object,
    onChange: React.PropTypes.func.isRequired,
    errors: React.PropTypes.any,
    schema: React.PropTypes.object.isRequired
};
