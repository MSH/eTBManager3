import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { Profile, Fluidbar } from '../../../components/index';


export default class PatientPanel extends React.Component {

	render() {
		const patient = this.props.patient;
		if (!patient) {
			return null;
		}

		const type = patient.gender === 'MALE' ? 'male' : 'female';

		return (
			<Fluidbar>
				<Grid>
					<Row>
						<Col md={12}>
							<div className="margin-2x">
								<Profile title={patient.name}
									subtitle={this.props.recordNumber}
									type={type}
									size="large"
									/>
							</div>
						</Col>
					</Row>
					{this.props.content}
				</Grid>
			</Fluidbar>
			);
	}
}

PatientPanel.propTypes = {
	patient: React.PropTypes.object,
	recordNumber: React.PropTypes.string,
	content: React.PropTypes.node
};
