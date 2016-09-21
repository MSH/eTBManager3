
import React from 'react';
import { ButtonToolbar, Button, Modal } from 'react-bootstrap';
import Card from './card';
import Form from '../forms/form';
import { isFunction } from '../commons/utils';
import AsyncButton from './async-button';
import RemoteForm from './remote-form';

/**
 * The page controller of the public module
 */
export default class FormDialog extends React.Component {

	constructor(props) {
		super(props);
		this.confirmClick = this.confirmClick.bind(this);
		this.remoteFormLoad = this.remoteFormLoad.bind(this);

		const title = this.props.schema ? this.props.schema.title : undefined;
		this.state = { title: title, remoteFormMounted: false };
	}

	componentDidMount() {
		this._mounted = true;
	}

	componentWillUnmount() {
		this._mounted = false;
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
			prom
				.then(() => this._mounted && self.setState({ fetching: false }))
				.catch(res => {
					return this._mounted && self.setState({ errors: res, fetching: false });
				});
		}
	}

	/**
	 * Called when using remoteForm to flag when it is mounted
	 * @param {[string, function, node]} title The title that will be used on dialog
	 */
	remoteFormLoad(schema) {
		this.setState({ title: schema.title, remoteFormMounted: true });
	}

	render() {
		const schema = this.props.schema;
		const remotePath = this.props.remotePath;

		if (!schema && !remotePath) {
			return null;
		}

		// get instance to be edited
		const doc = this.props.doc;

		// get the title of the form
		const title = isFunction(this.state.title) ? this.state.title(doc) : this.state.title;

		// get validation errors, if any available
		const errors = this.state ? this.state.errors : null;

		let form;
		if (remotePath) {
			form = (
					<RemoteForm ref="form"
						onLoadForm={this.remoteFormLoad}
						remotePath={remotePath}
						errors={errors} />
					);
		} else {
			form = (
					<Form ref="form" schema={schema}
						onInit={this.props.onInit}
						doc={doc} errors={errors}
						resources={this.props.resources}/>
					);
		}

		// if it is a remoteForm and it is not mounted, don't display buttons
		const buttons = remotePath && !this.state.remoteFormMounted ? null : (
			<div className="mtop">
				<ButtonToolbar>
					<AsyncButton fetching={this.state.fetching} faIcon="check"
						bsStyle="success"
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
	schema: React.PropTypes.object,
	doc: React.PropTypes.object,
	onConfirm: React.PropTypes.func,
	onCancel: React.PropTypes.func,
	onInit: React.PropTypes.func,
	confirmCaption: React.PropTypes.any,
	resources: React.PropTypes.object,
	wrapType: React.PropTypes.oneOf(['modal', 'card', 'none']),
	hideCancel: React.PropTypes.bool,
	className: React.PropTypes.string,

	modalShow: React.PropTypes.bool,
	modalBsSize: React.PropTypes.string,

	remotePath: React.PropTypes.any
};

FormDialog.defaultProps = {
	highlight: false,
	confirmCaption: __('action.save'),
	wrapType: 'none'
};
