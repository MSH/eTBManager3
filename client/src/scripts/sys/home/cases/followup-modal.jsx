
import React from 'react';
import { FormDialog, Fa } from '../../../components/index';
import moment from 'moment';

import { getEditSchema, getFollowUpType } from './followup-utils';
import CRUD from '../../../commons/crud';

/**
 * The page controller of the public module
 */
export default class FollowupModal extends React.Component {

	constructor(props) {
		super(props);

		this.save = this.save.bind(this);
		this.state = { doc: {} };
	}

	save() {
		// initialize crud
		const crud = new CRUD(getFollowUpType(this.props.followUpType.id).crud);

		// set tbcaseId
		const nDoc = this.state.doc;
		nDoc.tbcaseId = this.props.tbcase.id;
		this.setState({ doc: nDoc });

		if (this.props.opType === 'new') {
			return crud.create(this.state.doc).then(() => this.props.onClose('success'));
		}

		if (this.props.opType === 'edt') {
			console.log('do it');
		}

		return null;
	}

	renderTitle() {
		var title;

		if (this.props.opType === 'edt') {
			if (!this.state.doc) {
				return null;
			}

			title = __('action.edit') + ' ';
			title = title + this.props.followUpType.name + ' ';
			title = title + moment(this.state.doc[this.props.followUpType.dateField]).format('ll');
		} else if (this.props.opType === 'new') {
			title = __('cases.details.newresult') + ' ' + this.props.followUpType.name;
		}

		return (<span><Fa icon={this.props.followUpType.icon} />{title}</span>);
	}

	render() {
		if (!this.props.opType || this.props.opType === 'del') {
			return null;
		}

		const fschema = getEditSchema(this.props.followUpType.id);
		fschema.title = this.renderTitle();

		return (
			<FormDialog
				schema={fschema}
				doc={this.state.doc}
				onCancel={this.props.onClose}
				onConfirm={this.save}
				wrapType={'modal'}
				modalShow={this.props.show}/>
		);
	}
}

FollowupModal.propTypes = {
	show: React.PropTypes.bool,
	onClose: React.PropTypes.func,
	opType: React.PropTypes.oneOf(['new', 'edt', 'del']),
	followUpType: React.PropTypes.object.isRequired,
	tbcase: React.PropTypes.object.isRequired
};
