
import React from 'react';
import { Input } from 'react-bootstrap';
import { WaitIcon, SelectionBox } from '../../components/index';
import FormUtils from '../../forms/form-utils';
import Form from '../../forms/form';


/**
 * Field control used in the form lib for displaying and selection of a unit
 */
class UnitControl extends React.Component {

	static serverRequest(schema, val) {
		// check if workspaceId property was defined but no value in the current state
		if ('workspaceId' in schema && !schema.workspaceId) {
			return null;
		}

		return schema.readOnly ?
			null :
			{
				cmd: 'unit',
				params: {
					value: val,
					workspaceId: schema.workspaceId
				}
			};
	}

	static snapshot(schema, doc) {
		if (schema.workspaceId) {
			FormUtils.propEval(schema, 'workspaceId', doc);
		}
	}

	constructor(props) {
		super(props);
		this.onAuChange = this.onAuChange.bind(this);
		this.onUnitChange = this.onUnitChange.bind(this);

		this.state = {};
	}

	componentWillMount() {
		// resources are available to initialize the field ?
		const resources = this.props.resources;
		if (resources) {
			this.setState({
				adminUnits: resources.adminUnits,
				units: resources.units,
				adminUnitId: resources.adminUnitId
			});
			return;
		}

		// root was loaded ?
		if (!this.state.adminUnits) {
			const req = UnitControl.serverRequest(this.props.schema, this.props.value);
			if (!req) {
				return;
			}

			const self = this;

			FormUtils.serverRequest(req)
				.then(res => {
					self.setState(res);
				});
		}
	}

	componentWillReceiveProps(nextProps) {
		const res = nextProps.resources;

		// check if resources changed
		if (res && this.props.resources !== res) {
			console.log('4. new properties ', res);
			this.setState({
				adminUnits: res.adminUnits,
				units: res.units,
				adminUnitId: res.adminUnitId,
				unit: null
			});
		}
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
//		this.setState({ unit: id });
	}

	createAdmUnitList() {
		// admin unit is being loaded ?
		if (!this.state.adminUnits) {
			return <WaitIcon type="field" />;
		}

		const sc = this.props.schema;
		const label = FormUtils.labelRender(sc.label, sc.required);

		// get the selected item
		const id = this.state.adminUnitId;
		const value = id ? this.state.adminUnits.find(item => item.id === id) : null;
		console.log('5. Rendering adminunits value = ', value);

		return (
				<SelectionBox ref="admunit" value={value}
					type="select" label={label} onChange={this.onAuChange}
					noSelectionLabel="-"
					optionDisplay="name"
					options={this.state.adminUnits} />
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
				<SelectionBox ref="unit" value={value}
					type="select" onChange={this.onUnitChange}
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

UnitControl.options = {
	supportedTypes: 'unit'
};

export default Form.typeWrapper(UnitControl);
