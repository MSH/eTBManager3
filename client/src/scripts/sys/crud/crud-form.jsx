
import React from 'react';
import { FormDialog } from '../../components';
import controlWrapper from './crud-control-wrapper';

class CrudForm extends React.Component {

	componentWillMount() {
		this.setState({ visible: this.props.controller.isFormOpen() });
	}

	eventHandler(evt, data) {
		if (evt !== 'open-form' && evt !== 'close-form') {
			return;
		}

		const onnew = this.props.openOnNew;

		// ignore events if not on new forms
		if (!onnew || (onnew && data && data.id)) {
			return;
		}

		this.setState({ visible: evt === 'open-form' });
	}


	render() {
		if (!this.state.visible) {
			return null;
		}

		const controller = this.props.controller;

		return (
			<FormDialog schema={this.props.schema}
				doc={controller.frm.doc}
				onConfirm={controller.saveAndClose}
				wrapType={this.props.wrapType}
				onCancel={controller.closeForm}
				className={this.props.className}
				/>
			);
	}
}

CrudForm.propTypes = {
	schema: React.PropTypes.object,
	controller: React.PropTypes.object.isRequired,
	openOnNew: React.PropTypes.bool,
	openOnEdit: React.PropTypes.bool,
	wrapType: React.PropTypes.string,
	className: React.PropTypes.string
};

CrudForm.defaultProps = {
	wrapType: 'card'
};

export default controlWrapper(CrudForm);
