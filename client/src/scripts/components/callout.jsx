
import React from 'react';

export default class Callout extends React.Component {

    render() {
        let className = 'callout';
        if (this.props.bsStyle) {
            className += ' callout-' + this.props.bsStyle;
        }

        return (
            <div className={className}>
                {this.props.children}
            </div>
            );
    }
}

Callout.propTypes = {
    bsStyle: React.PropTypes.string,
    children: React.PropTypes.any
};
