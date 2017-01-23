
import React from 'react';
import { Collapse } from 'react-bootstrap';
import Card from './card';

/**
 * A simple card component with support for additional content that is
 * expanded or collapsed on users click
 */
export default class CollapseCard extends React.Component {

    constructor(props) {
        super(props);
        this.onClick = this.onClick.bind(this);
        this.state = { collapse: this.props.collapse };
    }

    /**
     * Called when user clicks on the card
     * @param  {[type]} evt [description]
     * @return {[type]}     [description]
     */
    onClick(evt) {
        if (!evt.isPropagationStopped()) {
            this.setState({ collapse: !this.state.collapse });
        }
    }

    render() {
        const props = Object.assign({}, this.props);
        delete props.collapsable;

        return (
            <Card {...props} onClick={this.onClick} className="collapse-card">
                <div>
                    {this.props.children}
                    <Collapse in={!this.state.collapse}>
                        {this.props.collapsable}
                    </Collapse>
                </div>
            </Card>
        );
    }
}


CollapseCard.propTypes = {
    // the title of the card
    title: React.PropTypes.string,
    // the header of the card (if title is not informed)
    header: React.PropTypes.element,
    padding: React.PropTypes.oneOf(['none', 'small', 'default']),
    children: React.PropTypes.any,
    style: React.PropTypes.object,
    onClick: React.PropTypes.func,
    collapsable: React.PropTypes.any,
    collapse: React.PropTypes.bool
};

CollapseCard.defaultProps = {
    collapse: true
};
