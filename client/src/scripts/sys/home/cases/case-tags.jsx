
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
					type: 'multiSelect',
					options: 'profiles',
					label: __('admin.tags'),
					size: { sm: 12 },
					required: true
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

		this.state = { doc: { tags: props.tbcase.tags } };
		this.saveTags = this.saveTags.bind(this);
		this.onCancel = this.onCancel.bind(this);
	}

	saveTags() {
		console.log('go to server and save this case tags! Dont forget to return a promise');
		this.onCancel();
	}

	onCancel() {
		this.setState({ doc: {} });
		if (this.props.onClose) {
			this.props.onClose();
		}
	}

	render() {
		fschema.title = __('Permission.CASE_TAG') + ' - ' + this.props.tbcase.patient.name;

		return (
			<FormDialog
				schema={fschema}
				doc={this.state.doc}
				onConfirm={this.saveTags}
				onCancel={this.onCancel}
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
