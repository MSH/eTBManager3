import React from 'react';
import { Collapse } from 'react-bootstrap';


/**
 * A simple row component with support for additional content that is
 * expanded or collapsed on users click
 */
export default class Expandable extends React.Component {

    constructor(props) {
        super(props);
        this.onClick = this.onClick.bind(this);
        this.state = { expanded: false };
    }

    /**
     * Called when user clicks on the row
     * @param  {[type]} evt [description]
     * @return {[type]}     [description]
     */
    onClick(evt) {
        if (!evt.isDefaultPrevented()) {
            this.setState({ expanded: !this.state.expanded });
        }
    }

    render() {
        const props = Object.assign({}, this.props);
        delete props.collapsable;

        return (
            <div onClick={this.onClick} className={this.props.className}>
                {this.props.children}
                <Collapse in={this.state.expanded}>
                    <div className={this.props.expandClassName}>
                    {this.state.expanded ? this.props.onExpandRender(this.props.value) : null}
                    </div>
                </Collapse>
            </div>
        );
    }
}


Expandable.propTypes = {
    children: React.PropTypes.any,
    onExpandRender: React.PropTypes.func,
    value: React.PropTypes.object,
    className: React.PropTypes.string,
    expandClassName: React.PropTypes.string
};

Expandable.defaultProps = {
    collapse: true
};
