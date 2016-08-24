import React from 'react';
import { Button } from 'react-bootstrap';
import { Card, FormDialog, Fa } from '../../../components';
import Form from '../../../forms/form';
import CRUD from '../../../commons/crud';
import moment from 'moment';
import { app } from '../../../core/app';

const editorDef = {
	controls: [
		{
			property: 'sendSystemMessages',
			type: 'yesNo',
			label: __('Workspace.sendSystemMessages'),
			size: { sm: 12 },
			required: true
		},
		{
			type: 'string',
			label: __('form.name'),
			property: 'name',
			size: { sm: 8 },
			required: true
		},
		{
			type: 'string',
			label: __('Workspace.extension'),
			property: 'extension',
			size: { sm: 4 },
			required: true
		},
		{
			type: 'subtitle',
			label: __('admin.workspaces.cases'),
			size: { sm: 12 }
		},
		{
			type: 'select',
			label: __('NameComposition'),
			property: 'patientNameComposition',
			options: app.getState().app.lists.NameComposition,
			size: { sm: 12 },
			required: true
		},
		{
			type: 'select',
			label: __('Workspace.treatMonitoringInput'),
			property: 'treatMonitoringInput',
			options: app.getState().app.lists.TreatMonitoringInput,
			size: { sm: 12 },
			required: true
		},
		{
			type: 'select',
			label: __('Workspace.patientAddrRequiredLevels') + 'IMPROVE THIS LIST',
			property: 'patientAddrRequiredLevels',
			options: { from: 1, to: 5 },
			size: { sm: 12 },
			required: true
		},

		{
			type: 'subtitle',
			label: __('DisplayCaseNumber'),
			size: { sm: 12 },
			level: 2
		},
		{
			type: 'select',
			label: __('DiagnosisType.SUSPECT'),
			property: 'suspectCaseNumber',
			options: app.getState().app.lists.DisplayCaseNumber,
			size: { sm: 12 },
			required: true
		},
		{
			type: 'select',
			label: __('DiagnosisType.CONFIRMED'),
			property: 'confirmedCaseNumber',
			options: app.getState().app.lists.DisplayCaseNumber,
			size: { sm: 12 },
			required: true
		},

		{
			type: 'subtitle',
			label: __('admin.workspaces.casevalidation'),
			size: { sm: 12 },
			level: 2
		},
		{
			type: 'select',
			label: __('CaseClassification.TB'),
			property: 'caseValidationTB',
			options: app.getState().app.lists.CaseValidationOption,
			size: { sm: 12 },
			required: true
		},
		{
			type: 'select',
			label: __('CaseClassification.DRTB'),
			property: 'caseValidationDRTB',
			options: app.getState().app.lists.CaseValidationOption,
			size: { sm: 12 },
			required: true
		},
		{
			type: 'select',
			label: __('CaseClassification.NTM'),
			property: 'caseValidationNTM',
			options: app.getState().app.lists.CaseValidationOption,
			size: { sm: 12 },
			required: true
		},
		{
			type: 'subtitle',
			label: __('admin.workspaces.lmis'),
			size: { sm: 12 }
		},
		{
			type: 'select',
			label: __('Workspace.monthsToAlertExpiredMedicines') + ' (in months)',
			property: 'monthsToAlertExpiredMedicines',
			options: { from: 1, to: 12 },
			size: { sm: 12 },
			required: true
		},
		{
			type: 'select',
			label: __('Workspace.minStockOnHand') + ' (in months)',
			property: 'minStockOnHand',
			options: { from: 1, to: 12 },
			size: { sm: 12 },
			required: true
		},
		{
			type: 'select',
			label: __('Workspace.maxStockOnHand') + ' (in months)',
			property: 'maxStockOnHand',
			options: { from: 1, to: 12 },
			size: { sm: 12 },
			required: true
		}
	],
	title: __('admin.config')
};

export default class WorkspaceSettings extends React.Component {

	constructor(props) {
		super(props);
		this.state = { doc: {}, editing: false };
		this.showForm = this.showForm.bind(this);
		this.saveAndClose = this.saveAndClose.bind(this);
	}

	showForm(state) {
		const self = this;
		return (() => self.setState({ editing: state }));
	}

	saveAndClose() {
		this.setState({ editing: false });
	}

	render() {
		const header = (
			<div>
				<h4 className="inlineb">{__('admin.config')}</h4>
				<div className="pull-right">
					<Button onClick={this.showForm(true)}><Fa icon="pencil"/>{__('action.edit')}</Button>
				</div>
			</div>
		);
		return (
			<div>
				<Card header={header}>
					<Form readOnly schema={editorDef} doc={this.state.doc} />
				</Card>
				<FormDialog schema={editorDef}
					doc={this.state.doc}
					onConfirm={this.saveAndClose}
					onCancel={this.showForm(false)}
					wrapType={'modal'}
					modalShow={this.state.editing === true}
					/>
			</div>
			);
	}
}
