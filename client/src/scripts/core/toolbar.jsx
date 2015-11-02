'use strict';

import React from 'react';
import { Navbar, Nav, NavItem, CollapsibleNav, DropdownButton, MenuItem, Badge } from 'react-bootstrap';

/**
 * The home page of the initialization module
 */
export default class Toolbar extends React.Component {

    constructor() {
        super();
    }


    componentDidMount() {
    }

    componentDidUmount() {
    }


    render() {
        var Logo = (
            <a>
                <img src={'images/logo2.png'}></img>
            </a>
        );

        var loggedin = this.props.session !== undefined;

        var items;

        // if logged in, show items in the toolbar
        if (loggedin) {
            let search = (
                <div className='header-search'>
                    <input type='search' placeholder='Search...'/>
                    <button><i className='fa fa-remove'></i></button>
                </div>
            );
            let workspace = (
                <span className='header-ws'>
                    <span className='fa fa-stack'>
                        <i className='fa fa-circle fa-stack-2x fa-inverse'></i>
                        <i className='fa fa-globe fa-stack-2x'></i>
                    </span>
                    <span className='ws-text'>
                    MSH Demo
                    </span>
                </span>
            );
            let user = (
                <span className='fa fa-stack'>
                    <i className='fa fa-circle fa-stack-2x'></i>
                    <i className='fa fa-user fa-stack-1x fa-inverse'></i>
                </span>
            );

            items = (
                <CollapsibleNav eventKey={100}>
                    <Nav navbar>
                        <NavItem eventKey={1} href='#'>{__('Home')}</NavItem>
                        <NavItem eventKey={1} href='#'>{__('Reports')}</NavItem>
                        <NavItem eventKey={2} href='#'>{__('Administration')}</NavItem>
                    </Nav>
                    <Nav navbar right>
                        <NavItem className='hsmall'>
                            {search}
                        </NavItem>
                        <DropdownButton eventKey={3} title={workspace} pullRight className='hsmall'>
                            <MenuItem eventKey='1'>Message 1</MenuItem>
                            <MenuItem eventKey='2'>Message 2</MenuItem>
                            <MenuItem eventKey='4'>Message 3</MenuItem>
                        </DropdownButton>
                        <DropdownButton eventKey={3} title={user} pullRight className='hsmall'>
                            <MenuItem eventKey='1'>{__('User profile')}...</MenuItem>
                            <MenuItem eventKey='2'>{__('Change password')}...</MenuItem>
                            <MenuItem divider />
                            <MenuItem eventKey='4'>{__('Logout')}</MenuItem>
                        </DropdownButton>
                    </Nav>
                </CollapsibleNav>
            );
        }
        else {
            items = (
                <Nav navbar right>
                </Nav>
            );
        }

        return (
            <Navbar className='header' brand={Logo} fixedTop inverse toggleNavKey={100}>
                {items}
            </Navbar>
        );
    }
}