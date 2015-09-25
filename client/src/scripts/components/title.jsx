
import React from 'react';

import { PageHeader } from 'react-bootstrap';

/**
 * Define a page title
 */
export default class Title extends React.Component {
    render() {
        var title = this.props.text;
        document.title = 'e-TB Manager' + (title? ' - ' + title: '');

        return (
            <PageHeader>{title}</PageHeader>
        )
    }
}