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
		doc.notificationUnitId = this.props.tbunit.id;
		doc.state = 'NOT_ONTREATMENT';
		doc.patient = this.props.patient;

		return doc;
	}

	save(doc) {
		const req = { doc: doc };
		return crud.create(req);
	}

	render() {
		// TODOMS: CRIAR UMA FUNÇÃO PARA PASSAR COMO REMOTEPATH E NELA EU VOU SETAR OS VALORES DA TELA.
		return (
			<Grid fluid className="mtop-2x">
				<Row>
					<Col mdOffset={2} md={9}>
						<RemoteFormDialog
							wrapType="card"
							remotePath={'/api/tbl/case/initform'}
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
