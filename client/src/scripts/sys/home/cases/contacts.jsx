import React from 'react';
import CrudView from '../../crud/crud-view';
import { Profile } from '../../../components';
import CaseComments from './case-comments';
import { mockCrud } from '../../mock-data';


export default class Contacts extends React.Component {

	constructor(props) {
		super(props);

		// form fields for contacts
		const editorSchema = {
			layout: [
				{
					type: 'string',
					label: 'Contact name',
					property: 'registrationCode',
					max: 100,
					size: { sm: 12 }
				},
				{
					type: 'number',
					property: 'age',
					label: __('TbCase.age'),
					required: true,
					size: { sm: 6 }
				},
				{
					type: 'select',
					property: 'gender',
					label: __('Gender'),
					required: true,
					options: [
						{ id: 'MALE', name: __('Gender.MALE') },
						{ id: 'FEMALE', name: __('Gender.FEMALE') }
					],
					size: { sm: 6 }
				},
				{
					type: 'date',
					property: 'dateOfExamination',
					label: __('TbContact.dateOfExamination'),
					size: { sm: 6 }
				},
				{
					type: 'select',
					property: 'contactType',
					label: __('TbField.CONTACTTYPE'),
					options: [
						{ id: 'household', name: 'Household' },
						{ id: 'institutional', name: 'Institutional (asylum, shelter, orphanage, etc.)' },
						{ id: 'nosocomial', name: 'Nosocomial' }
					]
				},
				{
					type: 'yesNo',
					property: 'examined',
					label: __('TbContact.examined'),
					required: true
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
							list: self.props.tbcase.contacts,
							count: self.props.tbcase.contacts.length
						};
					default:
						return null;
				}
			});
		this.state = { crud: crud, editorSchema: editorSchema };
	}

	cellRender(item) {
		return (
			<Profile size="small" type={item.gender.toLowerCase()}
				title={item.name} subtitle={item.age} />
		);
	}

	render() {
		const tbcase = this.props.tbcase;

		return (
			<CaseComments
				tbcase={tbcase} group="contacts">
				<CrudView combine
					modal
					title={__('cases.contacts')}
					editorSchema={this.state.editorSchema}
					crud={this.state.crud}
					onCellRender={this.cellRender}
					/>
			</CaseComments>
			);
	}
}

Contacts.propTypes = {
	tbcase: React.PropTypes.object
};
