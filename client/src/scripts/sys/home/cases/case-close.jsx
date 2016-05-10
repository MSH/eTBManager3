
import React from 'react';
import { FormDialog } from '../../../components/index';

const fschema = {
			title: __('cases.close'),
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
					type: 'group',
					visible: value => value.outcome === 'OTHER',
					layout: [
						{
							property: 'otherOutcome',
							type: 'string',
							label: __('CaseState.OTHER')
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

	closeCase() {
		console.log('go to server and close this case! Dont forget to return a promise');
	}

	render() {
		fschema.title = __('cases.close') + ' - ' + this.props.tbcase.patient.name;
		return (
			<FormDialog
				schema={fschema}
				doc={this.state.doc}
				onConfirm={this.closeCase}
				onCancel={this.props.onClose}
				confirmCaption={__('cases.close')}
				wrapType={'modal'}
				modalShow={this.props.show}/>
		);
	}
}

CaseClose.propTypes = {
	tbcase: React.PropTypes.object,
	show: React.PropTypes.bool,
	onClose: React.PropTypes.func
};
