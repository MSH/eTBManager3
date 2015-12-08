
import React from 'react';
import { Table, Button, Row, Col, Alert, Badge } from 'react-bootstrap';
import { Card } from '../../components/index';


/**
 * Table view for standard CRUD operations
 */
export default class TableView extends React.Component {

	constructor(props) {
		super(props);
		this.state = {};
		this.newClick = this.newClick.bind(this);
	}


	/**
	 * Called when new is clicked
	 * @return {[type]} [description]
	 */
	newClick() {
		this.raiseEvent({ type: 'new' });
	}

	raiseEvent(evt) {
		if (this.props.onEvent) {
			this.props.onEvent(evt);
		}
	}

	/**
	 * Create the new button
	 * @return {Component} The new button, or null if user has no permission to create a new item
	 */
	createNewButton() {
		if (this.props.readOnly) {
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
		const res = this.props.data;
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
		if (!this.props) {
			return null;
		}

		const res = this.props.data;

		if (!res) {
			return null;
		}

		let content;

		// no record was found ?
		if (res.list.length === 0) {
			content = <Alert bsStyle="warning">{'No record found'}</Alert>;
		}
		else {
			// if records were found, create the table content
			content = this.createTable();
		}

		const newButton = this.createNewButton();
		const searchBox = this.createSearchBox();

		// the size of the title column
		const colProps = {
			xs: 12,
			sm: 12,
			md: 12
		};
		// adjust the size of the title according to the search box and new button
		colProps.md += (searchBox ? -3 : 0) + (newButton ? -2 : 0);

		const count = res.count > 0 ? <Badge className="tbl-counter">{res.count}</Badge> : null;

		// create the header of the card
		const header = (
			<Row>
				<Col {...colProps}>
					<h4>{this.props.tableDef.title}{count}</h4>
				</Col>
				{searchBox}
				{newButton}
			</Row>
			);

		return (
			<Card header={header}>
				{content}
			</Card>
			);
	}
}

TableView.propTypes = {
	title: React.PropTypes.string,
	paging: React.PropTypes.bool,
	search: React.PropTypes.bool,
	// the custom filters to be included in the panel
	filterView: React.PropTypes.func,
	// if true, the new button will not be available
	readOnly: React.PropTypes.bool,
	// layout of the table
	tableDef: React.PropTypes.object,
	// event fired when user clicks on the new button
	onEvent: React.PropTypes.func,
	// data to be displayed
	data: React.PropTypes.object
};
