
let emailPattern = /^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/;
let passwordPattern = /((?=.*\d)(?=.*[a-zA-Z]).{6,20})/;

/**
 * Validate a form based on its model
 * @param comp the React component representing the form
 * @param model the model of each field in the form with its validation rules
 */
export function validateForm(comp, model) {
    let errors = {};
    let data = {};

    // create field data
    Object.keys(model).forEach( (field) => data[field] = comp.refs[field].getValue() );

    // validate all fields
    Object.keys(model).forEach((field) => {
        let val = data[field];
        let msg = validateValue(val, model[field], data);
        if (msg) {
            errors[field] = msg;
        }
    });

    // prepare result
    let res = {
        value: data
    };

    if (Object.keys(errors).length > 0) {
        res.errors = errors;
    }

    return res;
}

/**
 * Validate a single value based on its model
 * @param value the value to be validated
 * @param model the validation rules of the value
 */
function validateValue(value, model, data) {
    // value is empty ?
    let empty = value === undefined || value === null || value === '';

    if (empty) {
        if (model.required) {
            return 'Value is required';
        }
        else {
            return;
        }
    }

    if (typeof value === 'string') {
        if (model.min && value.length < model.min) {
            return 'Must have at least ' + model.min + ' characters';
        }

        if (model.max && value.length > model.max) {
            return 'Cannot have more than ' + model.max + ' characters';
        }
    }

    // check for valid e-mail address
    if (model.email && !emailPattern.test(value)) {
        return 'e-mail address is not valid';
    }

    if (model.password && !passwordPattern.test(value)) {
        return 'Password length must be at least 6 characteres long and contain at least one digit and letter';
    }

    if (model.validate) {
        let res = model.validate.call(data);
        if (res) {
            return res;
        }
    }
}