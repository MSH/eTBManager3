
import React from 'react';

/**
 * Define a page title
 */
export default class Card extends React.Component {

    borderClass() {
        switch (this.props.padding) {
            case 'none': return '';
            case 'small': return ' card-small';
            default: return ' card-default';
        }
    }

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

        const contentClass = 'card-content';

        const children = React.Children.map(this.props.children, function(item) {
            return <div className={contentClass}>{item}</div>;
        });

        const cn = this.props.className;
        const className = 'card' + this.borderClass() + (cn ? ' ' + cn : '') +
            (this.props.highlight ? ' highlight' : '');

        return (
            <div className={className} style={this.props.style} onClick={this.props.onClick}>
                {header}
                {children}
            </div>
        );
    }
}

Card.propTypes = {
    title: React.PropTypes.string,
    header: React.PropTypes.element,
    children: React.PropTypes.any,
    style: React.PropTypes.object,
    onClick: React.PropTypes.func,
    className: React.PropTypes.string,
    padding: React.PropTypes.oneOf(['none', 'small', 'default']),
    highlight: React.PropTypes.bool
};

Card.defaulProps = {
    padding: 'default'
};
