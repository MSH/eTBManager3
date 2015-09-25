/*
 * Webpack development server configuration
 *
 * This file is set up for serving the webpack-dev-server, which will watch for changes and recompile as required if
 * the subfolder /webpack-dev-server/ is visited. Visiting the root will not automatically reload.
 */
'use strict';
var webpack = require('webpack'),
    path = require('path'),
    config = require('./client/config'),
    I18nPlugin = require('i18n-webpack-plugin');


var contextPath = path.join( __dirname, 'client', config.clientSrc),
    outPath = path.join(__dirname, 'client', config.distPath);


module.exports = config.languages.prod.map( function(lang) {

    // get list of messages for the given language
    var messages;
    if (lang !== config.defaultLanguage) {
        messages = require('./client/messages/messages_' + lang + '.json');
    }

    // return the configuration to be used in production mode
    return {
        context: contextPath,

        output: {
            filename: 'main.js',
            path: path.join( outPath, 'scripts', lang),
            publicPath: '/scripts/' + lang + '/'
        },

        cache: true,
        debug: true,
        devtool: false,
        entry: [
            path.join(contextPath, 'scripts', config.mainScript)
        ],


        stats: {
            colors: true,
            reasons: true
        },


        module: {
            loaders: [
                {
                    test: /\.jsx?$/,
                    exclude: /node_modules/,
                    loader: 'babel'
                }, {
                    test: /\.less/,
                    loader: 'style-loader!css-loader!less-loader'
                }, {
                    test: /\.css$/,
                    loader: 'style-loader!css-loader'
                }, {
                    test: /\.(png|jpg)$/,
                    loader: 'url-loader?limit=8192'
                },
                {
                    test: /(?!fontawesome)\.(png|woff|woff2|eot|ttf|svg)$/,
                    loader: 'url-loader?limit=100000'
                },
                {   // font awesome fonts
                    test: /(?=.*fontawesome).*(?=.*\.woff(2))?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
                    loader: 'url-loader?limit=10000&minetype=application/font-woff'
                },
                {   // font awesome fonts
                    test: /(?=.*fontawesome).*\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
                    loader: 'file-loader'
                }
            ]
        },

        plugins: [
            new webpack.NoErrorsPlugin(),
            new webpack.optimize.UglifyJsPlugin(),
            new I18nPlugin(messages)
        ]
    }
});
