
import React from 'react';
import { Navbar, Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';
import { app } from '../core/app';
import SearchBox from './search-box';
import { hasPerm, logout } from './session';
import { Fa } from '../components';

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

function adminClick(key) {
    switch (key) {
        case 31: return app.goto('/sys/admin/tables');
        case 32: return app.goto('/sys/admin/reports');
        case 33: return app.goto('/sys/admin/settings');
        default: return -1;
    }
}


function userMenuSel(key) {
    switch (key) {
        case 'prof': app.goto('/sys/usersettings');
            break;
        case 'lang': app.dispatch('change-lang');
            break;
        case 'ws': app.dispatch('open-ws-sel');
            break;
        default:
            break;
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
export default function(session) {
    // the workspace menu
    // const workspace = (
    //     <span className="header-ws">
    //         <span className="fa fa-stack">
    //             <i className="fa fa-circle fa-stack-2x fa-inverse"></i>
    //             <i className="fa fa-globe fa-stack-2x"></i>
    //         </span>
    //         <span className="ws-text visible-lg-inline visible-md-inline visible-xs-inline">
    //         {session.workspaceName}
    //         </span>
    //     </span>
    // );

    // the user data
    const user = (
        <span className="fa fa-stack">
            <i className="fa fa-circle fa-stack-2x"></i>
            <i className="fa fa-user fa-stack-1x fa-inverse"></i>
        </span>
    );

    const langName = app.getState().app.languages.find(item => item.id === app.getLang()).name;

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
                        <MenuItem eventKey={31} onSelect={adminClick}>{__('admin.tables')}</MenuItem>
                        <MenuItem eventKey={32} onSelect={adminClick}>{__('admin.reports')}</MenuItem>
                        <MenuItem eventKey={33} onSelect={adminClick}>{__('admin.config')}</MenuItem>
                    </NavDropdown>
                }
            </Nav>
            <Nav pullRight >
                <NavDropdown id="ddUser" eventKey={3} title={user} className="nav-item-icon" onSelect={userMenuSel} >
                    <MenuItem eventKey="prof">
                        <div>
                            <Fa icon="cog" />
                            {__('usersettings')}
                        </div>
                    </MenuItem>
                    <MenuItem eventKey="pwd">
                        <div>
                            <Fa icon="key" />
                            {__('changepwd')}
                        </div>
                    </MenuItem>
                    <MenuItem divider />
                    <MenuItem eventKey="lang">
                        <div>
                        {__('changelang')}
                        <div className="text-muted">
                            <Fa icon="angle-right" />
                            {langName}
                        </div>
                        </div>
                    </MenuItem>
                    <MenuItem divider />
                    <MenuItem eventKey="ws">
                        <div>
                        {__('changews')}
                        <div className="text-muted">
                            <Fa icon="globe" />
                            {session.workspaceName}
                        </div>
                        </div>
                    </MenuItem>
                    <MenuItem divider />
                    <MenuItem eventKey="4" onClick={cmdLogout}>
                        <div>
                            <Fa icon="sign-out" />
                            {__('action.logout')}
                        </div>
                    </MenuItem>
                </NavDropdown>
            </Nav>
            <Navbar.Form pullRight>
                <SearchBox />
            </Navbar.Form>
        </Navbar.Collapse>
	);
}

