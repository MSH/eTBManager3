

import React from 'react';
import CRUD from '../../commons/crud';
import { WaitIcon, SelectionBox } from '../../components/index';
import FormUtils from '../../forms/form-utils';
import Form from '../../forms/form';

const crud = new CRUD('unit');
const crudAU = new CRUD('adminunit');

class UnitControl extends React.Component {

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
			return this.setState({
				adminUnits: resources.adminUnits,
				units: resources.units,
				adminUnitId: resources.adminUnitId
			});
		}

		// root was loaded ?
		if (!this.state.adminUnits) {
			// create the query
			const qry = {
				rootUnits: true
			};

			const self = this;

			// query the root items
			crudAU.query(qry)
			.then(res => self.setState({
				adminUnits: res.list
			}));
		}
	}

	static isServerInitRequired(schema) {
		return !schema.readOnly;
	}

	/**
	 * Called when user changes the administrative unit select box
	 * @return {[type]} [description]
	 */
	onAuChange(evt, item) {
		const admUnit = item ? item.id : null;

		if (admUnit === '-') {
			return this.setState({ units: null });
		}

		const self = this;
		crud.query({ adminUnitId: admUnit, includeSubunits: true })
		.then(res => self.setState({ units: res.list }));

		this.setState({ units: null });
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
		this.setState({ unit: id });
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
	editorRender(schema) {
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
