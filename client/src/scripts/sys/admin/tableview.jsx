
import React from 'react';
import { Table, Button, Row, Col, Alert } from 'react-bootstrap';
import { Card, WaitIcon } from '../../components/index';
import { hasPerm } from '../../core/session';


/**
 * Table view for standard CRUD operations
 */
export default class TableView extends React.Component {

	constructor(props) {
		super(props);
		this.state = {};
		this.newClick = this.newClick.bind(this);
	}

	fetchData(crud) {
		const self = this;
		crud.query({ rootUnits: true })
		.then(result => self.setState({ result: result }));

	}

	/**
	 * Called when the new button is clicked
	 */
	newClick() {
		this.setState({ editing: true, doc: {} });
	}

	/**
	 * Create the new button
	 * @return {Component} The new button, or null if user has no permission to create a new item
	 */
	createNewButton() {
		if (!hasPerm(this.props.perm)) {
			return null;
		}

		return (
			<Col xs={4} sm={6} md={2}>
				<div className="pull-right">
					<Button bsStyle="default" bsSize="small" onClick={this.newClick}><i className="fa fa-plus"/>{'New'}</Button>
				</div>
			</Col>
			);
	}

	/**
	 * Create the search control
	 * @return {[type]} [description]
	 */
	createSearchBox() {
		if (!this.props.search) {
			return null;
		}

		return (
				<Col xs={8} sm={6} md={3}>
					<div className="input-group">
						<input type="text" placeholder="Search" className="input-sm form-control" />
						<span className="input-group-btn">
							<button type="button" className="btn btn-sm btn-default">{__('action.go')}</button>
						</span>
					</div>
				</Col>
				);
	}

	/**
	 * Create the table that will display the result
	 * @return {[type]} [description]
	 */
	createTable() {
		const tbldef = this.props.tableDef;
		const res = this.state.result;
		if (!tbldef && !res) {
			return null;
		}

		// create table titles
		const titles = (
			<tr>
				{
					tbldef.columns.map(col => <th key={col.property} align={col.align}>{col.title}</th>)
				}
			</tr>
			);

		// create table rows
		const rows = res.list.map(item => (
				<tr key={item.id}>
					{tbldef.columns.map(col => <td key={item.id + col.property}>{item[col.property]}</td>)}
				</tr>
			));

		return (
			<Table responsive>
				<thead>
					{titles}
				</thead>
				<tbody>
					{rows}
				</tbody>
			</Table>
			);
	}

	/**
	 * Rende the table view
	 * @return {[type]} [description]
	 */
	render() {
		const crud = this.props.crud;
		const editForm = this.props.editForm;
		const title = this.props.title;
		const res = this.state ? this.state.result : null;

		let content;

		// check if data must be fetched from the server
		if (!res) {
			this.fetchData(crud);
			return <WaitIcon />;
		}

		if (res.list.length === 0) {
			content = <Alert bsStyle="warning">{'No record found'}</Alert>;
		}
		else {
			content = this.createTable();
		}

		const newButton = this.createNewButton();
		const searchBox = this.createSearchBox();

		// the title column properties (size will vary according to the elements there)
		const colProps = {
			xs: 12,
			sm: 12,
			md: 12
		};
		colProps.md += (searchBox ? -3 : 0) + (newButton ? -2 : 0);

		// create the header of the table
		const header = (
			<Row>
				<Col {...colProps}>
					<h4>{title}</h4>
				</Col>
				{searchBox}
				{newButton}
			</Row>
			);

		// is it in editing mode ?
		const Editor = this.state.editing ? this.props.editForm : null;

		return (
			<div>
				{Editor && <Editor/>}
				<Card header={header}>
					{content}
				</Card>
			</div>
			);
	}
}

TableView.propTypes = {
	crud: React.PropTypes.object,
	title: React.PropTypes.string,
	paging: React.PropTypes.bool,
	search: React.PropTypes.bool,
	// the custom filters to be included in the panel
	filterView: React.PropTypes.func,
	// the edit form component
	editForm: React.PropTypes.func,
	// the permission to test to enable editing
	perm: React.PropTypes.string,
	// layout of the table
	tableDef: React.PropTypes.object
};
