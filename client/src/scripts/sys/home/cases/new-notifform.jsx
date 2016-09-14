import React from 'react';
import CRUD from '../../../commons/crud';
import { server } from '../../../commons/server';
import { Grid, Col, Row } from 'react-bootstrap';
import { RemoteFormDialog } from '../../../components';
import { app } from '../../../core/app';

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
		const req = { diagnosisType: this.props.diagnosisType, caseClassification: this.props.classification };
		return server.post('/api/tbl/case/initform', req).then(res => {
			res.doc.patient = this.props.patient;
			return res;
		});
	}

	save(doc) {
		const req = { doc: doc };

		req.unitId = this.props.tbunit.id;

		return crud.create(req).then(id => {
			app.goto('/sys/home/cases/details?id=' + id);
		});
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
