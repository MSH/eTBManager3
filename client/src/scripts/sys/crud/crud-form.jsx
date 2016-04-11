
import React from 'react';
import { FormDialog } from '../../components';

export default class CrudForm extends React.Component {

	constructor(props) {
		super(props);
	}

	componentWillMount() {
		const self = this;
		const handler = this.props.controller.on(evt => {
			if (evt === 'open-form' || evt === 'close-form') {
				// check if this form should handle events
				if (self.isFormVisible() && this.props.openOnNew) {
					self.forceUpdate();
				}
			}
		});
		this.setState({ handler: handler });
	}

	componentWillUnmount() {
		this.props.controller.removeListener(this.state.handler);
	}

	/**
	 * Return true if form is visible
	 * @return {Boolean} [description]
	 */
	isFormVisible() {
		const ctrl = this.props.controller;
		return ctrl.isFormOpen() &&
			((ctrl.isNewForm() && this.props.openOnNew) || (!ctrl.isNewForm() && this.props.openOnEdit));
	}

	render() {
		if (!this.isFormVisible()) {
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
