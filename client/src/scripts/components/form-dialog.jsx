
import React from 'react';
import { ButtonToolbar, Button } from 'react-bootstrap';
import Card from './card';
import Form from './form';
import AsyncButton from './async-button';

/**
 * The page controller of the public module
 */
export default class FormDialog extends React.Component {

	constructor(props) {
		super(props);
		this.state = {};
		this.confirmClick = this.confirmClick.bind(this);
		this.cancelClick = this.cancelClick.bind(this);
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
		const errors = Form.validate(this.props.formDef.layout, this.props.doc);

		// there are validation errors?
		if (errors) {
			return this.setState({ errors: errors });
		}

		this.setState({ errors: null, fetching: true });

		// the promise to be called when confirming
		let prom;
		if (this.props.onConfirm) {
			prom = this.props.onConfirm();
		}

		// it is expected that a promise is returned, in order to be informed about errors
		if (prom && prom.then) {
			const self = this;

			// wait for the end of the promise
			prom.then(() => self.mounted && self.setState({ fetching: false }))
				.catch(res => self.mounted && self.setState({ errors: res, fetching: false }));
		}
	}

	cancelClick() {
		if (this.props.onCancel) {
			this.props.onCancel();
		}
	}

	render() {
		const formDef = this.props.formDef;
		if (!formDef) {
			return null;
		}

		// get instance to be edited
		const doc = this.props.doc;

		// get the layout of the form
		const layout = formDef.layout;

		// get the title of the form
		let title;
		if (typeof formDef.title === 'function') {
			title = formDef.title(doc);
		}
		else {
			title = formDef.title;
		}

		// get validation errors, if any available
		const errors = this.state ? this.state.errors : null;

		// get the caption to be displayed in the confirm button
		let confirmCaption = this.props.confirmCaption;
		if (!confirmCaption) {
			confirmCaption = __('action.save');
		}

		return (
			<Card title={title} >
				<div>
					<Form layout={layout} doc={doc} errors={errors}/>
				</div>
				<ButtonToolbar>
					<AsyncButton fetching={this.state.fetching} faIcon="check"
						bsStyle="primary"
						onClick={this.confirmClick}>{confirmCaption}
					</AsyncButton>
					<Button onClick={this.cancelClick}>
						<i className="fa fa-times fa-fw"/>{__('action.cancel')}
					</Button>
				</ButtonToolbar>
			</Card>
			);
	}
}

FormDialog.propTypes = {
	formDef: React.PropTypes.object,
	doc: React.PropTypes.object,
	onConfirm: React.PropTypes.func,
	onCancel: React.PropTypes.func,
	confirmCaption: React.PropTypes.any
};

FormDialog.defaultProps = {
	dataModel: {}
};

