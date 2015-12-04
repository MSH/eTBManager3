
import React from 'react';

/**
 * Define a page title
 */
export default class Card extends React.Component {
    render() {
        let header = this.props.header || null;

        // header was not defined ?
        if (!header) {
            const title = this.props.title;
            // title was defined?
            if (title) {
                header = <div className="card-header"><div className="card-title">{title}</div></div>;
            }
        }

        const children = React.Children.map(this.props.children, function(item) {
            return <div className="card-content">{item}</div>;
        });

        return (
            <div className="card" style={this.props.style}>
                {header}
                {children}
            </div>
        );
    }
}

Card.propTypes = {
    title: React.PropTypes.string,
    header: React.PropTypes.element,
    noPadding: React.PropTypes.bool,
    children: React.PropTypes.any,
    style: React.PropTypes.object
};
