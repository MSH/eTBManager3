
import React from 'react';
import SessionUtils from './session-utils';


/**
 * Fake page used to check the user view when system is called at ready state
 */
export default class Start extends React.Component {

    componentWillMount() {
        // check user view
        SessionUtils.gotoHome();
    }

    render() {
        return null;
    }
}
