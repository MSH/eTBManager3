
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
                header = <h4>{title}</h4>;
            }
        }

        if (header) {
            header = <div className="card-header">{header}</div>;
        }

        const contentClass = 'card-content' + (this.props.noPadding ? ' no-padding' : '');

        const children = React.Children.map(this.props.children, function(item) {
            return <div className={contentClass}>{item}</div>;
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
