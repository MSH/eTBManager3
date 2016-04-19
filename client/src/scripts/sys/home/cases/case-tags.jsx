
import React from 'react';
import { FormDialog } from '../../../components/index';

const tfschema = {
			layout: [{
					property: 'newTag',
					required: true,
					type: 'string',
					max: 20,
					label: 'New tag',
					size: { sm: 12 }
				}
			]
		};

const fschema = {
			title: __('Permission.CASE_TAG'),
			layout: [
				{
					property: 'tags',
					required: true,
					type: 'multiListBox',
					label: __('admin.tags'),
					options: [
						{ id: '1', name: 'On Treatment' },
						{ id: '2', name: 'Tag 2' },
						{ id: '3', name: 'Tag 3' },
						{ id: '4', name: 'Tag 4' },
						{ id: '5', name: 'Tag 5' },
						{ id: '6', name: 'Tag 6' },
						{ id: '7', name: 'Tag 7' },
						{ id: '8', name: 'Tag 8' },
						{ id: '9', name: 'Tag 9' },
						{ id: '10', name: 'Tag 23' },
						{ id: '11', name: 'Tag 25' },
						{ id: '12', name: 'Tag 28' },
						{ id: '13', name: 'Tag 29' },
						{ id: '14', name: 'Tag 20' },
						{ id: '15', name: 'Tag 21' },
						{ id: '16', name: 'Tag 22' },
						{ id: '17', name: 'Tag 23' }
					],
					vertical: true,
					textAlign: 'left',
					maxHeight: 250
				},
				{
					property: 'newTags',
					type: 'tableForm',
					fschema: tfschema,
					min: 0,
					size: { sm: 12 }
				}
			]
		};

/**
 * The page controller of the public module
 */
export default class CaseTags extends React.Component {

	constructor(props) {
		super(props);

		this.state = { doc: {} };
	}

	saveTags() {
		console.log('go to server and save this case tags! Dont forget to return a promise');
	}

	render() {
		fschema.title = __('Permission.CASE_TAG') + ' - ' + this.props.tbcase.patient.name;

		return (
			<FormDialog
				schema={fschema}
				doc={this.state.doc}
				onConfirm={this.saveTags}
				onCancel={this.props.onClose}
				confirmCaption={__('action.save')}
				wrapType={'modal'}
				modalShow={this.props.show}/>
		);
	}
}

CaseTags.propTypes = {
	tbcase: React.PropTypes.object,
	show: React.PropTypes.bool,
	onClose: React.PropTypes.func
};
