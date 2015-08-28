'use strict';

import React from 'react';
import { Navbar, Nav, NavItem, DropdownButton, MenuItem } from 'react-bootstrap';


/**
 * The home page of the initialization module
 */
export default class Home extends React.Component {

	render() {
		var Logo = (
			<a>
				<img src={'images/logo.png'}></img>
				<label>e-TB Manager</label>
			</a>
			)

		return (
			<Navbar brand={Logo} fixedTop>
			    <Nav>
			      <NavItem eventKey={1} href='#'>{__('Home')}</NavItem>
			      <NavItem eventKey={1} href='#'>{__('Reports')}</NavItem>
			      <NavItem eventKey={2} href='#'>{__('Administration')}</NavItem>
			    </Nav>
			    <Nav right>
			      <NavItem eventKey={4} href='#'>{__('Language')}</NavItem>
			      <DropdownButton eventKey={3} title={__('User')}>
			        <MenuItem eventKey='1'>{__('User profile')}...</MenuItem>
			        <MenuItem eventKey='2'>{__('Change password')}...</MenuItem>
			        <MenuItem eventKey='3'>{__('Change workspace')}...</MenuItem>
			        <MenuItem divider />
			        <MenuItem eventKey='4'>{__('Logout')}</MenuItem>
			      </DropdownButton>
			    </Nav>
			</Navbar>
			);
	}
}