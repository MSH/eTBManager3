/*
 * Webpack development server configuration
 *
 * This file is set up for serving the webpack-dev-server, which will watch for changes and recompile as required if
 * the subfolder /webpack-dev-server/ is visited. Visiting the root will not automatically reload.
 */
'use strict';
var webpack = require('webpack'),
    path = require('path'),
    config = require('../config');



module.exports = {

    context: path.join( __dirname, '..', config.clientSrc),

    output: {
        filename: 'main.js',
        path: path.join(__dirname, config.distPath, 'scripts'),
        publicPath: '/scripts/'
    },

    cache: true,
    debug: true,
    devtool: false,
    entry: [

        // For hot style updates
        'webpack/hot/dev-server',

        // The script refreshing the browser on none hot updates
        'webpack-dev-server/client?' + config.proxy.webpack.url,

        config.mainScript
    ],

    stats: {
        colors: true,
        reasons: true
    },

    //resolve: {
    //    extensions: ['', '.js'],
    //    alias: {
    //        'styles': '../../../src/styles',
    //        'components': '../../../src/scripts/components/'
    //    }
    //},
    module: {
        loaders: [{
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
            test: /\.(png|woff|woff2|eot|ttf|svg)$/,
            loader: 'url-loader?limit=100000' }
        ]
    },

    plugins: [
        new webpack.HotModuleReplacementPlugin(),
        new webpack.NoErrorsPlugin()
    ]

};
