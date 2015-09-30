
import { registerReducer } from '../core/reducers.js';
import { INIT_SUCCESS } from './actions.js';

let _ini = false;

export function init() {
    if (_ini) {
        return;
    }

    registerReducer(INIT_SUCCESS, reducInitSuccess);

    _ini = true;
}

function reducInitSuccess(state, action) {
    let obj = { message: action.message};
    for (let k in state) {
        obj[k] = state[k];
    }

    return obj;
}