
import React from 'react';
import { Grid, Row, Col } from 'react-bootstrap';
import { Profile, Fluidbar } from '../../../components/index';
import { app } from '../../../core/app';
import SearchPatient from './new-searchpatient';
import NotifForm from './new-notifform';


export default class NewNotif extends React.Component {

	constructor(props) {
		super(props);
		this.onSelectPatient = this.onSelectPatient.bind(this);

//		this.state = { };
		this.state = { patient: { name: 'Juan Suarez', age: 44, gender: 'MALE' } };
	}

	/**
	 * Called when user clicks on a patient to generate a new notification
	 * @param  {[type]} pat [description]
	 * @return {[type]}     [description]
	 */
	onSelectPatient(pat) {
		// prepare data model
		this.setState({ patient: {
			name: pat.name,
			age: pat.age,
			patientId: pat.id,
			birthDate: pat.birthDate,
			gender: pat.gender
		} });
	}

	/**
	 * Render
	 * @return {React.Component} Component to display
	 */
	render() {
		const session = app.getState().session;
		const lst = [];
		const keys = Object.keys(session.adminUnit);
		keys.forEach((k, index) => {
				lst.push(<a key={index} href="/sys/home/admunit">{session.adminUnit[k].name}</a>);
				if (index < keys.length - 1) {
					lst.push(<span key={'s' + index}>{', '}</span>);
				}
			});

		const subtitle = (
			<div>
				{lst}
				<br/>
				<a>{session.workspaceName}</a>
			</div>
		);

		return (
			<div>
				<Fluidbar>
					<Grid>
						<Row>
							<Col md={12}>
								<div className="margin-2x">
									<Profile title={session.unitName}
										subtitle={subtitle}
										type="tbunit"
										size="large"
										/>
								</div>
							</Col>
						</Row>
					</Grid>
				</Fluidbar>
				{
					this.state.patient ?
						<NotifForm patient={this.state.patient} /> :
						<SearchPatient onSelect={this.onSelectPatient} />
				}
			</div>
			);
	}
}
