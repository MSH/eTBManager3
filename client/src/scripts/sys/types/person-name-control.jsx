import React from 'react';
import { Row, Col, FormControl, ControlLabel } from 'react-bootstrap';
import { app } from '../../core/app';
import su from '../session-utils';
import FormUtils from '../../forms/form-utils';

import './person-name-control.less';

export default class PersonNameControl extends React.Component {

	static typeName() {
		return 'personName';
	}

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
	}

	/**
	 * Called everytime the user changes the name
	 */
	onChange(evt) {
		if (this.props.onChange) {
			const fieldVal = evt.target.value;
			const id = evt.target.id;
			let val;

			if (id === 'fullName') {
				val = { name: fieldVal };
			} else {
				// update just the changed field
				const field = {};
				field[evt.target.id] = fieldVal;
				// create a new person name object
				val = Object.assign({}, this.props.value, field);
			}

			console.log(val);
			this.props.onChange({ schema: this.props.schema, value: val });
		}
	}

	calcFields() {
		const nameComp = app.getState().session.patientNameComposition;

		switch (nameComp) {
			case 'FIRSTSURNAME': return ['name', 'middleName'];
			case 'SURNAME_FIRSTNAME': return ['middleName', 'name'];
			case 'FIRST_MIDDLE_LASTNAME': return ['name', 'middleName', 'lastName'];
			case 'LAST_FIRST_MIDDLENAME': return ['lastName', 'name', 'middleName'];
			case 'LAST_FIRST_MIDDLENAME_WITHOUT_COMMAS': return ['lastName', 'name'];
			default: return ['fullName'];
		}
	}


	placeHolder(id) {
		switch (id) {
			case 'middleName': return __('Patient.middleName');
			case 'lastName': return __('Patient.lastName');
			case 'fullName': return __('Patient.fullName');
			default: return __('Patient.firstName');
		}
	}


	render() {
		// field is just for displaying ?
		if (this.props.schema.readOnly) {
			const content = su.nameDisplay(this.props.value);
			return FormUtils.readOnlyRender(content, this.props.schema.label);
		}

		const fields = this.calcFields();

		const size = { sm: 12 / fields.length };

		return (
			<div className="form-group person-name">
			<Row>
				<Col sm={12}>
					<ControlLabel>{'Patient name:'}</ControlLabel>
				</Col>
			</Row>
			<Row>
			{
				fields.map(id => (
					<Col {...size} key={id} >
						<FormControl id={id}
							type="text"
							placeholder={this.placeHolder(id)}
							onChange={this.onChange} />
					</Col>
				))
			}
			</Row>
			</div>
		);
	}
}


PersonNameControl.propTypes = {
	value: React.PropTypes.object,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	schema: React.PropTypes.object
};
