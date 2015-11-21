
import React from 'react';
import { Navbar, Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';
import { AUTHENTICATED, LOGOUT } from './actions';
import Server from '../commons/server';

/**
 * The home page of the initialization module
 */
export default class Toolbar extends React.Component {

    constructor() {
        super();
        this._onAppChange = this._onAppChange.bind(this);
        this.logout = this.logout.bind(this);
    }


    componentDidMount() {
        this.props.app.add(this._onAppChange);
    }

    componentDidUmount() {
        this.props.app.remove(this._onAppChange);
    }

    _onAppChange(action) {
        if (action === AUTHENTICATED || action === LOGOUT) {
            this.forceUpdate();
        }
    }

    // logs the user out of the system
    logout() {
        // get current authentication token
        const autk = window.app.getCookie('autk');

        const self = this;
        // inform server to register logout of the authentication token
        if (autk) {
            Server.get('/api/auth/logout?tk=' + autk)
            .then(() => {
                // clear authentication token in the cookies
                window.app.setCookie('autk', null);

                const app = self.props.app;

                // inform the system about the logout
                app.dispatch(LOGOUT, { session: null });
                // goes to the login page
                self.props.app.goto('/pub/login');
            });
        }
    }

    render() {
        var Logo = (
            <a>
                <img src="images/logo2.png"></img>
            </a>
        );

        var state = this.props.app.getState();
        const session = state.session;
        var loggedin = state.session !== undefined;

        var items;

        console.log(state);

        // if logged in, show items in the toolbar
        if (loggedin) {
            // the input search key
            const search = (
                <div className="header-search">
                    <input type="search" placeholder="Search..."/>
                    <button><i className="fa fa-remove"></i></button>
                </div>
            );

            // the workspace menu
            const workspace = (
                <span className="header-ws">
                    <span className="fa fa-stack">
                        <i className="fa fa-circle fa-stack-2x fa-inverse"></i>
                        <i className="fa fa-globe fa-stack-2x"></i>
                    </span>
                    <span className="ws-text">
                    {session.workspaceName}
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
                        <NavDropdown id="ddWs" eventKey={3} title={workspace} className="nav-item-icon">
                        {
                            session.workspaces.map(ws => <MenuItem key={ws.id} eventKey={ws.id}>{ws.name}</MenuItem>)
                        }
                        </NavDropdown>
                        <NavDropdown id="ddUser" eventKey={3} title={user} className="nav-item-icon">
                            <MenuItem eventKey="1">{__('User profile') + '...'}</MenuItem>
                            <MenuItem eventKey="2">{__('Change password') + '...'}</MenuItem>
                            <MenuItem divider />
                            <MenuItem eventKey="4" onClick={this.logout}>{__('Logout')}</MenuItem>
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
