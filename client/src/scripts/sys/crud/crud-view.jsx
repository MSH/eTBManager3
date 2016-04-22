import React from 'react';
import { Row, Col } from 'react-bootstrap';
import { Card } from '../../components';
import { hasPerm } from '../session';
import CrudMessage from './crud-message';
import CrudPagination from './crud-pagination';
import CrudGrid from './crud-grid';
import CrudController from './crud-controller';
import CrudForm from './crud-form';
import CrudCounter from './crud-counter';
import CrudAddButton from './crud-add-button';

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
					schema={this.props.editorSchema} openOnNew
					wrapType={this.props.modal ? 'modal' : 'card'} />
				<Card title={this.props.title}>
					<Row>
						<Col sm={12}>
							<CrudAddButton controller={controller} />
							<CrudCounter controller={controller} />
						</Col>
					</Row>
					<Row>
						<Col sm={12}>
							<div className="mtop">
							<CrudMessage controller={controller} />
							{
								this.props.pageSize &&
								<CrudPagination controller={controller} />
							}
							<CrudGrid controller={controller}
								cellSize={this.props.cellSize}
								options={this.props.options}
								onRender={this.props.onCellRender}
								onExpandRender={this.props.onDetailRender}
								editorSchema={this.props.editorSchema}
								modal={this.props.modal} />
							{
								this.props.pageSize &&
								<CrudPagination controller={controller} />
							}
							</div>
						</Col>
					</Row>
				</Card>
			</div>
			);
	}
}


CrudView.propTypes = {
	title: React.PropTypes.string,
	editorSchema: React.PropTypes.object.isRequired,
	onCellRender: React.PropTypes.func.isRequired,
	onDetailRender: React.PropTypes.func,
	cellSize: React.PropTypes.object,
	perm: React.PropTypes.string,
	crud: React.PropTypes.object.isRequired,
	search: React.PropTypes.bool,
	pageSize: React.PropTypes.number,
	queryFilters: React.PropTypes.object,
	options: React.PropTypes.array,
	modal: React.PropTypes.modal,
	// if true, the card will have no bottom margin
	combine: React.PropTypes.bool,
	children: React.PropTypes.node
};

CrudView.defaultProps = {
	search: false,
	modal: false,
	paging: false,
	cellSize: { md: 6 }
};
