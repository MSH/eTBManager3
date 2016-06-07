
import React from 'react';
import { Navbar, Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';
import { app } from '../core/app';
import SearchBox from './search-box';
import { hasPerm, logout } from './session';
import { Fa } from '../components';
import { WORKSPACE_CHANGE, LOGOUT } from '../core/actions';


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

    componentWillUmount() {
        app.remove(this._onAppChange);
    }

    _onAppChange(action, data) {
        if (action === LOGOUT) {
            this.setState({ toolbarContent: null, session: null });
        }

        if (action === WORKSPACE_CHANGE) {
            this.setState({ session: data.session });
        }
        return null;
    }

    cmdLogout() {
        logout().then(() => app.goto('/pub/login'));
    }

    render() {
        var Logo = (
            <a>
                <img src="images/logo2.png"></img>
            </a>
        );

        // the user data
        const user = (
            <span className="fa fa-stack">
                <i className="fa fa-circle fa-stack-2x"></i>
                <i className="fa fa-user fa-stack-1x fa-inverse"></i>
            </span>
        );

        const langName = app.getState().app.languages.find(item => item.id === app.getLang()).name;

        const session = app.getState().session;

        return (
            <Navbar className="header" fixedTop inverse>
                <Navbar.Header>
                    <Navbar.Brand>
                        {Logo}
                    </Navbar.Brand>
                    <Navbar.Toggle />
                </Navbar.Header>
                <Navbar.Collapse>
                    <Nav>
                        <NavItem href="#/sys/home/index">{__('home')}</NavItem>
                        {
                            hasPerm('REPORTS') &&
                            <NavItem href="#/sys/reports/index">{__('reports')}</NavItem>
                        }
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
                        <NavDropdown id="ddUser" eventKey={3} title={user} className="nav-item-icon" onSelect={userMenuSel} >
                            <MenuItem href="#/sys/usersettings">
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
                            <MenuItem eventKey="4" onClick={this.cmdLogout}>
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
            </Navbar>
        );
    }
}
