
import React from 'react';
import { FormDialog } from '../../../components/index';
import { server } from '../../../commons/server';
import { app } from '../../../core/app';
import SessionUtils from '../../session-utils';

const transfOutOnTreat = {
	controls: [
		{
			property: 'moveDate',
			required: true,
			type: 'date',
			label: __('cases.move.date'),
			defaultValue: new Date()
		},
		{
			property: 'unitToId',
			type: 'unit',
			unitType: 'TBUNIT',
			label: __('Tbunit'),
			required: true
		}
	]
};

const transfOutNotOnTreat = {
	controls: [
		{
			property: 'unitToId',
			type: 'unit',
			unitType: 'TBUNIT',
			label: __('Tbunit'),
			required: true
		}
	]
};

const transfIn = {
	controls: [
		{
			property: 'moveDate',
			required: true,
			type: 'date',
			label: __('cases.move.date'),
			defaultValue: new Date()
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
		this.onConfirm = this.onConfirm.bind(this);
	}

	onConfirm() {
		const doc = this.state.doc;
		doc.tbcaseId = this.props.tbcase.id;

		const api = this.props.tbcase.transferring ? '/api/cases/case/transferin' : '/api/cases/case/transferout';

		return server.post(api, doc)
				.then(res => {
					if (!res.success) {
						return Promise.reject(res.errors);
					}

					this.setState({ doc: {} });
					this.props.onClose();

					app.dispatch('case-update');

					return res.result;
				});
	}

	render() {
		let fschema;
		if (!this.props.tbcase.transferring) {
			fschema = this.props.tbcase.state === 'ONTREATMENT' ? transfOutOnTreat : transfOutNotOnTreat;
		} else {
			fschema = transfIn;
		}

		let title = this.props.tbcase.transferring ? __('cases.move.regtransferin') : __('cases.move');
		title = title + ' - ' + SessionUtils.nameDisplay(this.props.tbcase.patient.name);

		fschema.title = title;
		return (
			<FormDialog
				schema={fschema}
				doc={this.state.doc}
				onConfirm={this.onConfirm}
				onCancel={this.props.onClose}
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
