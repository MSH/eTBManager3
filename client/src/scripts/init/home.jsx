'use strict';

import React from 'react';
import { Navbar, Nav, NavItem, DropdownButton, MenuItem } from 'react-bootstrap';


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
			      <NavItem eventKey={1} href='#'>Home</NavItem>
			      <NavItem eventKey={1} href='#'>Reports</NavItem>
			      <NavItem eventKey={2} href='#'>Administration</NavItem>
			    </Nav>
			    <Nav right>
			      <NavItem eventKey={4} href='#'>Language</NavItem>
			      <DropdownButton eventKey={3} title={'User'}>
			        <MenuItem eventKey='1'>Profile...</MenuItem>
			        <MenuItem eventKey='2'>Change password...</MenuItem>
			        <MenuItem eventKey='3'>Workspace...</MenuItem>
			        <MenuItem divider />
			        <MenuItem eventKey='4'>Logout</MenuItem>
			      </DropdownButton>
			    </Nav>
			</Navbar>
			);
	}
}