import React from 'react';
import CrudView from '../../crud/crud-view';
import CaseComments from './case-comments';
import CRUD from '../../../commons/crud';
import Form from '../../../forms/form';
import moment from 'moment';
import { app } from '../../../core/app';
import { isEmpty } from '../../../commons/utils';

const crud = new CRUD('prevtreat');

const readOnlySchema = {
			controls: [
				{
					property: 'am',
					type: 'yesNo',
					label: __('cases.prevtreat.am'),
					size: { sm: 2 },
					visible: i => i.am === true
				},
				{
					property: 'cfz',
					type: 'yesNo',
					label: __('cases.prevtreat.cfz'),
					size: { sm: 2 },
					visible: i => i.cfz === true
				},
				{
					property: 'cm',
					type: 'yesNo',
					label: __('cases.prevtreat.cm'),
					size: { sm: 2 },
					visible: i => i.cm === true
				},
				{
					property: 'cs',
					type: 'yesNo',
					label: __('cases.prevtreat.cs'),
					size: { sm: 2 },
					visible: i => i.cs === true
				},
				{
					property: 'e',
					type: 'yesNo',
					label: __('cases.prevtreat.e'),
					size: { sm: 2 },
					visible: i => i.e === true
				},
				{
					property: 'eto',
					type: 'yesNo',
					label: __('cases.prevtreat.eto'),
					size: { sm: 2 },
					visible: i => i.eto === true
				},
				{
					property: 'h',
					type: 'yesNo',
					label: __('cases.prevtreat.h'),
					size: { sm: 2 },
					visible: i => i.h === true
				},
				{
					property: 'lfx',
					type: 'yesNo',
					label: __('cases.prevtreat.lfx'),
					size: { sm: 2 },
					visible: i => i.lfx === true
				},
				{
					property: 'ofx',
					type: 'yesNo',
					label: __('cases.prevtreat.ofx'),
					size: { sm: 2 },
					visible: i => i.ofx === true
				},
				{
					property: 'r',
					type: 'yesNo',
					label: __('cases.prevtreat.r'),
					size: { sm: 2 },
					visible: i => {
						console.log('heeeeeeey', i);
						return (i.r === true);
					}
				},
				{
					property: 's',
					type: 'yesNo',
					label: __('cases.prevtreat.s'),
					size: { sm: 2 },
					visible: i => i.s === true
				},
				{
					property: 'z',
					type: 'yesNo',
					label: __('cases.prevtreat.z'),
					size: { sm: 2 },
					visible: i => i.z === true
				}
			]
		};

export default class CasePrevTbTreats extends React.Component {

	constructor(props) {
		super(props);
		this.cellRender = this.cellRender.bind(this);

		const editorSchema = {
			defaultProperties: {
				tbcaseId: props.tbcase.id
			},
			controls: [
				{
					type: 'select',
					property: 'month',
					label: __('cases.prevtreat.inimonth'),
					options: [
						{ id: 0, name: 'Janeiro' },
						{ id: 1, name: 'Fevereiro' },
						{ id: 2, name: 'Março' },
						{ id: 3, name: 'Abril' },
						{ id: 4, name: 'Maio' },
						{ id: 5, name: 'Junho' },
						{ id: 6, name: 'Julho' },
						{ id: 7, name: 'Agosto' },
						{ id: 8, name: 'Setembro' },
						{ id: 9, name: 'Outubro' },
						{ id: 10, name: 'Novembro' },
						{ id: 11, name: 'Dezembro' }
					],
					size: { sm: 6 },
					required: true
				},
				{
					type: 'select',
					label: __('cases.prevtreat.iniyear'),
					property: 'year',
					options: { from: 1990, to: 2016 },
					size: { sm: 6 },
					required: true
				},
				{
					type: 'select',
					property: 'outcomeMonth',
					label: __('cases.prevtreat.endmonth'),
					options: [
						{ id: 0, name: 'Janeiro' },
						{ id: 1, name: 'Fevereiro' },
						{ id: 2, name: 'Março' },
						{ id: 3, name: 'Abril' },
						{ id: 4, name: 'Maio' },
						{ id: 5, name: 'Junho' },
						{ id: 6, name: 'Julho' },
						{ id: 7, name: 'Agosto' },
						{ id: 8, name: 'Setembro' },
						{ id: 9, name: 'Outubro' },
						{ id: 10, name: 'Novembro' },
						{ id: 11, name: 'Dezembro' }
					],
					size: { sm: 6 }
				},
				{
					type: 'select',
					label: __('cases.prevtreat.endyear'),
					property: 'outcomeYear',
					options: { from: 1990, to: 2016 },
					size: { sm: 6 }
				},
				{
					type: 'select',
					label: __('cases.prevtreat.outcome'),
					property: 'outcome',
					options: app.getState().app.lists.PrevTBTreatmentOutcome,
					size: { sm: 12 },
					required: true
				},
				{
					type: 'subtitle',
					label: __('cases.prevtreat.substances'),
					size: { sm: 12 }
				},
				{
					property: 'am',
					type: 'yesNo',
					label: __('cases.prevtreat.am'),
					size: { sm: 3 },
					defaultValue: false
				},
				{
					property: 'cfz',
					type: 'yesNo',
					label: __('cases.prevtreat.cfz'),
					size: { sm: 3 },
					defaultValue: false
				},
				{
					property: 'cm',
					type: 'yesNo',
					label: __('cases.prevtreat.cm'),
					size: { sm: 3 },
					defaultValue: false
				},
				{
					property: 'cs',
					type: 'yesNo',
					label: __('cases.prevtreat.cs'),
					size: { sm: 3 },
					defaultValue: false
				},
				{
					property: 'e',
					type: 'yesNo',
					label: __('cases.prevtreat.e'),
					size: { sm: 3 },
					defaultValue: false
				},
				{
					property: 'eto',
					type: 'yesNo',
					label: __('cases.prevtreat.eto'),
					size: { sm: 3 },
					defaultValue: false
				},
				{
					property: 'h',
					type: 'yesNo',
					label: __('cases.prevtreat.h'),
					size: { sm: 3 },
					defaultValue: false
				},
				{
					property: 'lfx',
					type: 'yesNo',
					label: __('cases.prevtreat.lfx'),
					size: { sm: 3 },
					defaultValue: false
				},
				{
					property: 'ofx',
					type: 'yesNo',
					label: __('cases.prevtreat.ofx'),
					size: { sm: 3 },
					defaultValue: false
				},
				{
					property: 'r',
					type: 'yesNo',
					label: __('cases.prevtreat.r'),
					size: { sm: 3 },
					defaultValue: false
				},
				{
					property: 's',
					type: 'yesNo',
					label: __('cases.prevtreat.s'),
					size: { sm: 3 },
					defaultValue: false
				},
				{
					property: 'z',
					type: 'yesNo',
					label: __('cases.prevtreat.z'),
					size: { sm: 3 },
					defaultValue: false
				}
			],
			title: doc => doc && doc.id ? __('cases.prevtreat.edt') : __('cases.prevtreat.new')
		};

		this.state = { editorSchema: editorSchema };
	}

	cellRender(item) {
		let title = moment([2000, item.month, 1]).format('MMMM') + '/' + item.year;
		const outcomeMonth = !isEmpty(item.outcomeMonth) ? moment([2000, item.outcomeMonth, 1]).format('MMMM') + '/' : '';
		title = title + (item.outcomeYear ? ' ' + __('global.until') + ' ' + outcomeMonth + item.outcomeYear : '');

		return (
			<span>
				<div>
					<b>{title + ' - ' + item.outcome.name}</b>
				</div>
				<hr/>
				<Form readOnly schema={readOnlySchema} doc={item} />
			</span>
		);
	}

	render() {
		const tbcase = this.props.tbcase;

		return (
			<CaseComments
				tbcase={tbcase} group="PREV_TREATS">
				<CrudView combine modal
					title={__('cases.prevtreat')}
					editorSchema={this.state.editorSchema}
					crud={crud}
					onCellRender={this.cellRender}
					queryFilters={{ tbcaseId: tbcase.id }}
					/>
			</CaseComments>
			);
	}
}

CasePrevTbTreats.propTypes = {
	tbcase: React.PropTypes.object
};
