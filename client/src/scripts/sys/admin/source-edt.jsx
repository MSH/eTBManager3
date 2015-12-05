
import React from 'react';
import { ButtonToolbar, Button, Input, Grid, Row, Col } from 'react-bootstrap';
import { Card } from '../../components/index';
import CRUD from '../../commons/crud';

const crud = new CRUD('source');

/**
 * The page controller of the public module
 */
export default class SourceEdt extends React.Component {

	render() {
		const doc = this.props.doc;
		const title = doc && doc.id ? __('admin.sources.edit') : __('admin.sources.new');
		return (
			<Card title={title} >
				<div>
					<Grid>
						<Row>
							<Col lg={4} md={6}>
								<Input label={__('form.name') + ':'} type="text" />
							</Col>
						</Row>
					</Grid>
				</div>
				<ButtonToolbar>
					<Button bsStyle="primary">{__('action.save')}</Button>
					<Button>{__('action.cancel')}</Button>
				</ButtonToolbar>
			</Card>
			);
	}
}

SourceEdt.propTypes = {
	doc: React.PropTypes.object
};
