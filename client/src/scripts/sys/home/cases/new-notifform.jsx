import React from 'react';
import CRUD from '../../../commons/crud';
import { server } from '../../../commons/server';
import { Grid, Col, Row } from 'react-bootstrap';
import { RemoteFormDialog } from '../../../components';

const crud = new CRUD('case');

/**
 * Component that displays and handle notification form
 */
export default class NotifForm extends React.Component {

	constructor(props) {
		super(props);
		this.getRemoteForm = this.getRemoteForm.bind(this);
		this.save = this.save.bind(this);
	}

	componentWillMount() {

	}

	getRemoteForm() {
		return server.get('/api/tbl/case/initform').then(res => {
			// set patient values
			res.doc.patient.id = this.props.patient.id;
			res.doc.patient.name = this.props.patient.name;
			res.doc.patient.motherName = this.props.patient.motherName;
			res.doc.patient.birthDate = this.props.patient.birthDate;
			res.doc.patient.gender = this.props.patient.gender;

			// set tbcase values
			res.doc.tbcase.notificationUnit = this.props.tbunit.id;
			res.doc.tbcase.state = 'NOT_ONTREATMENT';
			res.doc.tbcase.diagnosisType = this.props.diagnosisType;
			res.doc.tbcase.classification = this.props.classification;

			return res;
		});
	}

	save(doc) {
		const req = { doc: doc };
		return crud.create(req);
	}

	render() {
		return (
			<Grid fluid className="mtop-2x">
				<Row>
					<Col mdOffset={2} md={9}>
						<RemoteFormDialog
							wrapType="card"
							remotePath={this.getRemoteForm}
							onCancel={this.props.onCancel}
							onConfirm={this.save} />
					</Col>
				</Row>
			</Grid>
			);
	}
}

NotifForm.propTypes = {
	patient: React.PropTypes.object,
	onCancel: React.PropTypes.func,
	diagnosisType: React.PropTypes.string,
	classification: React.PropTypes.string,
	tbunit: React.PropTypes.object
};
