/**
 * Created by rmemoria on 29/8/15.
 */


import Http from './http.js';
import Const from './app-constants.js';
import App from './app.js';


export default {

    /**
     * Return information about the system instance
     */
    requestServerStatus: function() {
        // get system information
        Http.get('/api/sys/info')
            .end(function(err, res) {
                var info = res.body;

                App.dispatch({
                    type: Const.SYS_INFO,
                    data: info
                })
            });
    }
}