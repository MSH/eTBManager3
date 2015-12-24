
import React from 'react';
import { Row, Col, DropdownButton, MenuItem, Button, Collapse } from 'react-bootstrap';
import { Card } from '../../components/index';
import CRUD from '../../commons/crud';
import TreeView from '../../components/tree-view';
import { app } from '../../core/app';
import { hasPerm } from '../../core/session';
import MessageDlg from '../../components/message-dlg';
import FormDialog from '../../components/form-dialog';


const crud = new CRUD('adminunit');


/**
 * The page controller of the public module
 */
export class AdmUnits extends React.Component {

	constructor(props) {
		super(props);
		this.loadNodes = this.loadNodes.bind(this);
		this.nodeInfo = this.nodeInfo.bind(this);
		this.nodeWrapper = this.nodeWrapper.bind(this);
		this.addRoot = this.addRoot.bind(this);
		this.onSave = this.onSave.bind(this);
		this.onCancelEditor = this.onCancelEditor.bind(this);
		this.onMenuSel = this.onMenuSel.bind(this);
		this.onInitTree = this.onInitTree.bind(this);
		this.deleteItem = this.deleteItem.bind(this);

		const session = app.getState().session;
		this.root = { name: session.workspaceName, id: session.workspaceId, level: 0 };
	}

	onInitTree(handler) {
		this.tvhandler = handler;
	}

	renderNode(item) {
		return item.name;
	}

	nodeInfo(item) {
		if (item === this.root) {
			return { leaf: false, expanded: true };
		}
		return { leaf: item.unitsCount === 0 };
	}

	nodeWrapper(content, item) {
		let btn;

		// has permission to edit
		if (hasPerm(this.props.route.data.perm + '_EDT')) {
			btn = item === this.root ?
				<Button bsSize="small" onClick={this.addRoot}
					pullRight>{__('action.add') + ' ' + this.csname(1)}
				</Button> :
				<DropdownButton id="optMenu" bsSize="small" pullRight
					onSelect={this.onMenuSel}
					title={<span className="hidden-xs" >{__('form.options')}</span>}>
						<MenuItem key="edit" eventKey={{ item: item, evt: 'edit' }}>
								{__('action.edit')}
						</MenuItem>
						<MenuItem key="del" eventKey={{ item: item, evt: 'del' }}>
								{__('action.delete')}
						</MenuItem>
						{
							item.level < this.state.maxlevel &&
							<MenuItem key="add" eventKey={{ item: item, evt: 'add' }}>
								{__('action.add') + ' ' + this.csname(item.level + 1)}
							</MenuItem>
						}
				</DropdownButton>;
		}
		else {
			btn = null;
		}

		return (
			<Row key={item.name}>
				<div className="tbl-cell">
					<Col xs={7}>{content}</Col>
					<Col xs={3}>{item.csName}</Col>
					<Col xs={2}>
						{btn}
					</Col>
				</div>
			</Row>
			);
	}

	getCsOptions(level) {
		return this.state.cslist.filter(item => item.level === level);
	}

	addRoot() {
		this.setState({ editing: true,
			level: 1,
			doc: { parents: { } },
			parent: this.root });
	}

	onMenuSel(evt, key) {
		switch (key.evt) {
			case 'edit': return this.cmdEdit(key.item);
			case 'del': return this.cmdDelete(key.item);
			case 'add': return this.cmdAdd(key.item);
			default: throw new Error('Invalid evt: ' + key.evt);
		}
	}

	/**
	 * Called when user clicks on the Add 'cs' in the drop down menu
	 * @param  {[type]} item [description]
	 */
	cmdAdd(item) {
		// create the series of adin unit parents
		let aux = item;
		const parents = {};
		let index = 0;
		while (aux !== this.root) {
			parents['p' + index++] = { id: aux.id, name: aux.name };
			aux = aux.parent;
		}

		// open the editor
		this.setState({ editing: true,
			level: item.level + 1,
			doc: { parents: parents, parentId: item.id },
			parent: item });
	}

	/**
	 * Called when user clicks on the 'edit' drop down menu
	 * @param  {[type]} item [description]
	 */
	cmdEdit(item) {
		crud.get(item.id)
		.then(res => {
			this.setState({ editing: true,
				level: item.level,
				doc: res,
				item: item
			});
		});
	}

	/**
	 * Called when the user clicks on the delete item
	 * @param  {object} item The admin unit to be deleted
	 */
	cmdDelete(item) {
		this.setState({ confirm: true, item: item });
	}

	/**
	 * Called when user closes the delete confirmation dialog
	 * @param  {[type]} action [description]
	 * @return {[type]}        [description]
	 */
	deleteItem(action) {
		if (action === 'yes') {
			const self = this;
			return crud.delete(this.state.item.id)
				.then(() => self.forceUpdate());
		}
		this.setState({ confirm: false, item: null });
	}

	/**
	 * Return the name of the country structure division in the given level
	 * @param  {[type]} level [description]
	 * @return {[type]}       [description]
	 */
	csname(level) {
		if (!this.state || !this.state.cslist) {
			return null;
		}

		const name = this.state.cslist
			.filter(item => item.level === level)
			.map(item => item.name)
			.join(', ');
		return name;
	}

	/**
	 * Load nodes based on the parent
	 * @param  {[type]} parent [description]
	 * @return {[type]}        [description]
	 */
	loadNodes(parent) {
		const qry = parent !== this.root ? { parentId: parent.id } : { rootUnits: true };

		if (!this.state || !this.state.cslist) {
			qry.fetchCountryStructure = true;
		}

		const self = this;
		return crud.query(qry)
			.then(res => {
				res.list.forEach(item => Object.assign(item, { level: parent.level + 1, parent: parent }));

				if (res.csList) {
					let maxlevel = 0;
					res.csList.forEach(item => {
						if (item.level > maxlevel) {
							maxlevel = item.level;
						}
					});
					self.setState({ cslist: res.csList, maxlevel: maxlevel });
				}
				return res.list;
			});
	}

	/**
	 * Called when admin unit must be saved
	 * @return {[type]} [description]
	 */
	onSave() {
		const self = this;
		const doc = this.state.doc;

		let prom;
		// is an existing item ?
		if (doc.id) {
			doc.level = this.state.item.level;
			prom = crud.update(doc.id, doc)
				.then(() => self.tvhandler.updateNode(this.state.item, doc));
		}
		else {
			prom = crud.create(doc)
				.then(res => {
					doc.id = res;
					doc.unitsCount = 0;
					// get the country structure name
					doc.csName = this.state.cslist.find(item => item.id === doc.csId).name;
					// add to the tree
					self.tvhandler.addNode(self.state.parent, doc);
				});
		}

		return prom.then(() => self.setState({ editing: false }));
	}

	/**
	 * Called when user clicks on the cancel button of the editor
	 * @return {[type]} [description]
	 */
	onCancelEditor() {
		this.setState({ editing: false });
	}


	/**
	 * Return the editor used for add or edit an administrative unit
	 * @return {object} editor definition to be used in form dialog
	 */
	getEditorDef() {
		return {
			layout: [
				{
					property: 'name',
					required: true,
					type: 'string',
					max: 200,
					label: __('form.name'),
					size: { sm: 6 }
				},
				{
					property: 'parents',
					type: 'adminunit',
					label: __('admin.adminunits.parentunit'),
					readOnly: true,
					size: { sm: 6, newLine: true }
				},
				{
					property: 'csId',
					type: 'string',
					label: __('admin.adminunits.countrystructure'),
					options: this.getCsOptions(this.state.level),
					required: true,
					size: { sm: 6 }
				},
				{
					property: 'customId',
					type: 'string',
					max: 50,
					label: __('form.customId'),
					size: { sm: 6 }
				}
			],
			title: doc => doc && doc.id ? __('admin.adminunits.edt') : __('admin.adminunits.new')
		};
	}

	render() {
		const state = this.state ? this.state : {};

		// display the titles
		const title = (
			<Row key="title" className="title">
				<div style={{ textWeight: 'bold' }}>
				<Col xs={7}>{__('form.name')}</Col>
				<Col xs={3}>{__('global.location')}</Col>
				</div>
			</Row>
			);

		const editing = state.editing;

		// render the view
		return (
			<div>
				{
					editing && <Collapse in transitionAppear>
						<div>
							<FormDialog formDef={this.getEditorDef()}
								onConfirm={this.onSave}
								onCancel={this.onCancelEditor}
								doc={this.state.doc} />
						</div>
						</Collapse>
				}
				<Card title={__('admin.adminunits')}>
					<TreeView onGetNodes={this.loadNodes}
						root={[this.root]}
						innerRender={this.renderNode}
						outerRender={this.nodeWrapper}
						nodeInfo={this.nodeInfo}
						onInit={this.onInitTree}
						title={title}
					/>
				</Card>
				<MessageDlg show={state.confirm}
					onClose={this.deleteItem}
					title={__('action.delete')}
					message={__('form.confirm_remove')} style="warning" type="YesNo" />
			</div>
			);
	}
}

AdmUnits.propTypes = {
	route: React.PropTypes.object
};
