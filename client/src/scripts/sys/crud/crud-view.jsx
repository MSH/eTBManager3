import React from 'react';
import { Button, Grid, Row, Col } from 'react-bootstrap';
import { Card } from '../../components';
import { hasPerm } from '../session';
import CrudMessage from './crud-message';
import CrudPagination from './crud-pagination';
import CrudGrid from './crud-grid';
import CrudController from './crud-controller';
import CrudForm from './crud-form';

/**
 * Aggregate all crud component offering a full stack crud editor
 */
export default class CrudView extends React.Component {

	constructor(props) {
		super(props);
		this.openNewForm = this.openNewForm.bind(this);
	}

	componentWillMount() {
		// the controller options
		const opts = {
			pageSize: this.props.pageSize,
			readOnly: !hasPerm(this.props.perm)
		};

		const controller = new CrudController(this.props.crud, opts);
		controller.initList();

		this.setState({ controller: controller });
	}

	openNewForm() {
		this.state.controller.openForm();
	}

	render() {
		const controller = this.state.controller;

		return (
			<div>
				<CrudForm controller={controller}
					schema={this.props.editorSchema} openOnNew />
				<Card title={this.props.title}>
					<Grid fluid>
						<Row>
							<Col sm={12}>
								<Button className="pull-right" onClick={this.openNewForm}>
									{__('action.add')}
								</Button>
							</Col>
						</Row>
					</Grid>
					<div className="mtop">
					<CrudMessage controller={controller} />
					{
						this.props.pageSize &&
						<CrudPagination controller={controller} showCounter />
					}
					<CrudGrid controller={controller}
						onRender={this.props.onCellRender}
						onExpandRender={this.props.onDetailRender}
						editorSchema={this.props.editorSchema} />
					{
						this.props.pageSize &&
						<CrudPagination controller={controller} />
					}
					</div>
				</Card>
			</div>
			);
	}
}


CrudView.propTypes = {
	title: React.PropTypes.string,
	editorSchema: React.PropTypes.object,
	onCellRender: React.PropTypes.func,
	onDetailRender: React.PropTypes.func,
	beforeEdit: React.PropTypes.func,
	cellSize: React.PropTypes.object,
	perm: React.PropTypes.string,
	crud: React.PropTypes.object.isRequired,
	search: React.PropTypes.bool,
	pageSize: React.PropTypes.number,
	queryFilters: React.PropTypes.object,
	// if true, the card will have no bottom margin
	combine: React.PropTypes.bool,
	children: React.PropTypes.node
};

CrudView.defaultProps = {
	search: false,
	paging: false,
	cellSize: { md: 6 }
};
