/**
 * This script is the first script executed by the web browser when the application is loaded.
 *
 * The content of this file is minified, uglified and copied to the server side (src/main/resources/templates)
 * during build process by gulp.
 *
 * The script is dynamically included in the index.ftl (freemarker template file) during execution
 *
 * @author Ricardo Memoria
 * august 2015
 *
 */

(function() {
    var LANG_KEY = 'lang',
        AUTHTOKEN_KEY = 'autk';

    /**
     * Information exposed to the main page, necessary to start the client side
     */
    var data = {
        languages: JSON.parse('${languages}'),
        contextPath: '${path}',
        defaultLanguage: '${defaultLanguage}',
        /**
         * Global function to get a cookie value
         * @param cname the name of the cookie
         */
        getCookie: function(cname) {
            var name = cname + '=';
            var s = '; ' + document.cookie;
            var vals = s.split('; ' + name);
            if (vals.length === 2) {
                return vals.pop().split(';').shift();
            }
        },
        /**
         * Set a cookie value
         * @param name is the name of the cookie
         * @param value is the value of the cookie
         * @param days is the expire date of the cookie in days
         */
        setCookie: function(name, value, days) {
            var s = name + '=' + value;
            if (days) {
                var d = new Date();
                d.setTime( d.getTime() + (days*24*60*60*1000) );
                s += "; expires="+d.toUTCString();
            }
            document.cookie = s;
        },
        getLang: function() {
            return window.app.getCookie(LANG_KEY);
        },
        setLang: function(value) {
            window.app.setCookie(LANG_KEY, value);
        },
        /**
         * Get authentication token to be sent to the client
         * @returns {*}
         */
        getAuthToken: function() {
            return window.app.getCookie(AUTHTOKEN_KEY);
        },
        setAuthToken: function(value) {
            window.app.setCookie(AUTHTOKEN_KEY, value);
        }
    };
    window.app = data;

    var setCookie = data.setCookie,
        getCookie = data.getCookie;


    // the navigator language
    // try to get language stored in the document cookie
    var navlang = getCookie(LANG_KEY) || navigator.language;

    // convert language to the java format (xx-xx to xx_XX)
    if (navlang.indexOf('-') > 0) {
        var v = navlang.split('-');
        navlang = v[0] + '_' + v[1].toUpperCase();
    }

    // the system language
    var lang;

    /**
     * Check if navigator language matches one of the available languages
     */
    if (data.languages[navlang]) {
        lang = navlang;
    }


    // language in the browser or cookie doensn't match the available languages?
    if (!lang) {
        // check if language is in the format language-country
        if (navlang.indexOf('-') > 0) {
            var mainlang = navlang.split('=').shift();
            if (data.languages[mainlang]) {
                lang = mainlang;
            }
        }

        if (!lang) {
            lang = data.defaultLanguage;
        }
    }

    /**
     * No language matched: RAISE AN ERROR (it should never happen)
     */
    if (!lang) {
        document.write('<h1>NO LANGUAGE FOUND!</h1>');
    }

    /**
     * Set the default language in the document cookie
     */
    setCookie(LANG_KEY, lang);

    /**
     * Load the main script based on the selected language
     */
    var jsElem = document.createElement("script");
    // get the language file name
    var jsfile = data.languages[lang];
    jsElem.type = "application/javascript";
    jsElem.src = data.contextPath + "/scripts/" + lang + '/' + jsfile;
    document.body.appendChild(jsElem);
})();