
import React from 'react';
import { Grid, Row, Col, DropdownButton, MenuItem, Button, Collapse } from 'react-bootstrap';
import { Card, Fa, FormDialog } from '../../../components/index';
import CRUD from '../../../commons/crud';
import TreeView from '../../../components/tree-view';
import { app } from '../../../core/app';
import { hasPerm } from '../../session';
import CountryStructures from './country-structures';
import { DOC_CREATE, DOC_UPDATE, DOC_DELETE } from '../../../core/actions';


const crud = new CRUD('adminunit');


/**
 * The page controller of the public module
 */
export default class AdmUnits extends React.Component {

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
        this.onCsChange = this.onCsChange.bind(this);
        this.onCsToggle = this.onCsToggle.bind(this);
        this.state = { root: this.createRoot() };
    }

    componentDidMount() {
        app.add(this.onCsChange);
    }

    componentWillUnmount() {
        app.remove(this.onCsChange);
    }

    /**
     * Create the root object of the tree view
     * @return {[type]} [description]
     */
    createRoot() {
        const session = app.getState().session;
        const key = this.state && this.state.root ? this.state.root.key + 1 : 1;
        return { name: session.workspaceName, id: session.workspaceId, level: 0, key: key };
    }

    /**
     * Called by the app if the list of country structures is modified
     * (user editing the country structure list)
     * @param  {[type]} act  [description]
     * @param  {[type]} data [description]
     * @return {[type]}      [description]
     */
    onCsChange(act, data) {
        if ((act === DOC_CREATE || act === DOC_UPDATE || act === DOC_DELETE) && (data.type === 'countrystructure')) {
            this.setState({ cslist: null, root: this.createRoot() });
        }
    }

    onInitTree(handler) {
        this.tvhandler = handler;
    }

    renderNode(item) {
        return item.name;
    }

    nodeInfo(item) {
        if (item === this.state.root) {
            return { leaf: false, expanded: true };
        }
        return { leaf: item.unitsCount === 0 };
    }

    nodeWrapper(content, item) {
        let btn;

        // has permission to edit
        if (hasPerm(this.props.route.data.perm + '_EDT')) {
            btn = item === this.state.root ?
                (
                <Button bsSize="small" onClick={this.addRoot}>
                    <Fa icon="plus"/><span className="hidden-xs">{__('action.add') + ' ' + this.csname(1)}</span>
                </Button>) : (
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
                </DropdownButton>);
        }
        else {
            btn = null;
        }

        return (
            <Row key={item.name} className="tbl-cell">
                <Col xs={7}>{content}</Col>
                <Col xs={3}>{item.countryStructure ? item.countryStructure.name : ''}</Col>
                <Col xs={2}>
                    {btn}
                </Col>
            </Row>
        );
    }

    getCsOptions(level) {
        return this.state.cslist.filter(item => item.level === level);
    }

    /**
     * Called when user clicks on the add button of the workspace node
     */
    addRoot() {
        this.setState({ editing: true,
            level: 1,
            doc: { parentId: { } },
            parent: this.state.root });
    }

    onMenuSel(key) {
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
        while (aux !== this.state.root) {
            parents['p' + index++] = { id: aux.id, name: aux.name };
            aux = aux.parent;
        }

        // open the editor
        this.setState({ editing: true,
            level: item.level + 1,
            doc: { parentId: parents },
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
                item: item,
                parent: item.parent
            });
        });
    }

    /**
     * Called when the user clicks on the delete item
     * @param  {object} item The admin unit to be deleted
     */
    cmdDelete(item) {
        const self = this;
        app.messageDlg({
            title: __('action.delete'),
            message: __('form.confirm_remove'),
            style: 'warning',
            type: 'YesNo'
        })
        .then(res => {
            if (res === 'yes') {
                return crud.delete(item.id)
                    .then(() => {
                        self.tvhandler.remNode(item);
                        self.forceUpdate();
                    });
            }
            return res;
        });
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
        const qry = parent !== this.state.root ? { parentId: parent.id } : { rootUnits: true };

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

    onEditorEvent(evt) {
        if (evt.type === 'ok') {
            this.onSave();
        }
        else {
            this.onCancelEditor();
        }
    }

    /**
     * Called when admin unit must be saved
     * @return {[type]} [description]
     */
    onSave() {
        const self = this;
        const doc = this.state.doc;
        const req = {
            name: doc.name,
            parentId: doc.parentId && doc.parentId.p0 ? doc.parentId.p0.id : null,
            countryStructure: doc.countryStructure,
            customId: doc.customId
        };

        let prom;
        doc.level = this.state.level;
        doc.parent = this.state.parent;

        // is an existing item ?
        if (doc.id) {
            prom = crud.update(doc.id, req)
                .then(() => self.tvhandler.updateNode(this.state.item, doc));
        }
        else {
            prom = crud.create(req)
                .then(res => {
                    doc.id = res;
                    doc.unitsCount = 0;
                    // get the country structure name
                    doc.countryStructure = this.state.cslist.find(item => item.id === doc.countryStructure);
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
     * Called when user wants to hide or show the country structure list
     * @return {[type]} [description]
     */
    onCsToggle() {
        this.setState({ showcs: !this.state.showcs });
    }

    /**
     * Return the editor used for add or edit an administrative unit
     * @return {object} editor definition to be used in form dialog
     */
    getEditorDef() {
        return {
            controls: [
                {
                    property: 'name',
                    required: true,
                    type: 'string',
                    max: 200,
                    label: __('form.name'),
                    size: { md: 6 }
                },
                {
                    property: 'parentId',
                    type: 'adminUnit',
                    label: __('admin.adminunits.parentunit'),
                    readOnly: true,
                    size: { md: 6 }
                },
                {
                    property: 'countryStructure',
                    type: 'select',
                    label: __('admin.adminunits.countrystructure'),
                    options: this.getCsOptions(this.state.level),
                    required: true,
                    size: { md: 6, newLine: true }
                },
                {
                    property: 'customId',
                    type: 'string',
                    max: 50,
                    label: __('form.customId'),
                    size: { md: 6 }
                }
            ],
            title: doc => doc && doc.id ? __('admin.adminunits.edt') : __('admin.adminunits.new')
        };
    }

    /**
     * Render the component
     */
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

        const btntitle = this.state.showcs ? __('admin.adminunits.hidecs') : __('admin.adminunits.showcs');

        const header = (
            <Row>
                <Col sm={7}>
                    <h4>{__('admin.adminunits')}</h4>
                </Col>
                <Col sm={5}>
                    <Button block onClick={this.onCsToggle}>{btntitle}</Button>
                </Col>
            </Row>
            );

        // render the view
        return (
            <div>
                {
                    state.showcs && <CountryStructures/>
                }
                {
                    editing && <Collapse in transitionAppear>
                        <div>
                            <FormDialog schema={this.getEditorDef()}
                                onConfirm={this.onSave}
                                onCancel={this.onCancelEditor}
                                doc={this.state.doc}
                                wrapType="card" />
                        </div>
                        </Collapse>
                }
                <Card header={header}>
                    <Grid fluid>
                    <TreeView key={state.root ? state.root.key : -1}
                        onGetNodes={this.loadNodes}
                        root={[state.root]}
                        innerRender={this.renderNode}
                        outerRender={this.nodeWrapper}
                        nodeInfo={this.nodeInfo}
                        onInit={this.onInitTree}
                        title={title}
                    />
                    </Grid>
                </Card>
            </div>
        );
    }
}

AdmUnits.propTypes = {
    route: React.PropTypes.object
};
