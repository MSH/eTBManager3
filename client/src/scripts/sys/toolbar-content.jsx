
import React from 'react';
import { Navbar, Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';
import { app } from '../core/app';
import { hasPerm, logout } from '../core/act-session';

// logs the user out of the system
function cmdLogout() {
    logout()
        .then(() => app.goto('/pub/login'));
}

function gotoHome() {
    app.goto('/sys/home/index');
}

function gotoReports() {
    app.goto('/sys/reports/index');
}

function adminClick(evt, key) {
    switch (key) {
        case 31: return app.goto('/sys/admin/tables');
        case 32: return app.goto('/sys/admin/reports');
        case 33: return app.goto('/sys/admin/settings');
        default: return -1;
    }
}

/**
 * Export a default method to be sent to the toolbar and executed by she to get the
 * content to be displayed by the toolbar.
 *
 * This implmenetation was done in order to separate custom implementation of the toolbar.
 * Because the toolbar is even displayed when user is not authenticated, it will promove a
 * better code splitting
 *
 * @return {[type]}     The content of the toolbar
 */
export default function() {
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
            <span className="ws-text visible-lg-inline visible-md-inline visible-xs-inline">
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
                <NavItem eventKey={1} onClick={gotoHome}>{__('home')}</NavItem>
                {
                    hasPerm('REPORTS') &&
                    <NavItem eventKey={2} onCLick={gotoReports}>{__('reports')}</NavItem>
                }
                {
                    hasPerm('ADMIN') &&
                    <NavDropdown id="dd-admin" eventKey={3} title={__('admin')}>
                        <MenuItem eventKey={31} onSelect={adminClick}>{'Tables'}</MenuItem>
                        <MenuItem eventKey={32} onSelect={adminClick}>{'Reports'}</MenuItem>
                        <MenuItem eventKey={33} onSelect={adminClick}>{'Settings'}</MenuItem>
                    </NavDropdown>
                }
            </Nav>
            <Nav pullRight>
                <NavItem className="hsmall">
                    {search}
                </NavItem>
                <NavDropdown id="ddWs" eventKey={3} title={workspace} className="nav-item-icon">
                {
                    session.workspaces.map(ws =>
                        <MenuItem key={ws.id} eventKey={ws.id}>
                            {ws.name}
                        </MenuItem>)
                }
                </NavDropdown>
                <NavDropdown id="ddUser" eventKey={3} title={user} className="nav-item-icon">
                    <MenuItem eventKey="1">{'User profile...'}</MenuItem>
                    <MenuItem eventKey="2">{__('changepwd') + '...'}</MenuItem>
                    <MenuItem divider />
                    <MenuItem eventKey="4" onClick={cmdLogout}>{__('action.logout')}</MenuItem>
                </NavDropdown>
            </Nav>
        </Navbar.Collapse>
	);
}

