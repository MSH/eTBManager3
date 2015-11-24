
import React from 'react';
import { Grid, Row, Col, Nav, NavItem } from 'react-bootstrap';
import { Fluidbar } from '../../components/index';


/**
 * The page controller of the public module
 */
export default class Settings extends React.Component {

	render() {
		let style = {
			minHeight: '550px',
			marginLeft: '-15px'
		};

		return (
			<div>
				<Fluidbar>
					<h3>{'Administration - Reports'}</h3>
				</Fluidbar>
				<Grid fluid>
					<Row>
						<Col md={3}>
							<div style={style}>
								<Nav>
									<NavItem>{'On-line users'}</NavItem>
									<NavItem>{'User session history'}</NavItem>
									<NavItem>{'Command history'}</NavItem>
									<NavItem>{'Command statistics'}</NavItem>
									<NavItem>{'Error log'}</NavItem>
								</Nav>
							</div>
						</Col>
					</Row>
				</Grid>
			</div>

			);
	}
}
