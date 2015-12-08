/**
 * Complete component to offer CRUD capabilities to an entity. It displays the content in a table
 * and offers controls to create new items, delete and edit them
 */

import React from 'react';
import { Collapse } from 'react-bootstrap';
import FormDialog from '../../components/form-dialog';
import TableView from './tableview';
import { hasPerm } from '../../core/session';
import WaitIcon from '../../components/wait-icon';


export default class CrudView extends React.Component {

	constructor(props) {
		super(props);
		this.state = {};
		this.onTableEvent = this.onTableEvent.bind(this);
		this.onSave = this.onSave.bind(this);
		this.onCancel = this.onCancel.bind(this);
		this.handleMenuCmd = this.handleMenuCmd.bind(this);
	}

	/**
	 * Called when the new button is clicked
	 */
	onTableEvent(evt) {
		switch (evt.type) {
			case 'new': return this.setState({ editing: true, doc: {} });
			case 'cmd': return this.handleMenuCmd(evt.key, evt.item);
			default: throw new Error('Unexpected event ' + evt);
		}
	}

	handleMenuCmd(key, item) {
		if (key === 'edit') {
			const self = this;
			// start editing the doc
			this.props.crud.get(item.id)
			.then(res => self.setState({ editing: true, doc: res }));
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
			.then(() => self.setState({ editing: false, table: null, doc: null }));
	}

	/**
	 * Called when user clicks on the cancel button of the editor
	 * @return {[type]} [description]
	 */
	onCancel() {
		this.setState({ editing: false });
	}

	/**
	 * Fetch data to feed the table
	 * @param  {[type]} crud [description]
	 * @return {[type]}      [description]
	 */
	fetchData() {
		const self = this;
		this.props.crud.query({ rootUnits: true })
		.then(result => self.setState({ table: result }));

	}


	render() {
		if (!this.props) {
			return null;
		}

		// check if data must be fetched from the server
		if (!(this.state && this.state.table)) {
			this.fetchData();
			return <WaitIcon />;
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

		return (
			<div>
				{
					editing && <Collapse in transitionAppear>
						<div>
							<FormDialog formDef={editorDef}
								onConfirm={this.onSave}
								onCancel={this.onCancel}
								doc={this.state.doc} />
						</div>
						</Collapse>
				}
				<TableView data={this.state.table}
					readOnly={readOnly}
					tableDef={tableDef}
					search={this.props.search}
					onEvent={this.onTableEvent}
					/>
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
