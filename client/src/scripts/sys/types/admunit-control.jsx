
import React from 'react';
import { Input } from 'react-bootstrap';
import msgs from '../../commons/messages';
import { app } from '../../core/app';

/**
 * Administrative unit type
 * @type {Object}
 */
const adminUnitType = {
	type: 'adminunit',
	validate: function(schema, value) {
		if (!typeof value !== 'object') {
			return msgs.NotValid;
		}
	},
	valueToSave: function(value) {
		return value !== null && value.p0 ? value.p0.id : null;
	},
	control: 'adminunit'
};

/**
 * Control to handle administrative unit selection
 */
export default class AdminUnitControl extends React.Component {

	auDisplay(val) {
		return Object.keys(val).map(key => val[key].name).join(', ');
	}

	renderReadOnly() {
		const el = this.props.schema;
		const val = this.props.value;
		const txt = val && val.p0 ? this.auDisplay(val) : app.getState().session.workspaceName;

		return	(
			<Input ref={el.property}
				label={el.label}
				type="text"
				disabled
				value={txt} />
			);
	}

	renderEditor() {
		return <h4>{'Not implemented'}</h4>;
	}

	render() {
		const el = this.props.schema;

		return el.readOnly ? this.renderReadOnly() : this.renderEditor();
	}
}


AdminUnitControl.propTypes = {
	schema: React.PropTypes.object,
	value: React.PropTypes.object,
	errors: React.PropTypes.object
};


// export { AdminUnitControl, adminUnitType };
