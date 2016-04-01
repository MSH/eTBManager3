
import React from 'react';
import { ButtonToolbar, Button, Modal } from 'react-bootstrap';
import Card from './card';
import Form from '../forms/form';
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

	componentDidMount() {
		this.mounted = true;
	}

	componentWillUnmount() {
		this.mounted = false;
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
			prom.then(() => self.mounted && self.setState({ fetching: false }))
				.catch(res => self.mounted && self.setState({ errors: res, fetching: false }));
			}
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
		let title;
		if (typeof schema.title === 'function') {
			title = schema.title(doc);
		}
		else {
			title = schema.title;
		}

		// get validation errors, if any available
		const errors = this.state ? this.state.errors : null;

		const form = (
				<div>
					<Form ref="form" schema={schema}
						onInit={this.props.onInit}
						doc={doc} errors={errors}
						resources={this.props.resources}/>
				</div>
				);

			const buttons = (
				<div>
					<ButtonToolbar>
						<AsyncButton fetching={this.state.fetching} faIcon="check"
							bsStyle="primary"
							onClick={this.confirmClick}>{this.props.confirmCaption}
						</AsyncButton>
						<Button onClick={this.props.onCancel}>
							<i className="fa fa-times fa-fw"/>{__('action.cancel')}
						</Button>
					</ButtonToolbar>
				</div>
				);

		switch (this.props.wrapType) {
			case 'card': return (
					<Card title={title} highlight={this.props.highlight}>
						{form}{buttons}
					</Card>
				);
			case 'modal': return (
					<Modal show={this.props.modalShow} onHide={this.cancelClick} bsSize={this.props.modalBsSize}>
						<Modal.Header closeButton>
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
					<div>{form}{buttons}</div>
				);
		}
	}
}

FormDialog.propTypes = {
	schema: React.PropTypes.object,
	doc: React.PropTypes.object,
	onConfirm: React.PropTypes.func,
	onCancel: React.PropTypes.func,
	onInit: React.PropTypes.func,
	confirmCaption: React.PropTypes.any,
	highlight: React.PropTypes.bool,
	resources: React.PropTypes.object,
	wrapType: React.PropTypes.oneOf(['modal', 'card', 'none']),

	modalShow: React.PropTypes.bool,
	modalBsSize: React.PropTypes.string
};

FormDialog.defaultProps = {
	highlight: false,
	confirmCaption: __('action.save'),
	wrapType: 'none'
};

