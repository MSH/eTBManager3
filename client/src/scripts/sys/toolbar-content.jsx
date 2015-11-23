
import React from 'react';
import { Navbar, Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';

/** The reference of the application in the module */
let app;


// logs the user out of the system
function logout() {
    app.session.logout()
    .then(() => app.goto('/pub/login'));
}

function gotoHome() {
    app.goto('/sys/home/index');
}

function gotoReports() {
    app.goto('/sys/reports/index');
}

function gotoAdmin() {
    app.goto('/sys/admin/index');
}

/**
 * Export a default method to be sent to the toolbar and executed by she to get the
 * content to be displayed by the toolbar.
 *
 * This implmenetation was done in order to separate custom implementation of the toolbar.
 * Because the toolbar is even displayed when user is not authenticated, it will promove a
 * better code splitting
 *
 * @param  {[type]} app The singleton application object
 * @return {[type]}     The content of the toolbar
 */
export default function(application) {
    app = application;
	const session = app.getState().session;

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

    // the user data
    const user = (
        <span className="fa fa-stack">
            <i className="fa fa-circle fa-stack-2x"></i>
            <i className="fa fa-user fa-stack-1x fa-inverse"></i>
        </span>
    );

	return (
        <Navbar.Collapse>
            <Nav>
                <NavItem eventKey={1} onClick={gotoHome}>{'Home'}</NavItem>
                {
                    app.session.hasPerm('REPORTS') &&
                    <NavItem eventKey={1} onCLick={gotoReports}>{'Reports'}</NavItem>
                }
                {
                    app.session.hasPerm('ADMIN') &&
                    <NavItem eventKey={2} onClick={gotoAdmin}>{'Administration'}</NavItem>
                }
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
                    <MenuItem eventKey="1">{'User profile...'}</MenuItem>
                    <MenuItem eventKey="2">{'Change password...'}</MenuItem>
                    <MenuItem divider />
                    <MenuItem eventKey="4" onClick={logout}>{'Logout'}</MenuItem>
                </NavDropdown>
            </Nav>
        </Navbar.Collapse>
	);
}

