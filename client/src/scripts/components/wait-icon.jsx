'use strict';

import React from 'react';

/**
 * Display an animated wait icon in the middle of the page
 */
export default class WaitIcon extends React.Component {

    render() {
        return (
            <div className="cssload-loading center">
                <i></i>
                <i></i>
                <i></i>
            </div>
        );
    }
};