/**
 * Complete component to offer CRUD capabilities to an entity. It displays the content in a table
 * and offers controls to create new items, delete and edit them
 */

import React from 'react';
import { Collapse, Alert, Button, ButtonToolbar } from 'react-bootstrap';
import FormDialog from '../../components/form-dialog';
import Form from '../../forms/form';
import { hasPerm } from '../session';
import { MessageDlg, CollapseCard, AsyncButton } from '../../components/index';
import CrudCard from './crud-card';


export default class CrudView extends React.Component {

	constructor(props) {
		super(props);
		// set the initial state
		this.state = { readOnly: !hasPerm(this.props.perm) };

		// function binding
		this.getCellSize = this.getCellSize.bind(this);
		this.cellRender = this.cellRender.bind(this);
		this.editClick = this.editClick.bind(this);
		this.deleteClick = this.deleteClick.bind(this);
		this.cardEventHandler = this.cardEventHandler.bind(this);
		this.deleteConfirm = this.deleteConfirm.bind(this);
	}

	componentWillMount() {
		this.refreshTable();
	}

	/**
	 * Called when the crud card wants to update its content
	 * @return {[type]} [description]
	 */
	refreshTable() {
		const self = this;

		return this.props.crud.query({ })
		.then(res => {
			// wrap itens inside a controller object
			const list = res.list.map(item => ({ data: item, state: 'ok' }));
			// generate new result
			const result = { count: res.count, list: list };
			// set state
			self.setState({ values: result });
			// return to the promise
			return result;
		});
	}


	cellRender(item, index) {
		// is in edit mode ?
		if (item.state === 'edit') {
			// create a context that will be used when clicking on the save or cancel button
			const context = { self: this, item: item };
			const func = this.formEventHandler.bind(context);

			// display cell for editing
			return (
				<Collapse in transitionAppear>
					<div>
					<FormDialog formDef={this.props.editorDef}
						doc={item.data}
						highlight
						onEvent={func}
						/>
					</div>
				</Collapse>
				);
		}

		const content = this.props.onCellRender(item.data);

		// generate the hidden content
		const colRender = this.props.onCollapseCellRender;
		let colContent = colRender ? colRender(item.data) : null;
		colContent = (
				<div>
					{colContent}
					<ButtonToolbar className="mtop">
						<AsyncButton bsStyle="primary"
							fetching={item.state === 'fetching'}
							data-item={index}
							onClick={this.editClick}>{__('action.edit')}</AsyncButton>
						<Button bsStyle="link"
							onClick={this.deleteClick}
							data-item={index}>{__('action.delete')}</Button>
					</ButtonToolbar>
				</div>
			);

		// render the cell content
		return (
			<CollapseCard collapsable={colContent} padding="small">
				{content}
			</CollapseCard>
			);
	}


	/**
	 * Return the cell size (width) that will take the whole are if it is editing
	 * @param  {[type]} item [description]
	 * @return {[type]}      [description]
	 */
	getCellSize(item) {
		return item.state === 'edit' ? { xs: 12 } : this.props.cellSize;
	}


	cardEventHandler(evt) {
		if (evt.type === 'new') {
			const doc = Form.newInstance(this.props.editorDef.layout);
			this.setState({
				editing: true,
				doc: doc,
				message: null });
		}
	}


	formEventHandler(evt) {
		const item = this.item;
		const comp = this.self;

		// user canceled the operation ?
		if (evt.type === 'cancel') {
			if (item) {
				item.state = 'ok';
			}
			comp.setState({ editing: null });
			return null;
		}

		// user confirmed the operation ?
		if (evt.type === 'ok') {
			// save the document
			const crud = comp.props.crud;
			const promise = item ? crud.update(evt.doc.id, evt.doc) : crud.create(evt.doc);

			return promise
				.then(() => {
					if (item) {
						item.state = 'ok';
					}

					comp.setState({
						values: null,
						editing: null,
						message: item ? __('default.entity_updated') : __('default.entity_created')
					});
					comp.refreshTable();
				});
		}

		return null;
	}


	editClick(evt) {
		const item = this._cmdReference(evt);

		// show wait icon
		item.state = 'fetching';
		this.forceUpdate();

		// get data to edit from server
		const self = this;
		this.props.crud.get(item.data.id)
		.then(res => {
			item.state = 'edit';
			item.doc = res;
			self.forceUpdate();
		})
		.catch(() => {
			item.state = 'ok';
			self.forceUpdate();
		});
	}

	deleteClick(evt) {
		const item = this._cmdReference(evt);
		this.setState({
			showConfirm: true,
			item: item
		});
	}

	_cmdReference(evt) {
		evt.stopPropagation();
		// prevent panel to collapse
		evt.stopPropagation();

		// recover item
		const index = evt.currentTarget.dataset.item;
		return this.state.values.list[index];
	}


	deleteConfirm(action) {
		if (action === 'yes') {
			const self = this;

			this.props.crud
				.delete(this.state.item.data.id)
				.then(() => {
					self.setState({ message: __('default.entity_deleted') });
					self.refreshTable();
				});
		}
		this.setState({ showConfirm: false, doc: null, message: null });
	}


	render() {
		if (!this.props) {
			return null;
		}

		// is it in editing mode ?
		const editing = this.state.editing && !this.state.readOnly;

		// any message to be displayed
		const msg = this.state.message;

		const formHandler = editing ? this.formEventHandler.bind({ self: this }) : null;

		return (
			<div>
				{
					msg && <Alert bsStyle="warning">{msg}</Alert>
				}
				{
					editing && <Collapse in transitionAppear>
						<div>
							<FormDialog formDef={this.props.editorDef}
								onEvent={formHandler}
								doc={this.state.doc} />
						</div>
						</Collapse>
				}
				<CrudCard title={this.props.title}
					onEvent={this.cardEventHandler}
					values={this.state.values}
					onCellSize={this.getCellSize}
					onCellRender={this.cellRender} />
				<MessageDlg show={this.state.showConfirm}
					onClose={this.deleteConfirm}
					title={__('action.delete')}
					message={__('form.confirm_remove')} style="warning" type="YesNo" />
			</div>
			);
	}
}

CrudView.propTypes = {
	title: React.PropTypes.string,
	editorDef: React.PropTypes.object,
	onCellRender: React.PropTypes.func,
	onCollapseCellRender: React.PropTypes.func,
	cellSize: React.PropTypes.object,
	perm: React.PropTypes.string,
	crud: React.PropTypes.object,
	search: React.PropTypes.bool,
	paging: React.PropTypes.bool
};

CrudView.defaultProps = {
	search: false,
	paging: false,
	cellSize: { md: 6 }
};

