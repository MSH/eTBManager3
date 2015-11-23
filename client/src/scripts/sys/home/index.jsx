
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import Profile from '../../components/profile';
import Card from '../../components/card';
import Fluidbar from '../../components/fluidbar';


/**
 * The page controller of the public module
 */
export default class Home extends React.Component {

	render() {
		const app = this.props.app;
		const state = app.getState();

		const subtitle = (
			<div><a href="#">{state.session.unitName}</a>
			<br/>
			<a href="#">{'Rio de Janeiro, RJ'}</a>
			<br/>
			<a href="#">{state.session.workspaceName}</a>
			</div>
		);

		const unitName = 'Centro de Referência Professor Hélio Fraga';

		return (
			<div>
				<Fluidbar>
					<Grid>
						<Row>
							<Col md={12}>
								<div className="margin-2x">
									<Profile title={state.session.userName} subtitle={subtitle} fa="user" size="large"/>
								</div>
							</Col>
						</Row>
					</Grid>
				</Fluidbar>
				<Grid>
					<Row>
						<Col md={8} mdOffset={2}>
						<Card>
							<Profile title={unitName} subtitle="Rio de Janeiro, RJ" fa="hospital-o" size="large"/>
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

Home.propTypes = {
	app: React.PropTypes.object
};
