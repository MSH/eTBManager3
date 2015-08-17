'use strict';

/**
 * Simple module to handle cookies in the client side
 * @author Ricardo Memoria
 */

export default {
    /**
     * Get a cookie value
     * @param  {String} cname The cookie name
     * @return {String}       Return the cookie value
     */
    get: function(cname) {
        var name = cname + '=';
        var ca = document.cookie.split(';');
        for(var i=0; i<ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) === 0) {
                return c.substring(name.length,c.length);
            }
        }
        return;
    },

    /**
     * Set a cookie value
     * @param {String} name  The cookie name
     * @param {String} value The cookie value
     * @param {int} days     The number of days the cookie will expire
     */
    put: function(name, value, days) {
        var d = new Date();
        d.setTime( d.getTime() + (days*24*60*60*1000) );
        var expires = "expires="+d.toUTCString();
        document.cookie = name + "=" + value + "; " + expires;
    }

};
