
import Http from '../commons/http';
import { navigator } from '../components/router.jsx';

export function registerWs(data) {

    return function(dispatch) {
        dispatch({
            type: 'INIT_WS',
            posting: true
        });

    }
}