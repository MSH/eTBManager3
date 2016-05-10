
import React from 'react';
import { Button, Row, Col, Alert, Badge, DropdownButton, MenuItem } from 'react-bootstrap';
import { Card, WaitIcon, Fa, GridTable } from '../components/index';
import { objEqual } from '../commons/utils';


/**
 * Table view for standard CRUD operations
 */
export default class CrudCard extends React.Component {

	constructor(props) {
		super(props);
		this.state = {};
		this.newClick = this.newClick.bind(this);
		this.newMenuClick = this.newMenuClick.bind(this);
	}

	shouldComponentUpdate(nextProps) {
		return !objEqual(nextProps, this.props);
	}


	/**
	 * Called when new is clicked
	 * @return {[type]} [description]
	 */
	newClick() {
		this.raiseEvent({ type: 'new' });
	}

	newMenuClick(evt, key) {
		this.raiseEvent({ type: 'new', key: key });
	}


	/**
	 * Raise an event that triggers the 'onEvent' property of the table
	 * @param  {object} evt The event to be triggered
	 */
	raiseEvent(evt) {
		if (this.props.onEvent) {
			this.props.onEvent(evt);
		}
	}

	/**
	 * Create the new button
	 * @return {React.Component} The new button, or null if user has no permission to create a new item
	 */
	createNewButton() {
		if (this.props.readOnly) {
			return null;
		}

		// render the button title
		const title = <span><Fa icon="plus"/>{__('action.add')}</span>;

		// get the content of the new button
		let content;
		if (this.props.newDropDown) {

			content = (
				<DropdownButton id="optMenu" bsSize="small" pullRight
					title={title}
					onSelect={this.newMenuClick}>
					{
						this.props.newDropDown.map(item =>
							<MenuItem key={item.key} eventKey={item.key}>
								{item.label}
							</MenuItem>)
					}
				</DropdownButton>
				);
		}
		else {
			content = (
				<Button bsStyle="default" bsSize="small" onClick={this.newClick}>
					{title}
				</Button>
				);
		}

		return (
			<Col xs={4} sm={6} md={2}>
				<div className="pull-right">
					{content}
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
		const values = this.props.values;

		return (
			<GridTable values={values.list}
				onCellRender={this.props.onCellRender}
				onCellSize={this.props.onCellSize}
				/>
			);
	}


	/**
	 * Render the header of the card
	 * @return {[type]} [description]
	 */
	headerRender() {
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

		const count = this.props.values ? this.props.values.count : 0;
		const compCount = count > 0 ? <Badge className="tbl-counter">{count}</Badge> : null;

		// create the header of the card
		return (
			<Row>
				<Col {...colProps}>
					<h4>{this.props.title} {compCount}</h4>
				</Col>
				{searchBox}
				{newButton}
			</Row>
			);
	}

	/**
	 * Rende the table view
	 * @return {[type]} [description]
	 */
	render() {
		// create the header of the card
		const header = this.headerRender();

		// get the list of values
		const res = this.props.values;

		// create the content of the card
		let content;

		// if there is no value, then show the wait icon (because it is loading)
		if (!res) {
			content = <WaitIcon type="card" />;
		}
		else if (res.length === 0) {
			content = <Alert bsStyle="warning">{__('form.norecordfound')}</Alert>;
		}
		else {
			// if records were found, create the table content
			content = this.createTable();
		}


		return (
			<Card header={header} padding={this.props.combine ? 'combine' : null}>
				{content}
			</Card>
			);
	}
}


CrudCard.propTypes = {
	title: React.PropTypes.string,
	values: React.PropTypes.object,
	paging: React.PropTypes.bool,
	search: React.PropTypes.bool,
	onCellRender: React.PropTypes.func,
	onCellSize: React.PropTypes.func,
	// the custom filters to be included in the panel
	filterView: React.PropTypes.func,
	// if true, the new button will not be available
	readOnly: React.PropTypes.bool,
	// event fired when user clicks on the new button
	onEvent: React.PropTypes.func,
	// list of key / label to display in a new drop down
	newDropDown: React.PropTypes.array,
	// if true, card will have no bottom margin
	combine: React.PropTypes.bool
};

CrudCard.defaultProps = {
	cellSize: { md: 6 }
};

