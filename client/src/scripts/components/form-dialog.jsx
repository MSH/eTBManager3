
import React from 'react';
import { ButtonToolbar, Button, Modal } from 'react-bootstrap';
import Card from './card';
import Form from '../forms/form';
import { isFunction } from '../commons/utils';
import AsyncButton from './async-button';

/**
 * The page controller of the public module
 */
export default class FormDialog extends React.Component {

	constructor(props) {
		super(props);
		this.state = {};
		this.confirmClick = this.confirmClick.bind(this);
	}

	/**
	 * Called when the user clicks on the confirm button
	 */
	confirmClick() {
		const errors = this.refs.form.validate();

		// there are validation errors?
		if (errors) {
			this.setState({ errors: errors });
			return;
		}

		this.setState({ errors: null, fetching: true });

		// the promise to be called when confirming
		let prom;
		if (this.props.onConfirm) {
			prom = this.props.onConfirm(this.props.doc);
		}

		// it is expected that a promise is returned, in order to be informed about errors
		if (prom && prom.then) {
			const self = this;

			// wait for the end of the promise
			prom.then(() => self.setState({ fetching: false }))
				.catch(res => self.setState({ errors: res, fetching: false }));
		}
	}

	render() {
		const schema = this.props.schema;
		if (!schema) {
			return null;
		}

		// get instance to be edited
		const doc = this.props.doc;

		// get the title of the form
		const title = isFunction(schema.title) ? schema.title(doc) : schema.title;

		// get validation errors, if any available
		const errors = this.state ? this.state.errors : null;

		const form = (
				<Form ref="form" schema={schema}
					onInit={this.props.onInit}
					doc={doc} errors={errors}
					resources={this.props.resources}/>
				);

			const buttons = (
				<div className="mtop">
					<ButtonToolbar>
						<AsyncButton fetching={this.state.fetching} faIcon="check"
							bsStyle="primary"
							onClick={this.confirmClick}>{this.props.confirmCaption}
						</AsyncButton>
						{
							!this.props.hideCancel &&
							<Button onClick={this.props.onCancel} disabled={this.state.fetching}>
								<i className="fa fa-times fa-fw"/>{__('action.cancel')}
							</Button>
						}
					</ButtonToolbar>
				</div>
				);

		switch (this.props.wrapType) {
			case 'card': return (
					<Card title={title} className={this.props.className}>
						{form} {buttons}
					</Card>
				);
			case 'modal': return (
					<Modal show={this.props.modalShow} bsSize={this.props.modalBsSize}>
						<Modal.Header>
							<Modal.Title>
								{title}
							</Modal.Title>
						</Modal.Header>
						<Modal.Body>
							{form}
						</Modal.Body>
						<Modal.Footer>
							{buttons}
						</Modal.Footer>
					</Modal>
				);
			default: return (
					<div className={this.props.className}>
						{
							title && <h3>{title}</h3>
						}
						{form} {buttons}
					</div>
				);
		}
	}
}

FormDialog.propTypes = {
	schema: React.PropTypes.object.isRequired,
	doc: React.PropTypes.object.isRequired,
	onConfirm: React.PropTypes.func,
	onCancel: React.PropTypes.func,
	onInit: React.PropTypes.func,
	confirmCaption: React.PropTypes.any,
	resources: React.PropTypes.object,
	wrapType: React.PropTypes.oneOf(['modal', 'card', 'none']),
	hideCancel: React.PropTypes.bool,
	className: React.PropTypes.string,

	modalShow: React.PropTypes.bool,
	modalBsSize: React.PropTypes.string
};

FormDialog.defaultProps = {
	highlight: false,
	confirmCaption: __('action.save'),
	wrapType: 'none'
};
