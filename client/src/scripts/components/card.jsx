
import React from 'react';
import Fa from './fa';

/**
 * Define a page title
 */
export default class Card extends React.Component {

    borderClass() {
        switch (this.props.padding) {
            case 'none': return '';
            case 'small': return ' card-small';
            case 'combine': return ' card-combine';
            default: return ' card-default';
        }
    }

    render() {
        let header = this.props.header || null;

        // header was not defined ?
        if (!header) {
            const title = this.props.title;
            // title or headerRight was defined?
            if (title || this.props.headerRight) {
                header = <h4>{title}</h4>;
            }
        }

        if (header) {
            header = (
                <div className="card-header">
                    {
                        this.props.headerRight &&
                        <div className="pull-right">
                            {this.props.headerRight}
                        </div>
                    }
                    {header}
                </div>
                );
        }

        const cn = this.props.className;
        const className = 'card' + this.borderClass() + (cn ? ' ' + cn : '') +
            (this.props.highlight ? ' highlight' : '');

        return (
            <div className={className} style={this.props.style} onClick={this.props.onClick}>
                {
                    this.props.closeBtn &&
                    <a className="pull-right card-btn-close" onClick={this.props.onClose}>
                        <Fa icon="close"/>
                    </a>
                }
                {header}
                <div className="card-content">
                    {this.props.children}
                </div>
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
    padding: React.PropTypes.oneOf(['none', 'small', 'default', 'combine']),
    highlight: React.PropTypes.bool,
    closeBtn: React.PropTypes.bool,
    onClose: React.PropTypes.func,
    // anything (usually buttons) that must be included in the right side of the header
    headerRight: React.PropTypes.node
};

Card.defaulProps = {
    padding: 'default'
};
