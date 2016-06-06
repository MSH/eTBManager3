
import React from 'react';
import { FormDialog, Fa } from '../../../components/index';
import moment from 'moment';

import { getEditSchema } from './followup-utils';

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
		console.log('go to server and save it! Dont forget to return a promise');
		this.props.onClose();
	}

	renderTitle() {
		var title;

		if (this.props.opType === 'edt') {
			if (!this.props.doc) {
				return null;
			}

			title = __('action.edit') + ' ';
			title = title + this.props.followUpType.name + ' ';
			title = title + moment(this.props.doc[this.props.followUpType.dateField]).format('ll');
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
				doc={this.props.doc}
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
	doc: React.PropTypes.object
};
