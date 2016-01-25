
import Form from '../../forms/form';

import AdminUnitControl from './admin-unit-control';
import YesNoControl from './yesno-control';
import UnitControl from './unit-control';

function register() {
	Form.registerType([
		AdminUnitControl,
		YesNoControl,
		UnitControl
		]);
}

export { register };
