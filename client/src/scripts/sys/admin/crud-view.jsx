/**
 * Complete component to offer CRUD capabilities to an entity. It displays the content in a table
 * and offers controls to create new items, delete and edit them
 */

import React from 'react';
import { Collapse, Alert, Button, ButtonToolbar } from 'react-bootstrap';
import FormDialog from '../../components/form-dialog';
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
		const qry = Object.assign({}, this.props.queryFilters);

		return this.props.crud.query(qry)
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
			let editor = this.props.editorDef;
			// multiple editors ?
			if (editor.editors) {
				editor = editor.select(item.data);
			}

			// display cell for editing
			return (
				<Collapse in transitionAppear>
					<div>
					<FormDialog schema={item.context.editor}
						doc={item.context.doc}
						highlight
						resources={this.context.resources}
						onConfirm={item.context.saveForm}
						onCancel={item.context.cancelForm}
						/>
					</div>
				</Collapse>
				);
		}

		const content = this.props.onCellRender(item.data);

		// generate the hidden content
		const colRender = this.props.onDetailRender;
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
			this.openNewForm(evt.key);
		}
	}


	createFormContext(editor, doc, item, resources) {
		const cntxt = {
			editor: editor,
			comp: this,
			doc: doc,
			item: item,
			errors: null,
			resources: resources
		};

		cntxt.saveForm = this.saveForm.bind(cntxt);
		cntxt.cancelForm = this.cancelForm.bind(cntxt);

		return cntxt;
	}


	openNewForm(key) {
		const edt = this.props.editorDef;
		const editor = edt.editors ? edt.editors[key] : edt;

		const doc = { };
		const formCont = this.createFormContext(editor, doc);
		// set the state
		this.setState({
			newform: formCont,
			message: null });
	}

	/**
	 * Save content of the form. This function is executed in a exclusive context
	 * @return {Promise} Promise resolved when server return
	 */
	saveForm() {
		// get reference to the context ('this' is not the react component)
		const self = this;
		const crud = this.comp.props.crud;
		const promise = this.item ? crud.update(this.item.data.id, this.doc) : crud.create(this.doc);

		return promise
			.then(() => {
				if (this.item) {
					this.item.state = 'ok';
				}

				self.comp.setState({
					values: null,
					newform: null,
					message: this.item ? __('default.entity_updated') : __('default.entity_created')
				});
				self.comp.refreshTable();
			});
	}

	/**
	 * Cancel form editor and hide form
	 * @return {[type]} [description]
	 */
	cancelForm() {
		const item = this.item;
		const comp = this.comp;

		if (item) {
			item.state = 'ok';
		}

		if (comp.state.newform) {
			comp.setState({ newform: null });
		}
		else {
			comp.updateValues();
		}
	}


	/**
	 * Called when user clicks on the edit button
	 * @param  {SyntheticMouseEvent} evt Information about the click event
	 * @return {Promise}     Promise to be resolved when data to edit is available
	 */
	editClick(evt) {
		const item = this._cmdReference(evt);

		// show wait icon
		item.state = 'fetching';
		this.updateValues();

		// select the property editor definition
		const edt = this.props.editorDef;
		const editor = edt.editors ? edt.editors[edt.select(item.data)] : edt;

		// get data to edit from server
		const self = this;
		return this.props.crud.getEdit(item.data.id)
		.then(res => {
			// set the id in order to identify if is a new doc or an existing doc
			res.id = item.data.id;
			// create a context to execute the editing operation
			const cntxt = self.createFormContext(editor, res, item);

			item.state = 'edit';
			item.context = cntxt;
			self.updateValues();
		})
		.catch(() => {
			item.state = 'ok';
			self.updateValues();
		});
	}

	updateValues() {
		const val = this.state.values;
		this.setState({ values: { count: val.count, list: val.list.slice(0) } });
	}

	/**
	 * Called when user clicks on the delete event
	 * @param  {[type]} evt [description]
	 * @return {[type]}     [description]
	 */
	deleteClick(evt) {
		const item = this._cmdReference(evt);

		// show the confirm delete dialog
		this.setState({
			showConfirm: true,
			item: item
		});
	}

	/**
	 * Get the item reference stored in the button that rose the event
	 * in a DOM attribute called data-item
	 * @param  {SyntheticMouseEvent} evt Information about the event
	 * @return {object}     The item assigned to the event
	 */
	_cmdReference(evt) {
		evt.stopPropagation();
		// prevent panel to collapse
		evt.stopPropagation();

		// recover item
		const index = evt.currentTarget.dataset.item;
		return this.state.values.list[index];
	}


	deleteConfirm(action) {
		const item = this.state.item;

		if (action === 'yes') {
			const self = this;

			this.props.crud
				.delete(item.data.id)
				.then(() => {
					self.setState({ message: __('default.entity_deleted') });
					self.refreshTable();
				});
		}

		item.state = 'ok';
		this.setState({ showConfirm: false, doc: null, message: null });
	}

	/**
	 * Rend the new form
	 * @return {React.Component} The new form, or null if not in new form mode
	 */
	newFormRender() {
		// is it in editing mode ?
		const newform = this.state.newform;

		if (!newform) {
			return null;
		}

		return (
				<Collapse in transitionAppear>
					<div>
						<FormDialog schema={newform.editor}
							onConfirm={newform.saveForm}
							onCancel={newform.cancelForm}
							doc={newform.doc} />
					</div>
				</Collapse>
			);
	}


	/**
	 * Component render
	 * @return {[type]} [description]
	 */
	render() {
		if (!this.props) {
			return null;
		}

		// any message to be displayed
		const msg = this.state.message;

		const editor = this.props.editorDef;

		// multiple options for new button ?
		const newDropDown = editor.editors ?
			Object.keys(editor.editors).map(key => ({ key: key, label: editor.editors[key].label })) :
			null;

		return (
			<div>
				{
					msg && <Alert bsStyle="warning">{msg}</Alert>
				}
				{
					this.newFormRender()
				}
				<CrudCard title={this.props.title}
					onEvent={this.cardEventHandler}
					values={this.state.values}
					newDropDown={newDropDown}
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
	onDetailRender: React.PropTypes.func,
	beforeEdit: React.PropTypes.func,
	cellSize: React.PropTypes.object,
	perm: React.PropTypes.string,
	crud: React.PropTypes.object,
	search: React.PropTypes.bool,
	paging: React.PropTypes.bool,
	queryFilters: React.PropTypes.object
};

CrudView.defaultProps = {
	search: false,
	paging: false,
	cellSize: { md: 6 }
};
