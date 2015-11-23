
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import Profile from '../../components/profile';
import Card from '../../components/card';
import Fluidbar from '../../components/fluidbar';


/**
 * The page controller of the public module
 */
export default class Index extends React.Component {

	render() {
		const unitName = 'Centro de Referência Professor Hélio Fraga';

		return (
			<div>
				<Fluidbar>
					<h3>{'Administration'}</h3>
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

Index.propTypes = {
	app: React.PropTypes.object
};
