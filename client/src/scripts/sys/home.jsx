
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';


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

		const profStyle = {
			paddingTop: "20px",
			paddingBottom: "20px"
		};

		return (
			<div style={style}>
				<Grid>
					<Row>
						<Col md={12}>
							<div className="profile margin-2x">
								<div className="profile-image">
									<span className="fa-stack profile-image">
										<i className="fa fa-circle fa-stack-2x fa-inverse" />
										<i className="fa fa-user fa-stack-1x profile-front" />
									</span>
								</div>
								<div className="profile-title">{state.session.userName}</div>
								<div>{state.session.unitName}</div>
								<div>{'Rio de Janeiro, RJ'}</div>
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
