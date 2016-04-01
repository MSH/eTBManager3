
import React from 'react';
import { FormDialog } from '../../../components/index';

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

	/**
	 * [closeCase description]
	 * @return {boolean} if the case was closed with success
	 */
	closeCase(doc) {
		alert('TODOMS: go to server and close this case!' + doc);

		return false;
	}

	render() {
		//TODOMSR: as observações dos campos estão aparecendo atras do modal - verificar no themes.less componente tooltip
		return (
			<FormDialog
				schema={fschema}
				doc={this.state.doc}
				onConfirm={this.closeCase}
				onCancel={this.props.onCancel}
				confirmCaption={__('cases.close')}
				wrapType={'modal'}
				modalTitle={__('cases.close') + ' - ' + this.props.tbcase.patient.name}
				modalShow={this.props.show}/>
		);
	}
}

CaseClose.propTypes = {
	tbcase: React.PropTypes.object,
	show: React.PropTypes.bool,
	onCancel: React.PropTypes.func
};
