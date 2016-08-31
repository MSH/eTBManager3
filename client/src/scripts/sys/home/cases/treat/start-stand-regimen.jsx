import React from 'react';
import { FormDialog } from '../../../../components';
import { server } from '../../../../commons/server';

/**
 * Display the form to start a standard regimen
 */
export default class StartStandRegimen extends React.Component {

	constructor(props) {
		super(props);
		this.save = this.save.bind(this);
		this.cancel = this.cancel.bind(this);
		const formSchema = {
			title: __('cases.treatment.startstandard'),
			controls: [
				{
					property: 'unitId',
					type: 'unit',
					label: __('Tbunit.treatmentHealthUnit'),
					unitType: 'TBUNIT',
					required: true
				},
				{
					property: 'iniDate',
					type: 'date',
					label: __('TbCase.iniTreatmentDate'),
					required: true,
					size: { sm: 6 }
				},
				{
					property: 'regimenId',
					type: 'select',
					label: __('Regimen'),
					options: 'regimens',
					params: {
						classification: this.props.tbcase.classification
					},
					required: true
				}
			]
		};
		this.state = { schema: formSchema };
	}

	componentWillMount() {
		const tbcase = this.props.tbcase;

		const doc = {
			caseId: tbcase.id,
			unitId: tbcase.ownerUnit.id
		};

		this.setState({ doc: doc });
	}

	save(doc) {
		const self = this;

		return server.post('/api/cases/case/treatment/start/standard', doc)
		.then(() => {
			self.props.onClose(doc);
		});
	}

	cancel() {
		this.props.onClose(false);
	}

	render() {
		return (
			<FormDialog schema={this.state.schema}
				wrapType="modal"
				modalShow
				doc={this.state.doc}
				onConfirm={this.save}
				onCancel={this.cancel}
				/>
		);
	}
}

StartStandRegimen.propTypes = {
	tbcase: React.PropTypes.object.isRequired,
	onClose: React.PropTypes.func.isRequired
};
