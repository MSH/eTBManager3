/**
 * Complete component to offer CRUD capabilities to an entity. It displays the content in a table
 * and offers controls to create new items, delete and edit them
 */

import React from 'react';
import { Collapse, Alert } from 'react-bootstrap';
import FormDialog from '../../components/form-dialog';
import TableView from './tableview';
import { hasPerm } from '../../core/session';
import WaitIcon from '../../components/wait-icon';
import MessageDlg from '../../components/message-dlg';


export default class CrudView extends React.Component {

	constructor(props) {
		super(props);
		this.state = {};
		this.onTableEvent = this.onTableEvent.bind(this);
		this.onSave = this.onSave.bind(this);
		this.onCancelEditor = this.onCancelEditor.bind(this);
		this.handleMenuCmd = this.handleMenuCmd.bind(this);
		this.closeConfirm = this.closeConfirm.bind(this);
	}

	/**
	 * Called when the new button is clicked
	 */
	onTableEvent(evt) {
		switch (evt.type) {
			case 'new': return this.setState({ editing: true, doc: {}, message: null });
			case 'cmd': return this.handleMenuCmd(evt.key, evt.item);
			default: throw new Error('Unexpected event ' + evt);
		}
	}

	handleMenuCmd(key, item) {
		if (key === 'edit') {
			this.edit(item);
		}

		if (key === 'delete') {
			this.setState({ confirm: true, item: item });
		}
	}

	/**
	 * Called when form editor requires the document being edited to be saved
	 * @return {[type]} [description]
	 */
	onSave() {
		const self = this;
		const doc = this.state.doc;
		const crud = this.props.crud;

		const promise = doc.id ? crud.update(doc.id, doc) : crud.create(doc);

		return promise
			.then(() => self.setState({ editing: false,
				table: null,
				doc: null,
				message: doc.id ? __('default.entity_updated') : __('default.entity_created') }));
	}

	edit(item) {
		this.setState({ fetching: true, item: item, message: null });
		const self = this;
		// start editing the doc
		return this.props.crud.get(item.id)
			.then(res => self.setState({ editing: true, doc: res, fetching: false, item: null }))
			.catch(res => self.setState({ fetching: false, item: null, message: res }));
	}

	delete() {
		const item = this.state.item;
		this.setState({ fetching: true, item: item, confirm: false, message: null });

		const self = this;
		return this.props.crud
			.delete(item.id)
			.then(() => self.setState({ fetching: false, item: null, message: __('default.entity_deleted'), table: null }));
	}

	/**
	 * Called when user clicks on the cancel button of the editor
	 * @return {[type]} [description]
	 */
	onCancelEditor() {
		this.setState({ editing: false });
	}

	closeConfirm(action) {
		if (action === 'yes') {
			return this.delete();
		}
		this.setState({ confirm: false, item: null });
	}

	/**
	 * Fetch data to feed the table
	 * @param  {[type]} crud [description]
	 * @return {[type]}      [description]
	 */
	fetchTable() {
		const self = this;
		this.props.crud.query({ rootUnits: true })
		.then(result => self.setState({ table: result }));
	}


	render() {
		if (!this.props) {
			return null;
		}

		const readOnly = !hasPerm(this.props.perm);

		const tableDef = {
			columns: this.props.tableDef.columns,
			title: this.props.tableDef.title
		};

		// if it is not read only, show menu
		if (!readOnly) {
			tableDef.menu = [
					{
						label: __('action.edit'),
						eventKey: 'edit'
					},
					{
						label: __('action.delete'),
						eventKey: 'delete'
					}
				];
		}

		const editorDef = this.props.editorDef;

		// is it in editing mode ?
		const editing = this.state.editing && !readOnly;

		const fetchingItem = this.state.fetching && this.state.item;

		const msg = this.state.message;

		// the display of the table
		let table;
		// check if data must be fetched from the server
		if (!(this.state && this.state.table)) {
			this.fetchTable();
			table = <WaitIcon />;
		}
		else {
			table = (
					<TableView data={this.state.table}
						readOnly={readOnly}
						tableDef={tableDef}
						search={this.props.search}
						onEvent={this.onTableEvent}
						fetchingItem={fetchingItem}
					/>);
		}

		return (
			<div>
				{
					msg && <Alert bsStyle="success">{msg}</Alert>
				}
				{
					editing && <Collapse in transitionAppear>
						<div>
							<FormDialog formDef={editorDef}
								onConfirm={this.onSave}
								onCancel={this.onCancelEditor}
								doc={this.state.doc} />
						</div>
						</Collapse>
				}
				{table}
				<MessageDlg show={this.state.confirm}
					onClose={this.closeConfirm}
					title={__('action.delete')}
					message={__('form.confirm_remove')} style="warning" type="YesNo" />
			</div>
			);
	}
}

CrudView.propTypes = {
	tableDef: React.PropTypes.object,
	editorDef: React.PropTypes.object,
	perm: React.PropTypes.string,
	crud: React.PropTypes.object,
	search: React.PropTypes.bool,
	paging: React.PropTypes.bool
};
