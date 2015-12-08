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
	}

	/**
	 * Called when the new button is clicked
	 */
	onTableEvent(evt) {
		if (evt.type === 'new') {
			return this.setState({ editing: true, doc: {} });
		}
	}

	/**
	 * Called when form editor requires the document being edited to be saved
	 * @return {[type]} [description]
	 */
	onSave() {
		const self = this;
		return this.props.crud.create(this.state.doc)
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


		const tableDef = this.props.tableDef;
		const editorDef = this.props.editorDef;

		// is it in editing mode ?
		const editing = this.state.editing && hasPerm(this.props.perm);

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
