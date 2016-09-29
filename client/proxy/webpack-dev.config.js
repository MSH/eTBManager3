/*
 * Webpack development server configuration
 *
 * This file is set up for serving the webpack-dev-server, which will watch for changes and recompile as required if
 * the subfolder /webpack-dev-server/ is visited. Visiting the root will not automatically reload.
 */
'use strict';
var webpack = require('webpack'),
    path = require('path'),
    config = require('../config'),
    I18nPlugin = require('i18n-webpack-plugin');



/**
 * Return an array of configurations
 */
module.exports = config.languages.dev.map( function(lang) {

    // get list of messages for the given language
    var messages = require('../messages/messages_' + lang + '.json');
    var momentLocExpr = new RegExp('^\.\/(' + lang.toLowerCase().replace('_', '-') + ')$');

    return {
        name: lang,

        context: path.join(__dirname, '..', config.clientSrc),

        output: {
            filename: 'app.js',
            path: path.join(__dirname, config.distPath, 'scripts', lang),
            publicPath: '/scripts/' + lang + '/'
        },

        cache: true,
        debug: true,
        devtool: false,
        entry: [

            // For hot style updates
            'webpack/hot/dev-server',

            // The script refreshing the browser on none hot updates
            'webpack-dev-server/client?' + config.proxy.webpack.url,

            path.join(__dirname, '..', config.clientSrc, 'scripts', config.mainScript)
        ],

        stats: {
            colors: true,
            reasons: true
        },

        resolve: {
            extensions: ["", ".js", ".jsx"]
        },

        module: {
            loaders: [
                {
                    test: /\.jsx/,
                    loader: 'babel-loader',
                    exclude: /node_modules/,
                    query: {
                        // https://github.com/babel/babel-loader#options
                        cacheDirectory: true,
                        presets: ['es2015', 'react']
                    }
                },
                {
                    test: /\.js/,
                    loader: 'babel-loader',
                    exclude: /node_modules/,
                    query: {
                        // https://github.com/babel/babel-loader#options
                        cacheDirectory: true,
                        presets: ['es2015', 'react']
                    }
                },
                {
                    test: /\.less/,
                    loader: 'style-loader!css-loader!less-loader'
                },
                {
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
            new webpack.HotModuleReplacementPlugin(),
//            new webpack.NoErrorsPlugin(),
            new I18nPlugin(messages),
            new webpack.DefinePlugin({
                __DEV__: true
            }),
            new webpack.ContextReplacementPlugin(/moment[\\\/]locale$/, momentLocExpr)
        ]
    }
});
