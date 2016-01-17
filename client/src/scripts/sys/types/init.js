
import Types from '../../forms/types';

import AdminUnitType from './adminunit-type';
import YesNoType from './yesno-type';
import UnitType from './unit-type';

function register() {
	Types.register([
		AdminUnitType,
		YesNoType,
		UnitType
		]);
}

export { register };
