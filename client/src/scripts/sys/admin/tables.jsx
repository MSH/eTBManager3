
import React from 'react';
import { Grid, Row, Col, Nav, NavItem } from 'react-bootstrap';
import { Profile, Card, Fluidbar } from '../../components/index';


/**
 * The page controller of the public module
 */
export default class Tables extends React.Component {

	render() {
		const unitName = 'Centro de Referência Professor Hélio Fraga';

		let style = {
			minHeight: '550px',
			marginLeft: '-15px'
		};

		return (
			<div>
				<Fluidbar>
					<h3>{'Administration'}</h3>
				</Fluidbar>
				<Grid fluid>
					<Row>
						<Col md={3}>
							<div style={style}>
								<Nav>
									<NavItem>{'Administrative Units'}</NavItem>
									<NavItem>{'TB units'}</NavItem>
									<NavItem>{'Laboratories'}</NavItem>
									<NavItem>{'Medicine sources'}</NavItem>
									<NavItem>{'Medicine generic names'}</NavItem>
									<NavItem>{'Commodities & Medicines'}</NavItem>
									<NavItem>{'Medicine Regimens'}</NavItem>
									<hr/>
									<NavItem>{'Tags'}</NavItem>
									<NavItem>{'Weekly frequency'}</NavItem>
									<NavItem>{'Age ranges'}</NavItem>
									<hr/>
									<NavItem>{'Users'}</NavItem>
									<NavItem>{'User groups'}</NavItem>
								</Nav>
							</div>
						</Col>
						<Col md={9}>
							<Card>
								<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="plus" size="large"/>
								<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="medium"/>
								<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="small"/>
							</Card>
						</Col>
					</Row>
				</Grid>
			</div>

			);
	}
}

Tables.propTypes = {
	app: React.PropTypes.object
};
