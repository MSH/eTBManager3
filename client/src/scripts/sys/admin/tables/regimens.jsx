
import React from 'react';
import CrudView from '../../crud-view';
import CRUD from '../../../commons/crud';
import { app } from '../../../core/app';
import Profile from '../../../components/profile';
import { Row, Col } from 'react-bootstrap';
import { Card, TableForm } from '../../../components/index';

const crud = new CRUD('regimen');

// definition of the form fields to edit medicine regimens
const editorDef = {
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
			size: { sm: 4 } //TODOMS: pq esse trem n ocupa a col toda? vi que ele esta com um width de 33% whyyyy????
		}
	],
	title: doc => doc && doc.id ? __('admin.regimens.edt') : __('admin.regimens.new')
};

const tfschema = {
			layout: [
				{
					property: 'iniDate',
					required: true,
					type: 'date',
					label: __('Period.iniDate'),
					size: { md: 3 }
				},
				{
					property: 'action',
					required: false,
					type: 'select',
					label: __('form.action'),
					options: app.getState().app.lists.CommandAction,
					size: { md: 3 }
				},
				{
					property: 'userId',
					required: false,
					type: 'select',
					label: __('User'),
					options: 'users',
					size: { md: 3 }
				},
				{
					property: 'type',
					required: false,
					type: 'string',
					max: 50,
					label: __('admin.reports.cmdhistory.cmdevent'),
					size: { sm: 3 }
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
	}

	cellRender(item) {
		return <Profile fa="file-text-o" title={item.name} size="small" />;
	}

	addRow() {
		var quantity = this.state.rowsQuantity + 1;
		this.setState({ rowsQuantity: quantity });
	}

	remRow() {
		var quantity = this.state.rowsQuantity - 1;
		this.setState({ rowsQuantity: quantity });
	}

	render() {
		// get information about the route of this page
		const data = this.props.route.data;

		return (
			<CrudView crud={crud}
				title={data.title}
				onCellRender={this.cellRender}
				editorDef={editorDef}
				perm={data.perm}>


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
