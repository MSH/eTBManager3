
import React from 'react';
import CrudView from '../../crud-view';
import CRUD from '../../../commons/crud';
import { app } from '../../../core/app';
import Profile from '../../../components/profile';
import { TableForm } from '../../../components/index';

const crud = new CRUD('regimen');

// definition of the form fields to edit medicine regimens
const editorDef = {
	defaultProperties: {
		medicines: null //TODOMSR ver se´essa é a maneira correta de passar os medicines.
	},
	layout: [
		{
			property: 'name',
			required: true,
			type: 'string',
			max: 200,
			label: __('form.name'),
			size: { sm: 6 }
		},
		{
			property: 'classification',
			required: true,
			type: 'select',
			options: app.getState().app.lists.CaseClassification,
			label: __('CaseClassification'),
			size: { sm: 4 }
		},
		{
			property: 'customId',
			type: 'string',
			max: 50,
			label: __('form.customId'),
			size: { sm: 6 }
		},
		{
			property: 'active',
			type: 'yesNo',
			required: true,
			label: __('EntityState.ACTIVE'),
			defaultValue: true,
			size: { sm: 4 } //TODOMSR: pq esse trem n ocupa a col toda? vi que ele esta com um width de 33% whyyyy????
		}
	],
	title: doc => doc && doc.id ? __('admin.regimens.edt') : __('admin.regimens.new')
};

const tfschema = {
			layout: [
				{
					property: 'medicine.id',
					required: true,
					type: 'select',
					label: __('Medicine'),
					options: 'medicines',
					size: { md: 4 }
				},
				{
					property: 'defaultDoseUnit',
					required: true,
					type: 'number',
					label: __('Regimen.doseunit'),
					size: { sm: 2 }
				},
				{
					property: 'defaultFrequency',
					required: true,
					type: 'select',
					label: __('Regimen.frequency'),
					options: { from: 1, to: 7 },
					size: { sm: 2 }
				},
				{
					property: 'iniDay',
					required: true,
					type: 'number',
					label: __('Regimen.iniday'),
					size: { sm: 2 }
				},
				{
					property: 'days',
					required: true,
					type: 'number',
					label: __('Regimen.days'),
					size: { sm: 2 }
				}
			]
		};

/**
 * The page controller of the public module
 */
export default class Regimens extends React.Component {

	constructor(props) {
		super(props);

		this.state = { rowsQuantity: 1, tfdocs: [], tferrorsarr: [] };
		this.addRow = this.addRow.bind(this);
		this.remRow = this.remRow.bind(this);
		this.getMedicines = this.getMedicines.bind(this);
	}

	cellRender(item) {
		return <Profile fa="file-text-o" title={item.name} size="small" />;
	}

	addRow() {
		var quantity = this.state.rowsQuantity + 1;
		this.setState({ rowsQuantity: quantity });
	}

	remRow() {
		if (this.state.rowsQuantity > 1) {
			var quantity = this.state.rowsQuantity - 1;
			this.setState({ rowsQuantity: quantity });
		}
	}

	getMedicines() {
		return this.state.tfdocs;
	}

	render() {
		// get information about the route of this page
		const data = this.props.route.data;

		editorDef.defaultProperties.medicines = this.getMedicines; // TODOMSR poderia fazer no component will mount??? pODERIA SER COM UMA FUNÇÃO DE UMA LINHA?

		return (
			<CrudView crud={crud}
				title={data.title}
				onCellRender={this.cellRender}
				editorDef={editorDef}
				perm={data.perm}>
				//TODOMSR como passar a função idValid do TableForm, pra ser executada antes de o formulario salvar?
				<TableForm
					fschema={tfschema}
					rowsQuantity={this.state.rowsQuantity}
					addRow={this.addRow}
					remRow={this.remRow}
					docs={this.state.tfdocs}
					errorsarr={this.state.tferrorsarr}
					nodetype={'div'} />

			</CrudView>
			);
	}
}

Regimens.propTypes = {
	route: React.PropTypes.object
};
