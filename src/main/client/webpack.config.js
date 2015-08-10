/*
 * Webpack development server configuration
 *
 * This file is set up for serving the webpack-dev-server, which will watch for changes and recompile as required if
 * the subfolder /webpack-dev-server/ is visited. Visiting the root will not automatically reload.
 */
'use strict';
var webpack = require('webpack'),
    path = require('path');


module.exports = {

    output: {
        filename: 'main.js',
        path: path.join(__dirname, '../resources/static'),
        publicPath: '/'
    },

    cache: true,
    debug: true,
    devtool: false,
    entry: [
        path.join(__dirname, 'scripts/main.js')
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
        preLoaders: [{
            test: /\.js$/,
            exclude: /node_modules/,
            loader: 'jsxhint'
        }],
        loaders: [{
            test: /\.js$/,
            exclude: /node_modules/,
            loader: 'react-hot!jsx-loader?harmony'
        }, {
            test: /\.less/,
            loader: 'style-loader!css-loader!less-loader'
        }, {
            test: /\.css$/,
            loader: 'style-loader!css-loader'
        }, {
            test: /\.(png|jpg)$/,
            loader: 'url-loader?limit=8192'
        }]
    },

    plugins: [
        new webpack.HotModuleReplacementPlugin(),
        new webpack.NoErrorsPlugin()
    ]

};
