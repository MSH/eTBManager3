
import React from 'react';
import SearchPatient from './new-searchpatient';
import NotifForm from './new-notifform';
import UnitPanel from '../commons/unit-panel';


export default class NewNotif extends React.Component {

	constructor(props) {
		super(props);
		this.onSelectPatient = this.onSelectPatient.bind(this);

		this.state = { };
//		this.state = { patient: { name: 'Juan Suarez', age: 44, gender: 'MALE' } };
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
		return (
			<div>
				<UnitPanel />
				{
					this.state.patient ?
						<NotifForm patient={this.state.patient} /> :
						<SearchPatient onSelect={this.onSelectPatient} />
				}
			</div>
			);
	}
}
