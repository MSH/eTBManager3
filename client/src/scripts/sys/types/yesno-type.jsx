
import React from 'react';
import Types from '../../forms/types';
import Fa from '../../components/fa';

/**
 * Type handle for administrative unit series
 */
export default class YesNoType extends Types.Handler {

	displayText(value) {
		return value ? __('global.yes') : __('global.no');
	}

	render(value) {
		return value ? <Fa icon="check" className="text-primary" /> : '-';
	}
}
