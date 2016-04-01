
import React from 'react';
import { FormDialog } from '../../../components/index';
import Form from '../../../forms/form';

const fschema = {
			layout: [
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
	}

	componentWillMount() {

	}

	moveCase() {
		const self = this;

		const errors = self.refs.form.validate();
		this.setState({ errors: errors });
		if (errors) {
			return true;
		}

		alert('TODOMS: go to server and close this case!');
		return false;
	}

	render() {
		const content = (
			<Form ref="form"
				schema={fschema}
				doc={this.state.doc}
				errors={this.state.errors} />
		);

		//TODOMSR: as observações dos campos estão aparecendo atras do modal
		return (
			<FormDialog
				show={this.props.show}
				title={__('cases.move') + ' - TODOMS CONCATENAR NOME DO PACIENTE'}
				type={'CustomCancel'}
				onClose={this.props.onClose}
				customBtnLbl={__('cases.move')}
				content={content} />
		);
	}
}

CaseMove.propTypes = {
	tbcase: React.PropTypes.object,
	show: React.PropTypes.bool,
	onClose: React.PropTypes.func
};
