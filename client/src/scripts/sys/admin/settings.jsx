
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
					<h3>{'Administration - Settings'}</h3>
				</Fluidbar>
				<Grid fluid>
					<Row>
						<Col md={3}>
							<div style={style}>
								<Nav>
									<NavItem>{'Workspaces'}</NavItem>
									<NavItem>{'Configuration'}</NavItem>
									<NavItem>{'Workspace settings'}</NavItem>
								</Nav>
							</div>
						</Col>
					</Row>
				</Grid>
			</div>

			);
	}
}
