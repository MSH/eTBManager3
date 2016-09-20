
import React from 'react';
import SearchPatient from './new-searchpatient';
import NotifForm from './new-notifform';
import { server } from '../../../commons/server';
import { Grid, Row, Col } from 'react-bootstrap';
import { Profile, Fluidbar } from '../../../components/index';
import { app } from '../../../core/app';
import SessionUtils from '../../session-utils';


export default class NewNotif extends React.Component {

	constructor(props) {
		super(props);
		this.onSelectPatient = this.onSelectPatient.bind(this);
		this.onCancel = this.onCancel.bind(this);

		this.state = { };
	}

	componentWillMount() {
		const id = this.props.route.queryParam('id');
		const self = this;

		// get data of the notification unit
		server.get('/api/tbl/unit/' + id).then(res => self.setState({ unit: res }));
	}

	/**
	 * Called when user clicks on a patient to generate a new notification
	 * @param  {[type]} pat [description]
	 * @return {[type]}     [description]
	 */
	onSelectPatient(data) {
		// prepare data model
		this.setState({
			patient: data.patient ? data.patient : {}
		});
	}

	onCancel() {
		app.goto('/sys/home/unit/cases?id=' + this.state.unit.id);
	}

	/**
	 * Render
	 * @return {React.Component} Component to display
	 */
	render() {
		if (!this.state.unit) {
			return null;
		}

		const unit = this.state.unit;
		const diag = this.props.route.queryParam('diag');
		const cla = this.props.route.queryParam('cla');

		return (
			<div>
				<Fluidbar>
					<Grid>
						<Row>
							<Col md={12}>
								<div className="margin-2x">
									<Profile title={unit.name}
										subtitle={SessionUtils.adminUnitLink(unit.address.adminUnit, true, true)}
										type={'tbunit'}
										size="large"
										/>
								</div>
							</Col>
						</Row>
					</Grid>
				</Fluidbar>
				{
					this.state.patient ?
						<NotifForm
							onCancel={this.onCancel}
							patient={this.state.patient}
							diagnosisType={diag}
							classification={cla}
							tbunit={this.state.unit} /> :
						<SearchPatient onSelect={this.onSelectPatient} onCancel={this.onCancel} />
				}
			</div>
			);
	}
}

NewNotif.propTypes = {
	route: React.PropTypes.object
};
