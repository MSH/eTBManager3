
import Form from '../../components/form';

import { adminUnitType, AdminUnitControl } from './admunit-control';

/**
 * Register the custom controlers and types
 * @return {[type]} [description]
 */
function register() {
	Form.registerType(adminUnitType);
	Form.registerControl(AdminUnitControl);
}

export { register };
