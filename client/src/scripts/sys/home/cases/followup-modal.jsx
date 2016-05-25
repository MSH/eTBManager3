
import React from 'react';
import { FormDialog, Fa } from '../../../components/index';
import moment from 'moment';

import { getEditSchema } from './followup-edit-schemas';

/**
 * The page controller of the public module
 */
export default class FollowupModal extends React.Component {

	constructor(props) {
		super(props);

		this.state = { doc: {} };
	}

	renderTitle() {
		var title;

		if (this.props.opType === 'edt') {
			if (!this.props.doc) {
				return null;
			}

			var datefield = 'dateCollected';
			if (this.props.followUpType === 'MEDEXAM' || this.props.followUpType.type === 'HIV' || this.props.followUpType.type === 'XRAY') {
				datefield = 'date';
			}

			title = __('action.edit') + ' ';
			title = title + this.props.followUpName + ' ';
			title = title + moment(this.props.doc[datefield]).format('ll');
		} else if (this.props.opType === 'new') {
			title = __('cases.details.newresult') + ' ' + this.props.followUpName;
		}

		return (<span><Fa icon={this.props.followUpType === 'MEDEXAM' ? 'stethoscope' : 'file-text'} />{title}</span>);
	}

	render() {
		if (!this.props.opType || this.props.opType === 'del') {
			return null;
		}

		const fschema = getEditSchema(this.props.followUpType);
		fschema.title = this.renderTitle();

		return (
			<FormDialog
				schema={fschema}
				doc={this.props.doc}
				onCancel={this.props.onClose}
				wrapType={'modal'}
				modalShow={this.props.show}/>
		);
	}
}

FollowupModal.propTypes = {
	show: React.PropTypes.bool,
	onClose: React.PropTypes.func,
	opType: React.PropTypes.oneOf(['new', 'edt', 'del']),
	followUpType: React.PropTypes.string,
	followUpName: React.PropTypes.string,
	doc: React.PropTypes.object
};
