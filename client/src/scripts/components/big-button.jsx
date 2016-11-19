
import React from 'react';
import Fa from './fa';

import './big-button.less';

/**
 * Define a page title
 */
export default class BigButton extends React.Component {
    render() {
        return (
            <div className="big-button" onClick={this.props.onClick}>
                <div className="big-button-ico">
                    <Fa icon={this.props.icon} size={4.3} />
                </div>
                <div className="big-button-txt">
                    <h3>{this.props.title}</h3>
                    <div>{this.props.description}</div>
                </div>
            </div>
        );
    }
}

BigButton.propTypes = {
    title: React.PropTypes.string,
    description: React.PropTypes.string,
    icon: React.PropTypes.string,
    onClick: React.PropTypes.func
};
