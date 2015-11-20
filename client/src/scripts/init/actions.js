

import { navigator } from '../components/router.jsx';

export const INIT_SUCCESS = 'init_success';

export function postSuccess(msg) {
    navigator.goto('/init/success');

    return {
        type: INIT_SUCCESS,
        message: msg
    };
}
