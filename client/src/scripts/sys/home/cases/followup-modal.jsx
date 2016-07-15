
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
		this.state = { doc: {}, showForm: true };
	}

	componentWillMount() {
		const op = this.props.operation;

		if (this.props.operation.opType === 'edt') {
			this.setState({ showForm: false });

			op.crud.getEdit(op.followUpId)
				.then(res => {
					// TODOMS: when date comes from server it is not being parsed on date control, this causes an error.
					res.date = new Date();
					if (res.dateRelease) {
						res.dateRelease = new Date();
					}
					this.setState({ doc: res, showForm: true });
				});
		}
	}

	save() {
		const op = this.props.operation;

		if (op.opType === 'new') {
			const doc = this.state.doc;
			doc.tbcaseId = op.tbcaseId;
			return op.crud.create(doc).then(() => {
				this.props.onClose('successNew');
			});
		}

		if (op.opType === 'edt') {
			return op.crud.update(this.props.operation.followUpId, this.state.doc).then(() => {
				this.props.onClose('successEdt');
			});
		}

		return null;
	}

	loadEditDoc(op) {
		this.setState({ showForm: false });

		return op.crud.getEdit(op.followUpId)
			.then(res => {
				this.setState({ doc: res, showForm: true });
			});
	}

	renderTitle(op) {
		let title;

		if (this.props.operation.opType === 'edt') {
			if (!this.state.doc) {
				return null;
			}

			title = __('action.edit') + ' ';
			title = title + op.followUpType.name + ' ';
			title = title + moment(this.state.doc.date).format('ll');
		} else if (this.props.operation.opType === 'new') {
			title = __('cases.details.newresult') + ' ' + op.followUpType.name;
		}

		return (<span><Fa icon={op.followUpType.icon} />{title}</span>);
	}

	render() {
		const op = this.props.operation;

		if (!this.props.operation || this.props.operation.opType === 'del' || this.state.doc === null) {
			return null;
		}

		const fschema = getEditSchema(this.props.operation.followUpType.id);
		fschema.title = this.renderTitle(op);

		return (
			<FormDialog
				schema={fschema}
				doc={this.state.doc}
				onCancel={this.props.onClose}
				onConfirm={this.save}
				wrapType={'modal'}
				modalShow={this.state.showForm}/>
		);
	}
}

FollowupModal.propTypes = {
	onClose: React.PropTypes.func,
	operation: React.PropTypes.object
};
