
import React from 'react';
import { Navbar, Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';
import { LOGIN } from './actions';

/**
 * The home page of the initialization module
 */
export default class Toolbar extends React.Component {

    constructor() {
        super();
    }


    componentDidMount() {
        this.props.app.add(this._onAppChange);
    }

    componentDidUmount() {
        this.props.app.remove(this._onAppChange);
    }

    _onAppChange(action) {
        if (action === LOGIN) {
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
        var loggedin = state.user !== undefined;

        var items;

        // if logged in, show items in the toolbar
        if (loggedin) {
            const search = (
                <div className="header-search">
                    <input type="search" placeholder="Search..."/>
                    <button><i className="fa fa-remove"></i></button>
                </div>
            );
            const workspace = (
                <span className="header-ws">
                    <span className="fa fa-stack">
                        <i className="fa fa-circle fa-stack-2x fa-inverse"></i>
                        <i className="fa fa-globe fa-stack-2x"></i>
                    </span>
                    <span className="ws-text">
                    {'MSH Demo'}
                    </span>
                </span>
            );
            const user = (
                <span className="fa fa-stack">
                    <i className="fa fa-circle fa-stack-2x"></i>
                    <i className="fa fa-user fa-stack-1x fa-inverse"></i>
                </span>
            );

            items = (
                <Navbar.Collapse>
                    <Nav>
                        <NavItem eventKey={1} href="#">{__('Home')}</NavItem>
                        <NavItem eventKey={1} href="#">{__('Reports')}</NavItem>
                        <NavItem eventKey={2} href="#">{__('Administration')}</NavItem>
                    </Nav>
                    <Nav pullRight>
                        <NavItem className="hsmall">
                            {search}
                        </NavItem>
                        <NavDropdown id="ddWs" eventKey={3} title={workspace} pullRight >
                            <MenuItem eventKey="1">{'Message 1'}</MenuItem>
                            <MenuItem eventKey="2">{'Message 2'}</MenuItem>
                            <MenuItem eventKey="4">{'Message 3'}</MenuItem>
                        </NavDropdown>
                        <NavDropdown id="ddUser" eventKey={3} title={user} pullRight >
                            <MenuItem eventKey="1">{__('User profile') + '...'}</MenuItem>
                            <MenuItem eventKey="2">{__('Change password') + '...'}</MenuItem>
                            <MenuItem divider />
                            <MenuItem eventKey="4">{__('Logout')}</MenuItem>
                        </NavDropdown>
                    </Nav>
                </Navbar.Collapse>
            );
        }
        else {
            items = <Nav navbar />;
        }

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
