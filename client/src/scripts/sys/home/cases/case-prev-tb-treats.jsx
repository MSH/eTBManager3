import React from 'react';
import CrudView from '../../crud/crud-view';
import CaseComments from './case-comments';
import CRUD from '../../../commons/crud';
import Form from '../../../forms/form';
import moment from 'moment';
import { app } from '../../../core/app';
import { Profile } from '../../../components';

const crud = new CRUD('prevtreat');

const displaySchema1 = {
			controls: [
				{
					type: 'string',
					label: __('cases.prevtreat.iniyear'),
					property: 'year',
					size: { sm: 2 }
				},
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
					size: { sm: 2 }
				},
				{
					type: 'string',
					label: __('cases.prevtreat.endyear'),
					property: 'outcomeYear',
					size: { sm: 2 }
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
					size: { sm: 2 }
				},
				{
					type: 'string',
					label: __('cases.prevtreat.outcome'),
					property: 'outcome.name',
					size: { sm: 4 }
				}
			]
		};

export default class CasePrevTbTreats extends React.Component {

	constructor(props) {
		super(props);
		this.cellRender = this.cellRender.bind(this);
		this.collapseCellRender = this.collapseCellRender.bind(this);

		const editorSchema = {
			defaultProperties: {
				tbcaseId: props.tbcase.id
			},
			controls: [
				{
					type: 'select',
					label: __('cases.prevtreat.iniyear'),
					property: 'year',
					options: { from: 1990, to: 2016 },
					size: { sm: 6 }
				},
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
					size: { sm: 6 }
				},

				{
					property: 'am',
					type: 'yesNo',
					label: __('cases.prevtreat.am'),
					size: { md: 3 }
				},
				{
					property: 'cfz',
					type: 'yesNo',
					label: __('cases.prevtreat.cfz'),
					size: { md: 3 }
				},
				{
					property: 'cm',
					type: 'yesNo',
					label: __('cases.prevtreat.cm'),
					size: { md: 3 }
				},
				{
					property: 'cs',
					type: 'yesNo',
					label: __('cases.prevtreat.cs'),
					size: { md: 3 }
				},
				{
					property: 'e',
					type: 'yesNo',
					label: __('cases.prevtreat.e'),
					size: { md: 3 }
				},
				{
					property: 'eto',
					type: 'yesNo',
					label: __('cases.prevtreat.eto'),
					size: { md: 3 }
				},
				{
					property: 'h',
					type: 'yesNo',
					label: __('cases.prevtreat.h'),
					size: { md: 3 }
				},
				{
					property: 'lfx',
					type: 'yesNo',
					label: __('cases.prevtreat.lfx'),
					size: { md: 3 }
				},
				{
					property: 'ofx',
					type: 'yesNo',
					label: __('cases.prevtreat.ofx'),
					size: { md: 3 }
				},
				{
					property: 'r',
					type: 'yesNo',
					label: __('cases.prevtreat.r'),
					size: { md: 3 }
				},
				{
					property: 's',
					type: 'yesNo',
					label: __('cases.prevtreat.s'),
					size: { md: 3 }
				},
				{
					property: 'z',
					type: 'yesNo',
					label: __('cases.prevtreat.z'),
					size: { md: 3 }
				},

				{
					type: 'select',
					label: __('cases.prevtreat.endyear'),
					property: 'outcomeYear',
					options: { from: 1990, to: 2016 },
					size: { sm: 4 }
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
					size: { sm: 4 }
				},
				{
					type: 'select',
					label: __('cases.prevtreat.outcome'),
					property: 'outcome',
					options: app.getState().app.lists.PrevTBTreatmentOutcome,
					size: { sm: 4 }
				}
			],
			title: doc => doc && doc.id ? __('cases.prevtreat.edt') : __('cases.prevtreat.new')
		};

		this.state = { editorSchema: editorSchema };
	}

	cellRender(item) {
		console.log(item);
		return (
			<Form readOnly schema={displaySchema1} doc={item} />
		);
	}

	collapseCellRender(item) {
		return (null);
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
					cellSize={{ md: 12 }}
					/>
			</CaseComments>
			);
	}
}

CasePrevTbTreats.propTypes = {
	tbcase: React.PropTypes.object
};
