

import React from 'react';
import { Input } from 'react-bootstrap';
import CRUD from '../../commons/crud';
import WaitIcon from '../../components/wait-icon';
import Types from '../../forms/types';
import Form from '../../forms/form';

const crud = new CRUD('unit');
const crudAU = new CRUD('adminunit');

export default class UnitInput extends React.Component {

	constructor(props) {
		super(props);
		this.onAuChange = this.onAuChange.bind(this);
		this.onUnitChange = this.onUnitChange.bind(this);

		this.state = {};
	}

	componentWillMount() {
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

	/**
	 * Called when user changes the administrative unit select box
	 * @return {[type]} [description]
	 */
	onAuChange() {
		const admUnit = this.refs.admunit.getValue();

		if (admUnit === '-') {
			return this.setState({ units: null });
		}

		const self = this;
		crud.query({ adminUnitId: admUnit, includeSubunits: true })
		.then(res => self.setState({ units: res.list }));

		this.setState({ units: null });
	}

	onUnitChange() {
		const id = this.refs.unit.getValue();
		if (this.props.onChange) {
			this.props.onChange({ element: this.props.schema, value: id });
		}
		this.setState({ unit: id });
	}

	createAdmUnitList() {
		// admin unit is being loaded ?
		if (!this.state.adminUnits) {
			return <WaitIcon type="field" />;
		}

		const options = this.state.adminUnits.map(opt => (
				<option key={opt.id} value={opt.id}>
					{opt.name}
				</option>
				));
		// include 'no selection' option
		options.unshift(<option key="-" value="-" >{'-'}</option>);

		const sc = this.props.schema;
		const label = Form.labelRender(sc.label, sc.required);

		return (
				<Input ref="admunit"
					type="select" label={label} onChange={this.onAuChange}>
					{options}
				</Input>
				);
	}

	createUnitList() {
		if (!this.state.units) {
			return null;
		}

		const options = this.state.units.map(opt => (
				<option key={opt.id} value={opt.id}>
					{opt.name}
				</option>
				));
		// include 'no selection' option
		options.unshift(<option key="-" value="-" >{'-'}</option>);

		return (
				<Input ref="unit"
					type="select" onChange={this.onUnitChange}>
					{options}
				</Input>
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
		const txt = Types.list.unit.displayText(this.props.value);

		return (
			<Input
				label={schema.label}
				type="text"
				disabled
				value={txt} />
			);
	}

	render() {
		const schema = this.props.schema || {};
		return schema.readOnly ? this.readOnlyRender(schema) : this.editorRender(schema);
	}
}

UnitInput.propTypes = {
	value: React.PropTypes.string,
	onChange: React.PropTypes.func,
	schema: React.PropTypes.object,
	errors: React.PropTypes.any
};
