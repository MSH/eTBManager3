
import React from 'react';
import { ButtonToolbar, Button } from 'react-bootstrap';
import { Card } from '../../components/index';
import CRUD from '../../commons/crud';
import Form from '../../components/form';

const crud = new CRUD('substance');

const layout = [
	{
		property: 'shortName',
		required: true,
		type: 'string',
		max: 20,
		label: __('form.shortName'),
		size: { sm: 3 }
	},
	{
		property: 'name',
		required: true,
		type: 'string',
		max: 200,
		label: __('form.name'),
		size: { sm: 6 }
	}
];


/**
 * The page controller of the public module
 */
export default class SubstanceEdt extends React.Component {

	constructor(props) {
		super(props);
		this.saveClick = this.saveClick.bind(this);
		this.cancelClick = this.cancelClick.bind(this);
	}

	saveClick() {
		const errors = Form.validate(layout, this.props.doc);

		// there are validation errors?
		if (errors) {
			return this.setState({ errors: errors });
		}

		crud.create(this.props.doc)
		.then(res => {
			if (this.props.onSave) {
				this.props.onSave(res);
			}
		});
	}

	cancelClick() {
		if (this.props.onCancel) {
			this.props.onCancel();
		}
	}

	render() {

		const doc = this.props.doc;
		const title = doc && doc.id ? __('admin.substances.edt') : __('admin.substances.new');
		const errors = this.state ? this.state.errors : null;

		return (
			<Card title={title} >
				<div>
					<Form layout={layout} dataModel={this.props.doc} errors={errors}/>
				</div>
				<ButtonToolbar>
					<Button bsStyle="primary" onClick={this.saveClick}>{__('action.save')}</Button>
					<Button onClick={this.cancelClick}>{__('action.cancel')}</Button>
				</ButtonToolbar>
			</Card>
			);
	}
}

SubstanceEdt.propTypes = {
	doc: React.PropTypes.object,
	onSave: React.PropTypes.func,
	onCancel: React.PropTypes.func
};

SubstanceEdt.defaultProps = {
	doc: {}
};

