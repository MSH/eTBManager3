
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
class AdminUnitControl extends React.Component {

	static controlName() {
		return 'adminunit';
	}

	auDisplay(val) {
		return Object.keys(val).map(key => val[key].name).join(', ');
	}

	renderReadOnly() {
		const el = this.props.element;
		const val = el.getValue();
		const txt = val && val.p0 ? this.auDisplay(val) : app.getState().session.workspaceName;

		return	(
			<Input ref={el.data.property}
				label={el.label()}
				type="text"
				disabled
				value={txt} />
			);
	}

	renderEditor() {
		return <h4>{'Not implemented'}</h4>;
	}

	render() {
		const el = this.props.element;

		return el.isReadOnly() ? this.renderReadOnly() : this.renderEditor();
	}
}


AdminUnitControl.propTypes = {
	element: React.PropTypes.object,
	doc: React.PropTypes.object,
	errors: React.PropTypes.object
};


export { AdminUnitControl, adminUnitType };
