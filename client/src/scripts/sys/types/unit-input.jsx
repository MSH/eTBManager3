

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
		this.onChange = this.onChange.bind(this);

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

	onChange() {
		console.log('changed');
	}

	createAdmUnitList() {
		const options = this.state.adminUnits.map(opt => (
				<option key={opt.id} value={opt.id}>
					{opt.name}
				</option>
				));

		const sc = this.props.schema;
		const label = Form.labelRender(sc.label, sc.required);

		return (
				<Input ref="admunit"
					type="select" label={label} onChange={self.onChange}>
					{options}
				</Input>
				);
	}

	/**
	 * Create the editor control to enter or change an unit
	 * @return {[type]} [description]
	 */
	editorRender(schema) {
		// admin unit is being loaded ?
		if (!this.state.adminUnits) {
			return <WaitIcon type="field" />;
		}

		const aulist = this.createAdmUnitList();

		const unitlist = (
			<Input ref="units" type="select"
				onChange={self.onChange} />
			);

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
	value: React.PropTypes.object,
	onChange: React.PropTypes.func,
	schema: React.PropTypes.object,
	errors: React.PropTypes.any
};
