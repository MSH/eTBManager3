import React from 'react';
import { Row, Col, FormGroup, FormControl, ControlLabel } from 'react-bootstrap';
import su from '../session-utils';
import FormUtils from '../../forms/form-utils';
import AdminUnitControl from './admin-unit-control';

import './person-name-control.less';

export default class AddressControl extends React.Component {

	static typeName() {
		return 'address';
	}

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
		this.adminUnitChange = this.adminUnitChange.bind(this);
	}

	serverRequest(nextSchema, nextValue, nextResources) {
		const auCtrl = this.refs.adminUnit;
		if (!auCtrl) {
			return null;
		}

		return auCtrl.serverRequest(nextSchema,
			nextValue && nextValue.adminUnit ? nextValue.adminUnit : null,
			nextResources);
	}

	/**
	 * Called everytime the user changes the name
	 */
	onChange(evt) {
		if (this.props.onChange) {
			const fieldVal = evt.target.value;

			// update just the changed field
			const field = {};
			field[evt.target.id] = fieldVal;
			// create a new person name object
			const val = Object.assign({}, this.props.value, field);

			this.props.onChange({ schema: this.props.schema, value: val });
		}
	}

	adminUnitChange(evt) {
		const value = Object.assign({}, this.props.value, { adminUnit: evt.value });
		this.props.onChange({ schema: this.props.schema, value: value });
	}

	render() {
		const value = this.props.value || {};
		const schema = this.props.schema;

		// field is just for displaying ?
		if (schema.readOnly) {
			const content = su.addressDisplay(value);
			return FormUtils.readOnlyRender(content, schema.label);
		}

		const auSchema = {
			required: schema.required
		};
		const adminUnit = value && value.adminUnit ? value.adminUnit : null;

		return (
			<div>
				{schema.label &&
					<Row>
						<Col sm={12}>
							<div className="addresstitle">{schema.label}</div>
						</Col>
					</Row>
				}
				<div className="address-edt">
					<FormGroup>
						<ControlLabel>{__('Address.address')}</ControlLabel>
						<FormControl id="address"
							type="text"
							value={value.address}
							onChange={this.onChange}
							/>
					</FormGroup>
					<FormGroup>
						<ControlLabel>{__('Address.complement')}</ControlLabel>
						<FormControl id="complement"
							type="text"
							value={value.complement}
							onChange={this.onChange}
							/>
					</FormGroup>
					<Row>
						<Col sm={6}>
							<FormGroup>
								<ControlLabel>{__('Address.zipCode')}</ControlLabel>
								<FormControl id="zipCode"
									type="text"
									value={value.zipCode}
									onChange={this.onChange}
									/>
							</FormGroup>
						</Col>
					</Row>
					<AdminUnitControl
						ref="adminUnit"
						value={adminUnit}
						onChange={this.adminUnitChange}
						schema={auSchema}
						resources={this.props.resources} />
				</div>
			</div>
		);
	}
}


AddressControl.propTypes = {
	value: React.PropTypes.object,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	schema: React.PropTypes.object,
	resources: React.PropTypes.any
};
