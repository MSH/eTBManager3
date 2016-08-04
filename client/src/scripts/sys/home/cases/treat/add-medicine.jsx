import React from 'react';
import { FormDialog } from '../../../../components';

export default class AddMedicine extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			schema: {
				title: __('Regimen.add'),
				controls: [
					{
						property: 'medicine',
						type: 'select',
						label: __('Medicine'),
						required: true,
						options: 'medicines',
						size: { md: 12 }
					},
					{
						property: 'ini',
						type: 'date',
						label: __('Period.iniDate'),
						required: true,
						size: { md: 6 }
					},
					{
						property: 'end',
						type: 'date',
						label: __('Period.endDate'),
						required: true,
						size: { md: 6 },
						validate: doc => doc.ini < doc.end,
						validateMessage: __('period.inienddate')
					},
					{
						property: 'doseUnit',
						type: 'select',
						label: __('PrescribedMedicine.doseUnit'),
						required: true,
						size: { md: 6 },
						options: {
							from: 1,
							to: 10
						}
					},
					{
						property: 'frequency',
						type: 'select',
						required: true,
						label: __('PrescribedMedicine.frequency'),
						size: { md: 6 },
						options: { from: 1, to: 7 }
					},
					{
						property: 'comments',
						type: 'text',
						label: __('global.comments')
					}
				]
			}
		};

		this.confirm = this.confirm.bind(this);
		this.cancel = this.cancel.bind(this);
	}

	/**
	 * Add the medicine to the treatment
	 * @param  {[type]} doc [description]
	 * @return {[type]}     [description]
	 */
	confirm(doc) {
		this.props.onClose(doc);
	}

	cancel() {
		this.props.onClose(null);
	}

	render() {
		const doc = this.props.doc;
		if (!doc) {
			return null;
		}

		return (
			<FormDialog
				schema={this.state.schema}
				doc={doc}
				wrapType="modal" modalShow
				onConfirm={this.confirm}
				onCancel={this.cancel} />
			);
	}
}

AddMedicine.propTypes = {
	doc: React.PropTypes.object,
	onClose: React.PropTypes.func
};
