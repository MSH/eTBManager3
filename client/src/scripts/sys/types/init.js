
import Types from '../../forms/types';

import AdminUnitType from './adminunit-type';
import YesNoType from './yesno-type';

function register() {
	Types.register([
		AdminUnitType,
		YesNoType
		]);
}

export { register };
