
import React from 'react';
import { ButtonToolbar, Button, Input, Grid, Row, Col } from 'react-bootstrap';
import { Card } from '../../components/index';
import CRUD from '../../commons/crud';

const crud = new CRUD('source');

/**
 * The page controller of the public module
 */
export default class SourceEdt extends React.Component {

	constructor(props) {
		super(props);
		this.saveClick = this.saveClick.bind(this);
		this.cancelClick = this.cancelClick.bind(this);
	}

	saveClick() {
		const data = {
			name: this.refs.name.getValue(),
			shortName: this.refs.shortName.getValue()
		};

		crud.create(data)
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
		const title = doc && doc.id ? __('admin.sources.edit') : __('admin.sources.new');
		return (
			<Card title={title} >
				<div>
					<Grid fluid>
						<Row>
							<Col lg={2} md={3}>
								<Input ref="shortName" label={__('form.shortName') + ':'} type="text" />
							</Col>
							<Col lg={4} md={6}>
								<Input ref="name" label={__('form.name') + ':'} type="text" />
							</Col>
						</Row>
					</Grid>
				</div>
				<ButtonToolbar>
					<Button bsStyle="primary" onClick={this.saveClick}>{__('action.save')}</Button>
					<Button onClick={this.cancelClick}>{__('action.cancel')}</Button>
				</ButtonToolbar>
			</Card>
			);
	}
}

SourceEdt.propTypes = {
	doc: React.PropTypes.object,
	onSave: React.PropTypes.func,
	onCancel: React.PropTypes.func
};
