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
    /**
     * Information exposed to the main page, necessary to start the client side
     */
    var data = {
        languages: '${languages}',
        contextPath: '${path}',
        defaultLanguage: '${defaultLanguage}'
    };
    window.appcfg = data;


    // the navigator language
    var navlang;

    // try to get language stored in the document cookie
    var s = '; ' + document.cookie;
    var vals = s.split('; lang=');
    if (vals.length == 2) {
        navlang = vals.pop().split(';').shift();
    }

    var navlang = navlang || navigator.language;
    document.write(navlang);

    // the system language
    var lang;

    /**
     * Check if navigator language matches one of the available languages
     */
    if (data.languages.indexOf(navlang) > 0) {
        lang = navlang;
    }


    // language in the browser or cookie doensn't match the available languages?
    if (!lang) {
        // check if language is in the format language-country
        if (navlang.indexOf('-') > 0) {
            var mainlang = navlang.split('=').shift();
            if (data.languages.indexOf(mainlang) >= 0) {
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
     * Load the main script based on the selected language
     */
    var jsElem = document.createElement("script");
    jsElem.type = "application/javascript";
    jsElem.src = data.contextPath + "/scripts/" + lang + "/main.js";
    document.body.appendChild(jsElem);
})();
