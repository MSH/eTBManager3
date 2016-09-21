import React from 'react';
import { HelpBlock } from 'react-bootstrap';
import { isString } from '../commons/utils';

/**
 * Simple component to display an error message used in public and init pages.
 * Error message may be a string or an object containing a msg property (from server side)
 * @param {[type]} props [description]
 */
export default function Error(props) {
    const error = props.msg;

    if (!error) {
        return null;
    }

    const msg = isString(error) ? error : error.msg;

    return <HelpBlock>{msg}</HelpBlock>;
}

Error.propTypes = {
    msg: React.PropTypes.any
};
