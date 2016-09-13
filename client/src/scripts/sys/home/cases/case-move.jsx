
import React from 'react';
import { FormDialog } from '../../../components/index';
import SessionUtils from '../../session-utils';

const fschema = {
			title: __('cases.move'),
			controls: [
				{
					property: 'date',
					required: true,
					type: 'date',
					label: __('cases.details.date'),
					defaultValue: new Date()
				},
				{
					property: 'unitId',
					type: 'unit',
					label: __('Tbunit'),
					required: true
				}
			]
		};

/**
 * The page controller of the public module
 */
export default class CaseMove extends React.Component {

	constructor(props) {
		super(props);

		this.state = { doc: {} };
		this.moveCase = this.moveCase.bind(this);
	}

	moveCase() {
		console.log('go to server and transfer this case! Dont forget to return a promise');
		this.props.onClose();
	}

	render() {
		fschema.title = __('cases.move') + ' - ' + SessionUtils.nameDisplay(this.props.tbcase.patient.name);
		return (
			<FormDialog
				schema={fschema}
				doc={this.state.doc}
				onConfirm={this.moveCase}
				onCancel={this.props.onClose}
				confirmCaption={__('cases.move')}
				wrapType={'modal'}
				modalShow={this.props.show}/>
		);
	}
}

CaseMove.propTypes = {
	tbcase: React.PropTypes.object,
	show: React.PropTypes.bool,
	onClose: React.PropTypes.func
};
