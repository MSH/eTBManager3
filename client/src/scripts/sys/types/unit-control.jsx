
import React from 'react';
import { Input } from 'react-bootstrap';
import { WaitIcon, SelectionBox } from '../../components/index';
import FormUtils from '../../forms/form-utils';


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

		this.state = {};
	}

	componentWillMount() {
		this.updateState(this.props.resources);
	}

	componentWillReceiveProps(props) {
		this.updateState(props.resources);
	}

	/**
	 * Update the state based on the resources sent from the parent
	 * @param  {[type]} resources [description]
	 * @return {[type]}           [description]
	 */
	updateState(resources) {
		if (!resources) {
			return;
		}

		// update the selected admin unit ID
		if (this.units) {
			this.setState({ auId: resources.adminUnitId, units: resources.units });
		}
		else {
			this.setState({ auId: resources.adminUnitId });
		}
	}

	/**
	 * Check if a server request is required
	 * @param  {Object} nextSchema The next schema
	 * @param  {Object} nextValue  The next value
	 * @return {boolean}           return true if server request is required
	 */
	_requestRequired(nextSchema, nextValue, nextResource) {
		const s = this.props.schema;
		// parameters have changed ?
		if (nextSchema.workspaceId !== s.workspaceId) {
			return true;
		}

		// no resources in both old and new props ?
		if (!nextResource && !this.props.resources) {
			return true;
		}

		return false;
//		return this.props.value !== nextValue && !res;
//		const it = res.units.find(opt => opt.id === nextValue);
//		return !it;
	}

	serverRequest(nextSchema, nextValue, nextResource) {
		console.log('hi');
		if (!this._requestRequired(nextSchema, nextValue, nextResource)) {
			return null;
		}

		return nextSchema.readOnly ?
			null :
			{
				cmd: 'unit',
				params: {
					value: nextValue,
					workspaceId: nextSchema.workspaceId
				}
			};
	}

	/**
	 * Called when user changes the administrative unit select box
	 * @return {[type]} [description]
	 */
	onAuChange(evt, item) {
		const admUnit = item ? item.id : null;

		if (admUnit === '-') {
			this.setState({ units: null });
			return;
		}

		const req = {
			cmd: 'unit',
			params: {
				// the workspace in use
				workspaceId: this.props.schema.workspaceId,
				// just the list of units
				units: true,
				// the selected admin unit
				adminUnitId: admUnit
			}
		};

		// request list of units to the server
		const self = this;
		FormUtils.serverRequest(req)
			.then(res => self.setState({ units: res.units }));

		this.setState({ units: null, adminUnitId: admUnit });
		this.onUnitChange(null, null);
	}

	/**
	 * Called when user selects a unit
	 * @param  {[type]} evt  [description]
	 * @param  {[type]} item [description]
	 * @return {[type]}      [description]
	 */
	onUnitChange(evt, item) {
		const id = item ? item.id : null;
		if (this.props.onChange) {
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
		const id = this.state.adminUnitId;
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
		if (!this.state.units) {
			return null;
		}

		// get the selected item
		const id = this.props.value;
		const value = id ? this.state.units.find(item => item.id === id) : null;

		return (
				<SelectionBox ref="unit"
					value={value}
					type="select"
					onChange={this.onUnitChange}
					noSelectionLabel="-"
					optionDisplay="name"
					options={this.state.units} />
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
		return (
			<Input
				label={schema.label}
				type="text"
				disabled
				value={this.props.value} />
			);
	}

	render() {
		const schema = this.props.schema || {};
		return schema.readOnly ? this.readOnlyRender(schema) : this.editorRender(schema);
	}
}

UnitControl.propTypes = {
	value: React.PropTypes.string,
	onChange: React.PropTypes.func,
	schema: React.PropTypes.object,
	errors: React.PropTypes.any,
	resources: React.PropTypes.object
};
