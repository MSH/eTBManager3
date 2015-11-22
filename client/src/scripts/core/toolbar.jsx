
import React from 'react';
import { Navbar, Nav } from 'react-bootstrap';
import { TB_SET } from './actions';

/**
 * The home page of the initialization module
 */
export default class Toolbar extends React.Component {

    constructor() {
        super();
        this._onAppChange = this._onAppChange.bind(this);
    }

    componentDidMount() {
        this.props.app.add(this._onAppChange);
    }

    componentDidUmount() {
        this.props.app.remove(this._onAppChange);
    }

    _onAppChange(action) {
        if (action === TB_SET) {
            this.forceUpdate();
        }
    }

    render() {
        var Logo = (
            <a>
                <img src="images/logo2.png"></img>
            </a>
        );

        var state = this.props.app.getState();

        const items = (state.toolbarContent && state.toolbarContent(this.props.app)) || <Nav navbar />;

        return (
            <Navbar className="header" fixedTop inverse>
                <Navbar.Header>
                    <Navbar.Brand>
                        {Logo}
                    </Navbar.Brand>
                    <Navbar.Toggle />
                </Navbar.Header>
                {items}
            </Navbar>
        );
    }
}


Toolbar.propTypes = {
    app: React.PropTypes.object
};
