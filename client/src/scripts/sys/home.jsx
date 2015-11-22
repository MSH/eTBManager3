
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import Profile from '../components/profile';


/**
 * The page controller of the public module
 */
export default class Home extends React.Component {

	render() {
		const app = this.props.app;
		const state = app.getState();

		const style = {
			backgroundColor: '#ffffff'
		};

		const subtitle = (
			<div>{state.session.unitName}
			<br/>
			{'Rio de Janeiro, RJ'}</div>
		);

		return (
			<div style={style}>
				<Grid>
					<Row>
						<Col md={12}>
							<div className="margin-2x">
								<Profile title={state.session.userName} subtitle={subtitle} fa="user" />
							</div>
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
