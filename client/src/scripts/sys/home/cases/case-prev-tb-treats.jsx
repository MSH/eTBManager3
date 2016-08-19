import React from 'react';
import CrudView from '../../crud/crud-view';
import CaseComments from './case-comments';
import CRUD from '../../../commons/crud';
import Form from '../../../forms/form';
import moment from 'moment';
import { app } from '../../../core/app';
import { Profile } from '../../../components';

const crud = new CRUD('prevtreat');

const readOnlySchema = {
			controls: [
				{
					property: 'am',
					type: 'yesNo',
					label: __('cases.prevtreat.am'),
					size: { sm: 1 }
				},
				{
					property: 'cfz',
					type: 'yesNo',
					label: __('cases.prevtreat.cfz'),
					size: { sm: 1 }
				},
				{
					property: 'cm',
					type: 'yesNo',
					label: __('cases.prevtreat.cm'),
					size: { sm: 1 }
				},
				{
					property: 'cs',
					type: 'yesNo',
					label: __('cases.prevtreat.cs'),
					size: { sm: 1 }
				},
				{
					property: 'e',
					type: 'yesNo',
					label: __('cases.prevtreat.e'),
					size: { sm: 1 }
				},
				{
					property: 'eto',
					type: 'yesNo',
					label: __('cases.prevtreat.eto'),
					size: { sm: 1 }
				},
				{
					property: 'h',
					type: 'yesNo',
					label: __('cases.prevtreat.h'),
					size: { sm: 1 }
				},
				{
					property: 'lfx',
					type: 'yesNo',
					label: __('cases.prevtreat.lfx'),
					size: { sm: 1 }
				},
				{
					property: 'ofx',
					type: 'yesNo',
					label: __('cases.prevtreat.ofx'),
					size: { sm: 1 }
				},
				{
					property: 'r',
					type: 'yesNo',
					label: __('cases.prevtreat.r'),
					size: { sm: 1 }
				},
				{
					property: 's',
					type: 'yesNo',
					label: __('cases.prevtreat.s'),
					size: { sm: 1 }
				},
				{
					property: 'z',
					type: 'yesNo',
					label: __('cases.prevtreat.z'),
					size: { sm: 1 }
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
						{ id: 1, name: 'Janeiro' },
						{ id: 2, name: 'Fevereiro' },
						{ id: 3, name: 'Março' },
						{ id: 4, name: 'Abril' },
						{ id: 5, name: 'Maio' },
						{ id: 6, name: 'Junho' },
						{ id: 7, name: 'Julho' },
						{ id: 8, name: 'Agosto' },
						{ id: 9, name: 'Setembro' },
						{ id: 10, name: 'Outubro' },
						{ id: 11, name: 'Novembro' },
						{ id: 12, name: 'Dezembro' }
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
						{ id: 1, name: 'Janeiro' },
						{ id: 2, name: 'Fevereiro' },
						{ id: 3, name: 'Março' },
						{ id: 4, name: 'Abril' },
						{ id: 5, name: 'Maio' },
						{ id: 6, name: 'Junho' },
						{ id: 7, name: 'Julho' },
						{ id: 8, name: 'Agosto' },
						{ id: 9, name: 'Setembro' },
						{ id: 10, name: 'Outubro' },
						{ id: 11, name: 'Novembro' },
						{ id: 12, name: 'Dezembro' }
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
					size: { sm: 3 }
				},
				{
					property: 'cfz',
					type: 'yesNo',
					label: __('cases.prevtreat.cfz'),
					size: { sm: 3 }
				},
				{
					property: 'cm',
					type: 'yesNo',
					label: __('cases.prevtreat.cm'),
					size: { sm: 3 }
				},
				{
					property: 'cs',
					type: 'yesNo',
					label: __('cases.prevtreat.cs'),
					size: { sm: 3 }
				},
				{
					property: 'e',
					type: 'yesNo',
					label: __('cases.prevtreat.e'),
					size: { sm: 3 }
				},
				{
					property: 'eto',
					type: 'yesNo',
					label: __('cases.prevtreat.eto'),
					size: { sm: 3 }
				},
				{
					property: 'h',
					type: 'yesNo',
					label: __('cases.prevtreat.h'),
					size: { sm: 3 }
				},
				{
					property: 'lfx',
					type: 'yesNo',
					label: __('cases.prevtreat.lfx'),
					size: { sm: 3 }
				},
				{
					property: 'ofx',
					type: 'yesNo',
					label: __('cases.prevtreat.ofx'),
					size: { sm: 3 }
				},
				{
					property: 'r',
					type: 'yesNo',
					label: __('cases.prevtreat.r'),
					size: { sm: 3 }
				},
				{
					property: 's',
					type: 'yesNo',
					label: __('cases.prevtreat.s'),
					size: { sm: 3 }
				},
				{
					property: 'z',
					type: 'yesNo',
					label: __('cases.prevtreat.z'),
					size: { sm: 3 }
				}
			],
			title: doc => doc && doc.id ? __('cases.prevtreat.edt') : __('cases.prevtreat.new')
		};

		this.state = { editorSchema: editorSchema };
	}

	cellRender(item) {
		let title = moment([2000, item.month - 1, 1]).format('MMMM') + '/' + item.year;
		const outcomeMonth = item.outcomeMonth ? moment([2000, item.outcomeMonth - 1, 1]).format('MMMM') + '/' : null;
		title = title + (item.outcomeYear ? ' ' + __('global.until') + ' ' + outcomeMonth + item.outcomeYear : null);

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

	collapseCellRender() {
		return (<hr/>);
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
					onDetailRender={this.collapseCellRender}
					queryFilters={{ tbcaseId: tbcase.id }}
					cellSize={{ sm: 12 }}
					/>
			</CaseComments>
			);
	}
}

CasePrevTbTreats.propTypes = {
	tbcase: React.PropTypes.object
};
