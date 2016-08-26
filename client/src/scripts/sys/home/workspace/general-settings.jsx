import React from 'react';
import { Button } from 'react-bootstrap';
import { Card, FormDialog, WaitIcon, Fa } from '../../../components';
import Form from '../../../forms/form';
import CRUD from '../../../commons/crud';
import { app } from '../../../core/app';

const editorDef = {
	controls: [
		{
			type: 'string',
			label: __('form.name'),
			property: 'name',
			size: { sm: 4 },
			required: true
		},
		{
			property: 'sendSystemMessages',
			type: 'yesNo',
			label: __('Workspace.sendSystemMessages'),
			size: { sm: 5 },
			defaultValue: false,
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
			size: { sm: 4 },
			required: true
		},
		{
			type: 'select',
			label: __('Workspace.treatMonitoringInput'),
			property: 'treatMonitoringInput',
			options: app.getState().app.lists.TreatMonitoringInput,
			size: { sm: 4 },
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
			size: { sm: 4 },
			required: true
		},
		{
			type: 'select',
			label: __('DiagnosisType.CONFIRMED'),
			property: 'confirmedCaseNumber',
			options: app.getState().app.lists.DisplayCaseNumber,
			size: { sm: 4 },
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
			size: { sm: 4 },
			required: true
		},
		{
			type: 'select',
			label: __('CaseClassification.DRTB'),
			property: 'caseValidationDRTB',
			options: app.getState().app.lists.CaseValidationOption,
			size: { sm: 4 },
			required: true
		},
		{
			type: 'select',
			label: __('CaseClassification.NTM'),
			property: 'caseValidationNTM',
			options: app.getState().app.lists.CaseValidationOption,
			size: { sm: 4 },
			required: true
		},
		{
			type: 'subtitle',
			label: __('admin.workspaces.lmis'),
			size: { sm: 12 }
		},
		{
			type: 'select',
			label: __('Workspace.monthsToAlertExpiredMedicines'),
			property: 'monthsToAlertExpiredMedicines',
			options: { from: 1, to: 12 },
			size: { sm: 4 },
			required: true
		},
		{
			type: 'select',
			label: __('Workspace.minStockOnHand'),
			property: 'minStockOnHand',
			options: { from: 1, to: 12 },
			size: { sm: 4 },
			required: true
		},
		{
			type: 'select',
			label: __('Workspace.maxStockOnHand'),
			property: 'maxStockOnHand',
			options: { from: 1, to: 12 },
			size: { sm: 4 },
			required: true
		}
	],
	title: __('admin.config')
};

const crud = new CRUD('workspace');

export default class WorkspaceSettings extends React.Component {

	constructor(props) {
		super(props);
		this.state = { editing: 'no' };
		this.openForm = this.openForm.bind(this);
		this.saveAndClose = this.saveAndClose.bind(this);
		this.cancelClick = this.cancelClick.bind(this);
	}

	componentWillMount() {
		crud.get(app.getState().session.workspaceId).then(res => {
			this.setState({ workspace: res });
		});
	}

	openForm() {
		this.setState({ editing: 'fetch' });

		return crud.getEdit(app.getState().session.workspaceId).then(res => {
			this.setState({ doc: res, editing: 'yes' });
		});
	}

	saveAndClose() {
		console.log(this.state.doc);
		return crud.update(app.getState().session.workspaceId, this.state.doc).then(() => {
			const readOnlyObj = Object.assign({}, this.state.doc);
			this.setState({ workspace: readOnlyObj, editing: 'no' });
		});
	}

	cancelClick() {
		this.setState({ editing: 'no' });
	}

	render() {
		if (!this.state.doc && !this.state.workspace) {
			return <WaitIcon type="card" />;
		}

		const header = (
			<div>
				<h4 className="inlineb">{__('admin.config')}</h4>
				<div className="pull-right">
					<Button onClick={this.openForm}>
						<Fa icon={this.state.editing === 'fetch' ? 'circle-o-notch' : 'pencil'} spin={this.state.editing === 'fetch'}/>
						{__('action.edit')}
					</Button>
				</div>
			</div>
		);
		return (
			<div>
				{(this.state.editing === 'no' || this.state.editing === 'fetch') && this.state.workspace &&
					<Card header={header}>
						<Form readOnly schema={editorDef} doc={this.state.workspace} />
					</Card>
				}

				{this.state.editing === 'yes' && this.state.doc &&
					<FormDialog schema={editorDef}
						doc={this.state.doc}
						onConfirm={this.saveAndClose}
						onCancel={this.cancelClick}
						wrapType={'card'}
						/>
				}
			</div>
			);
	}
}
