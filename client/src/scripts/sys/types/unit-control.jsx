
import React from 'react';
import { WaitIcon, SelectionBox } from '../../components';
import FormUtils from '../../forms/form-utils';
import su from '../session-utils';


/**
 * Field control used in the form lib for displaying and selection of a unit
 */
export default class UnitControl extends React.Component {

    static typeName() {
        return 'unit';
    }

    static snapshot(schema, doc) {
        if (schema.workspaceId) {
            FormUtils.propEval(schema, 'workspaceId', doc);
        }
        return schema;
    }

    constructor(props) {
        super(props);
        this.onAuChange = this.onAuChange.bind(this);
        this.onUnitChange = this.onUnitChange.bind(this);
    }

    /**
     * Check if a server request is required
     * @param  {Object} nextSchema The next schema
     * @param  {Object} nextValue  The next value
     * @return {boolean}           return true if server request is required
     */
    _requestRequired(nextSchema, nextValue, nextResource) {
        const s = this.props.schema;
        // workspace has changed ?
        if (nextSchema.workspaceId !== s.workspaceId) {
            return true;
        }

        // no resources in both old and new props ?
        if (!nextResource && !this.props.resources) {
            return true;
        }

        return false;
    }

    serverRequest(nextSchema, nextValue, nextResource) {
        if (!this._requestRequired(nextSchema, nextValue, nextResource)) {
            return null;
        }

        return nextSchema.readOnly ?
            null :
            {
                cmd: 'unit',
                params: {
                    value: nextValue,
                    workspaceId: nextSchema.workspaceId,
                    type: nextSchema.unitType
                }
            };
    }

    /**
     * Called when user changes the administrative unit select box
     * @return {[type]} [description]
     */
    onAuChange(item) {
        const admUnit = item ? item.id : null;
        const resources = this.props.resources;

        resources.adminUnitId = admUnit === '-' ? null : admUnit;

        // no admin unit selected ?
        if (admUnit === '-') {
            resources.units = null;
            this.forceUpdate();
            this.onUnitChange(null);
            return;
        }

        const req = {
            cmd: 'unit',
            params: {
                // the workspace in use
                workspaceId: this.props.schema.workspaceId,
                // the workspace in use
                type: this.props.schema.unitType,
                // just the list of units
                units: true,
                // the selected admin unit
                adminUnitId: admUnit
            }
        };

        // request list of units to the server
        const self = this;
        FormUtils.serverRequest(req)
            .then(res => {
                resources.units = res.units;
                self.forceUpdate();
            });

        resources.units = null;
        resources.adminUnitId = admUnit;
        this.onUnitChange(null);
    }

    /**
     * Called when user selects a unit
     * @param  {[type]} evt  [description]
     * @param  {[type]} item [description]
     * @return {[type]}      [description]
     */
    onUnitChange(item) {
        const id = item ? item.id : null;
        const val = this.props.value ? this.props.value : null;

        if (val !== id && this.props.onChange) {
            this.props.onChange({ schema: this.props.schema, value: id });
        }
    }

    createAdmUnitList() {
        const res = this.props.resources;
        // admin unit is being loaded ?
        if (!res || !res.adminUnits) {
            return <WaitIcon type="field" />;
        }

        const sc = this.props.schema;
        const label = FormUtils.labelRender(sc.label, sc.required);

        // get the selected item
        const id = res.adminUnitId;
        const value = id ? res.adminUnits.find(item => item.id === id) : null;

        return (
                <SelectionBox ref="admunit" value={value}
                    type="select"
                    label={label}
                    onChange={this.onAuChange}
                    noSelectionLabel="-"
                    optionDisplay="name"
                    options={res.adminUnits} />
                );
    }

    createUnitList() {
        const resources = this.props.resources;
        if (!resources) {
            return <WaitIcon type="field" />;
        }

        if (!resources.units) {
            return null;
        }

        // get the selected item
        const id = this.props.value;
        const value = id ? resources.units.find(item => item.id === id) : null;

        return (
                <SelectionBox ref="unit"
                    value={value}
                    type="select"
                    onChange={this.onUnitChange}
                    noSelectionLabel="-"
                    optionDisplay="name"
                    options={resources.units} />
                );
    }

    /**
     * Create the editor control to enter or change an unit
     * @return {[type]} [description]
     */
    editorRender() {
        const aulist = this.createAdmUnitList();

        const unitlist = this.createUnitList();

        return (
            <div>
                {aulist}
                {unitlist}
            </div>
            );
    }

    readOnlyRender(schema) {
        const unit = this.props.value;
        var text = unit ? unit.name : null;
        if (unit && unit.adminUnit) {
            text = su.unitDisplay(unit, '/cases');
        }
        return FormUtils.readOnlyRender(text, schema.label);
    }

    render() {
        const schema = this.props.schema || {};
        return schema.readOnly ? this.readOnlyRender(schema) : this.editorRender();
    }
}

UnitControl.propTypes = {
    value: React.PropTypes.any,
    onChange: React.PropTypes.func,
    schema: React.PropTypes.object,
    errors: React.PropTypes.any,
    resources: React.PropTypes.object
};
