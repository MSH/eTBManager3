
import React from 'react';
import { FormModal } from '../../../components/index';
import Form from '../../../forms/form';
import { app } from '../../../core/app';

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
					property: 'outcome',
					required: true,
					type: 'listBox',
					label: __('form.action'),
					options: [
						{ id: 'CURED', name: 'Cured' },
						{ id: 'TREAT_COMPLETED', name: 'Treatment completed' },
						{ id: 'DIED', name: 'Died' },
						{ id: 'LOST_FOLLOWUP', name: 'Lost follow-up' },
						{ id: 'TREAT_INTERRUPTED', name: 'Treatment interrupted' },
						{ id: 'OTHER', name: 'Other' }
					],
					vertical: true,
					textAlign: 'left'
				},
				{
				el: 'group',
					visible: value => value.outcome === 'OTHER',
					layout: [
						{
							property: 'otherOutcome',
							type: 'string',
							label: __('CaseState.OTHER') //todomsr: TA SAINDO MUITO JUNTO DO CAMPO ACIMA
						}
					]
				}
			]
		};

/**
 * The page controller of the public module
 */
export default class CaseClose extends React.Component {

	constructor(props) {
		super(props);

		this.state = { doc: {} };
	}

	componentWillMount() {

	}

	closeCase() {
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

		//TODOMSR: as observações dos campos estão aparecendo atras do modal - verificar no themes.less componente tooltip
		return (
			<FormModal
				show={this.props.show}
				title={__('cases.close') + ' - TODOMS CONCATENAR NOME DO PACIENTE'}
				type={'CustomCancel'}
				onClose={this.props.onClose}
				customBtnLbl={__('cases.close')}
				content={content} />
		);
	}
}

CaseClose.propTypes = {
	tbcase: React.PropTypes.object,
	show: React.PropTypes.bool,
	onClose: React.PropTypes.func
};
