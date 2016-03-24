
import React from 'react';
import { FormDialog } from '../../components';

export default class CrudForm extends React.Component {

	constructor(props) {
		super(props);

		this.saveForm = this.saveForm.bind(this);
	}

	componentWillMount() {
		const self = this;
		this.props.controller.on(evt => {
			if (evt === 'new-form') {
				self.setState({ doc: {} });
			}

			if (evt === 'close-new-form') {
				self.forceUpdate();
			}
		});
	}

	saveForm(doc) {
		return this.props.controller.saveNewForm(doc);
	}

	render() {
		const controller = this.props.controller;

		return controller.isNewFormOpen() ?
			<FormDialog schema={this.props.schema}
				doc={this.state.doc}
				onConfirm={this.saveForm}
				onCancel={controller.cancelNewForm}/> :
			null;
	}
}

CrudForm.propTypes = {
	schema: React.PropTypes.object,
	controller: React.PropTypes.object.isRequired
};
