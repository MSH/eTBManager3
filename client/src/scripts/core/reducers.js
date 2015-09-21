'use strict';

import {APP_RUN} from './actions';


const initialState = {
};


let reducers = {};


export function appReducer(state, action) {
	if (state === undefined) {
		return initialState;
	}

	var reducer = reducers[action.type];
	if (reducer) {
		return reducer(state, action);
	}
}

export function addReducer(type, func) {
	if (reducers[type]) {
		throw new Error('type ' + type + ' already registered as a reducer');
	}
	reducers[type] = func;
}

/**
 * App run
 */
reducers[APP_RUN] = function(state, action) {
	if (action.data) {
		return {
			app: action.data
		};
	}

	return {
		fetching: action.fetching
	};
};
