'use strict';

import React from 'react';
import { Navbar, Nav, NavItem, CollapsibleNav, DropdownButton, MenuItem } from 'react-bootstrap';

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
        )

        var loggedin = this.props.session !== undefined;

        var items;

        // if logged in, show items in the toolbar
        if (loggedin) {
            items = (
            <CollapsibleNav eventKey={100}>
                <Nav navbar>
                    <NavItem eventKey={1} href='#'>{__('Home')}</NavItem>
                    <NavItem eventKey={1} href='#'>{__('Reports')}</NavItem>
                    <NavItem eventKey={2} href='#'>{__('Administration')}</NavItem>
                </Nav>
                <Nav navbar right>
                    <NavItem eventKey={4} href='#'>{__('Language')}</NavItem>
                    <DropdownButton eventKey={3} title={__('User')} pullRight>
                        <MenuItem eventKey='1'>{__('User profile')}...</MenuItem>
                        <MenuItem eventKey='2'>{__('Change password')}...</MenuItem>
                        <MenuItem eventKey='3'>{__('Change workspace')}...</MenuItem>
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
                    <NavItem>First time</NavItem>
                </Nav>
            );
        }

        return (
            <Navbar brand={Logo} fixedTop toggleNavKey={100}>
                {items}
            </Navbar>
        );
    }
}