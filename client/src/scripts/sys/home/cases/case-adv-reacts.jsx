import React from 'react';
import CrudView from '../../crud/crud-view';
import { Profile } from '../../../components';
import CaseComments from './case-comments';
import { mockCrud } from '../../mock-data';


export default class CaseAdvReact extends React.Component {

	constructor(props) {
		super(props);

		// form fields for contacts
		const editorSchema = {
			layout: [
				{
					type: 'select',
					label: 'Adverse reaction',
					property: 'adverseReaction',
					required: true,
					options: [
						{ id: 'adv1', name: 'Adverse reaction 1' },
						{ id: 'adv2', name: 'Adverse reaction 2' },
						{ id: 'adv3', name: 'Adverse reaction 3' },
						{ id: 'adv4', name: 'Adverse reaction 4' },
						{ id: 'adv5', name: 'Adverse reaction 5' },
						{ id: 'adv6', name: 'Adverse reaction 6' }
					],
					size: { sm: 12 }
				},
				{
					type: 'select',
					label: __('Medicine'),
					property: 'substance',
					options: 'substances',
					size: { sm: 12 }
				},
				{
					type: 'select',
					property: 'month',
					label: 'Initial month of treatment',
					required: true,
					options: { from: 1, to: 24 },
					size: { sm: 6 }
				},
				{
					type: 'text',
					property: 'comments',
					label: __('global.comments'),
					size: { sm: 12 }
				}
			]
		};

		// create mock data
		const self = this;
		const crud = mockCrud()
			.on((evt) => {
				switch (evt) {
					case 'query':
						return {
							list: self.props.tbcase.adverseReactions,
							count: self.props.tbcase.adverseReactions.length
						};
					default:
						return null;
				}
			});
		this.state = { crud: crud, editorSchema: editorSchema };
	}

	cellRender(item) {
		return (
			<div>
				<div>{item.adverseReaction.name}</div>
				<div className="text-muted"><b>{'Month of treatment: '}</b>{item.month}</div>
				<div className="text-muted"><b>{__('Medicine') + ': '}</b>{item.medicine}</div>
			</div>
		);
	}

	render() {
		const tbcase = this.props.tbcase;

		return (
			<CaseComments
				tbcase={tbcase} group="adv-reacts">
				<CrudView combine
					modal
					title={__('cases.sideeffects')}
					editorSchema={this.state.editorSchema}
					crud={this.state.crud}
					onCellRender={this.cellRender}
					/>
			</CaseComments>
			);
	}
}

CaseAdvReact.propTypes = {
	tbcase: React.PropTypes.object
};
