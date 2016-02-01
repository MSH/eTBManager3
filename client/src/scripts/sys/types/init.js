
import Form from '../../forms/form';

import AdminUnitControl from './admin-unit-control';
import YesNoControl from './yesno-control';
import UnitControl from './unit-control';
import MultiSelect from './multi-select';

function register() {
	Form.registerType([
		AdminUnitControl,
		YesNoControl,
		UnitControl,
		MultiSelect
		]);
}

export { register };
