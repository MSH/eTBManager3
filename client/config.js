/**
 * Configuration about the paths used to build the client code
 *
 * @author Ricardo Memoria
 * aug-2015
 */

module.exports = {

    /**
     * Folder where the client code is
     */
    clientSrc: './src',

    /**
     * Folder where client code will be generated
     */
    distPath: '../src/main/resources/static',

    /**
     * Languages used in the client side
     */
    languages: {
        // just two languages in dev, to speed-up refreshes
        dev:  ['en', 'pt_BR'],
        // all languages available in production mode
        prod: ['en', 'pt_BR', 'uk', 'ru']
    },

    defaultLanguage: 'en',

    mainScript: 'main.js',

    /**
     * The proxy server to serve static content and forward dynamic content to another server
     */
    proxy: {
        /**
         * This is the port used in the proxy server
         */
        port: 3000,

        /**
         * static content
         */
        static: './src',

        /**
         * This is the development server, that will provide dynamic content
         */
        dev: {
            url: 'http://localhost:8080',
            path: [
                '/',
                '/api/*'
            ],
            /**
             * Request delay for development purposes
             * @type {number} delay in milliseconds
             */
            requestDelay: 200
        },

        /**
         * This is the webpack server, that will provide js compiled code during development
         */
        webpack: {
            // must be informed twice because it is used in 2 different parts of the configuration
            port: 8090,
            url: 'http://localhost:8090',
            path: '/scripts/*'
        }
    }
};