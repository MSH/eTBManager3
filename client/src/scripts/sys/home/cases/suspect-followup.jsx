
import React from 'react';
import { server } from '../../../commons/server';
import { app } from '../../../core/app';

import { RemoteFormDialog } from '../../../components';

/**
 * The page controller of the public module
 */
export default class SuspectFollowUp extends React.Component {

	constructor(props) {
		super(props);

		this.state = { doc: {} };
		this.onConfirm = this.onConfirm.bind(this);
		this.getRemoteForm = this.getRemoteForm.bind(this);
	}

	getRemoteForm() {
		return server.get('/api/cases/case/suspectfollowup/initform/' + this.props.classification).then(res => {
			// prepare doc here
			return res;
		});
	}

	onConfirm() {
		const doc = this.state.doc;
		doc.tbcaseId = this.props.tbcase.id;

		return server.post('pathhere', doc)
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
		if (!this.props.classification) {
			return null;
		}

		return (
			<RemoteFormDialog
				wrapType="modal"
				remotePath={this.getRemoteForm}
				onCancel={this.props.onClose}
				onConfirm={this.onConfirm}
				modalShow={this.props.show} />
		);
	}
}

SuspectFollowUp.propTypes = {
	tbcase: React.PropTypes.object,
	show: React.PropTypes.bool,
	onClose: React.PropTypes.func,
	classification: React.PropTypes.string
};
