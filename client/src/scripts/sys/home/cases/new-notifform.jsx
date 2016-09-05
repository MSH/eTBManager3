import React from 'react';
import CRUD from '../../../commons/crud';
import { Grid, Col, Row } from 'react-bootstrap';
import { RemoteFormDialog } from '../../../components';

const crud = new CRUD('case');

/**
 * Component that displays and handle notification form
 */
export default class NotifForm extends React.Component {

	constructor(props) {
		super(props);
		this.setValues = this.setValues.bind(this);
		this.save = this.save.bind(this);
	}

	componentWillMount() {

	}

	setValues(doc) {
		if (!doc.patient) {
			doc.patient = {};
		}

		doc.notificationUnitId = this.props.tbunit.id;
		doc.name = this.props.patient.name;
		doc.birthDate = this.props.patient.birthDate;
		doc.classification = this.props.classification;
		doc.classification = this.props.diagnosisType;

		return doc;
	}

	save(doc) {
		return crud.create(doc);
	}

	render() {
		return (
			<Grid fluid className="mtop-2x">
				<Row>
					<Col mdOffset={2} md={9}>
						<RemoteFormDialog
							wrapType="card"
							remotePath={'/api/tbl/case/initform'}
							onCancel={this.props.onCancel}
							onConfirm={this.save}
							afterResolve={this.setValues} />
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
