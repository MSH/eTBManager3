
import React from 'react';

import { PageHeader } from 'react-bootstrap';

/**
 * Define a page title
 */
export default class Card extends React.Component {
    render() {
        let title = this.props.title;

        let children = React.Children.map(this.props.children, function(item) {
            return <div className='card-content'>{item}</div>;
        });

        return (
            <div className='card' style={this.props.style}>
                {title && <div className='card-title'>{title}</div>}
                {children}
            </div>
        )
    }
}