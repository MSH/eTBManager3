
import React from 'react';
import { Navbar, Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';
import { app } from '../core/app';
import SearchBox from './search-box';
import { hasPerm, logout } from './session';
import { Fa } from '../components';
import { WORKSPACE_CHANGE } from '../core/actions';
import SessionUtils from './session-utils';

import './toolbar.less';


function userMenuSel(key) {
    switch (key) {
        case 'lang': app.dispatch('change-lang');
            break;
        case 'ws': app.dispatch('open-ws-sel');
            break;
        default:
            break;
    }
}


/**
 * The home page of the initialization module
 */
export default class Toolbar extends React.Component {

    constructor() {
        super();
        this._onAppChange = this._onAppChange.bind(this);
    }

    componentDidMount() {
        app.add(this._onAppChange);
    }

    shouldComponentUpdate() {
        return false;
    }

    componentWillUnmount() {
        app.remove(this._onAppChange);
    }

    _onAppChange(action) {
        if (action === WORKSPACE_CHANGE) {
            this.forceUpdate();
        }
        return null;
    }

    cmdLogout() {
        logout().then(() => app.goto('/pub/login'));
    }

    render() {
        var Logo = (
            <a>
                <img src="images/logo2.png" />
            </a>
        );

        const session = app.getState().session;

        const settings = (
            <span className="tb-icon">
                <i className="fa fa-cogs" />
            </span>
        );

        const langName = app.getState().app.languages.find(item => item.id === app.getLang()).name;

        return (
            <Navbar className="toolbar" fixedTop inverse>
                <Navbar.Header>
                    <Navbar.Brand>
                        {Logo}
                    </Navbar.Brand>
                    <Navbar.Toggle />
                </Navbar.Header>
                <Navbar.Collapse>
                    <Nav>
                        <NavItem href={SessionUtils.homeHash()}>{__('home')}</NavItem>
                        {
                            hasPerm('ADMIN') &&
                                <NavDropdown id="dd-admin" eventKey={3} title={__('admin')}>
                                    <MenuItem href="#/sys/admin/tables">{__('admin.tables')}</MenuItem>
                                    <MenuItem href="#/sys/admin/reports">{__('admin.reports')}</MenuItem>
                                    <MenuItem href="#/sys/admin/settings">{__('admin.config')}</MenuItem>
                                </NavDropdown>
                        }
                    </Nav>
                    <Nav pullRight >
                        <NavItem className="tb-user" href="#/sys/home/index">
                            <div className="tb-icon">
                                <i className="fa fa-user fa-inverse" />
                            </div>
                            {session.userName}
                        </NavItem>
                        <NavDropdown id="ddUser" eventKey={3} title={settings} onSelect={userMenuSel} >
                            <MenuItem href="#/sys/user/settings">
                                <div>
                                    <Fa icon="cog" />
                                    {__('session.usersettings')}
                                </div>
                            </MenuItem>
                            <MenuItem href="#/sys/user/changepassword">
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
                            <MenuItem eventKey="4" onClick={this.cmdLogout}>
                                <div>
                                    <Fa icon="sign-out" />
                                    {__('action.logout')}
                                </div>
                            </MenuItem>
                        </NavDropdown>
                    </Nav>
                    <Navbar.Form>
                        <SearchBox />
                    </Navbar.Form>
                </Navbar.Collapse>
            </Navbar>
        );
    }
}
