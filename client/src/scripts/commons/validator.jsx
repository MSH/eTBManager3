
import msgs from './messages';

const emailPattern = /^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/;
const passwordPattern = /((?=.*\d)(?=.*[a-zA-Z]).{6,20})/;

/**
 * Validate a form based on its model
 * @param comp the React component representing the form
 * @param model the model of each field in the form with its validation rules
 */
export function validateForm(comp, model) {
    const errors = {};
    const data = {};

    // create field data
    Object.keys(model).forEach(field => data[field] = comp.refs[field].getValue());

    // validate all fields
    Object.keys(model).forEach(field => {
        const val = data[field];
        const msg = validateValue(val, model[field], data);
        if (msg) {
            errors[field] = msg;
        }
    });

    // prepare result
    var res = {
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
 * @param {object} data is the object that host value
 */
function validateValue(value, model, data) {
    // value is empty ?
    const empty = value === undefined || value === null || value === '';

    if (empty) {
        if (model.required) {
            return msgs.NotNull;
        }
        return null;
    }

    if (typeof value === 'string') {
        if (model.min && value.length < model.min) {
            return msgs.minValue(model.min);
        }

        if (model.max && value.length > model.max) {
            return msgs.maxValue(model.max);
        }
    }

    // check for valid e-mail address
    if (model.email && !emailPattern.test(value)) {
        return msgs.NotValidEmail;
    }

    if (model.password && !passwordPattern.test(value)) {
        return msgs.NotValidPassword;
    }

    if (model.validate) {
        const res = model.validate.call(data);
        if (res) {
            return res;
        }
    }
}
